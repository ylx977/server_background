/**
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.gws.rdb.sharding.router.single;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.gws.rdb.sharding.api.HintShardingValueManager;
import com.gws.rdb.sharding.api.ShardingValue;
import com.gws.rdb.sharding.api.rule.DataNode;
import com.gws.rdb.sharding.api.rule.ShardingRule;
import com.gws.rdb.sharding.api.rule.TableRule;
import com.gws.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.gws.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.gws.rdb.sharding.parser.result.router.Condition;
import com.gws.rdb.sharding.parser.result.router.ConditionContext;
import com.gws.rdb.sharding.parser.result.router.SQLStatementType;
import com.gws.utils.GwsLogger;
import com.gws.utils.GwsLoggerTypeEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * 单逻辑表的库表路由.
 * 
 * @author wangdong, wangdong
 */
@Slf4j
public final class SingleTableRouter {
    
    private final ShardingRule shardingRule;
    
    private final String logicTable;
    
    private final ConditionContext conditionContext;
    
    private final Optional<TableRule> tableRule;
    
    private final SQLStatementType sqlStatementType;
    
    public SingleTableRouter(final ShardingRule shardingRule, final String logicTable, final ConditionContext conditionContext, final SQLStatementType sqlStatementType) {
        this.shardingRule = shardingRule;
        this.logicTable = logicTable;
        this.conditionContext = conditionContext;
        this.sqlStatementType = sqlStatementType;
        tableRule = shardingRule.findTableRule(logicTable);
    }
    
    /**
     * 路由.
     * 
     * @return 路由结果
     */
    public SingleRoutingResult route() {
        if (!tableRule.isPresent()) {
            log.trace("Can not find table rule of [{}]", logicTable);
            return null;
        }
        Collection<String> routedDataSources = routeDataSources();
        Collection<String> routedTables = routeTables(routedDataSources);
        return generateRoutingResult(routedDataSources, routedTables);
    }
    
    private Collection<String> routeDataSources() {
        DatabaseShardingStrategy strategy = shardingRule.getDatabaseShardingStrategy(tableRule.get());
        Optional<List<ShardingValue<?>>> hintDatabaseShardingValues = HintShardingValueManager.getShardingValueOfDatabase(logicTable);
        List<ShardingValue<?>> databaseShardingValues;
        if (hintDatabaseShardingValues.isPresent()) {
            databaseShardingValues = filterShardingValue(strategy.getShardingColumns(), hintDatabaseShardingValues.get());
        } else {
            databaseShardingValues = getShardingValues(strategy.getShardingColumns());
        }
        logBeforeRoute("database", logicTable, tableRule.get().getActualDatasourceNames(), strategy.getShardingColumns(), databaseShardingValues);
        Collection<String> result = new HashSet<>(strategy.doSharding(sqlStatementType, tableRule.get().getActualDatasourceNames(), databaseShardingValues));
        logAfterRoute("database", logicTable, result);
        Preconditions.checkState(!result.isEmpty(), "no database route info");
        return result;
    }
    
    private Collection<String> routeTables(final Collection<String> routedDataSources) {
        TableShardingStrategy strategy = shardingRule.getTableShardingStrategy(tableRule.get());
        Optional<List<ShardingValue<?>>> hintTableShardingValues = HintShardingValueManager.getShardingValueOfTable(logicTable);
        List<ShardingValue<?>> tableShardingValues;
        if (hintTableShardingValues.isPresent()) {
            tableShardingValues = filterShardingValue(strategy.getShardingColumns(), hintTableShardingValues.get());
        } else {
            tableShardingValues = getShardingValues(strategy.getShardingColumns());
        }
        logBeforeRoute("table", logicTable, tableRule.get().getActualTables(), strategy.getShardingColumns(), tableShardingValues);
        Collection<String> result = new HashSet<>(strategy.doSharding(sqlStatementType, tableRule.get().getActualTableNames(routedDataSources), tableShardingValues));
        logAfterRoute("table", logicTable, result);
        Preconditions.checkState(!result.isEmpty(), "no table route info");
        return result;
    }
    
    private List<ShardingValue<?>> getShardingValues(final Collection<String> shardingColumns) {
        List<ShardingValue<?>> result = new ArrayList<>(shardingColumns.size());
        for (String each : shardingColumns) {
            Optional<Condition> condition = conditionContext.find(logicTable, each);
            if (condition.isPresent()) {
                result.add(SingleRouterUtil.convertConditionToShardingValue(condition.get()));
            }
        }
        return result;
    }
    
    private List<ShardingValue<?>> filterShardingValue(final Collection<String> shardingColumns, final List<ShardingValue<?>> shardingValues) {
        List<ShardingValue<?>> result = new ArrayList<>(shardingColumns.size());
        for (ShardingValue<?> each : shardingValues) {
            if (shardingColumns.contains(each.getColumnName())) {
                result.add(each);
            }
        }
        return result;
    }
    
    private void logBeforeRoute(final String type, final String logicTable, final Collection<?> targets, final Collection<String> shardingColumns, final List<ShardingValue<?>> shardingValues) {
        log.trace("Before {} sharding {} routes db names: {} sharding columns: {} sharding values: {}", type, logicTable, targets, shardingColumns, shardingValues);
		GwsLogger.info(GwsLoggerTypeEnum.SQLTRACE, "routes db: {} sharding columns: {} sharding values: {}", targets, shardingColumns, shardingValues);
    }
    
    private void logAfterRoute(final String type, final String logicTable, final Collection<String> shardingResults) {
        log.trace("After {} sharding {} result: {}", type, logicTable, shardingResults);
    }
    
    private SingleRoutingResult generateRoutingResult(final Collection<String> routedDataSources, final Collection<String> routedTables) {
        SingleRoutingResult result = new SingleRoutingResult();
        for (DataNode each : tableRule.get().getActualDataNodes(routedDataSources, routedTables)) {
            result.put(each.getDataSourceName(), new SingleRoutingTableFactor(logicTable, each.getTableName()));
        }
        return result;
    }
}
