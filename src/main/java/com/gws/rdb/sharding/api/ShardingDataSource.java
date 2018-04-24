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

package com.gws.rdb.sharding.api;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.google.common.base.Preconditions;
import com.gws.rdb.sharding.api.config.ShardingConfiguration;
import com.gws.rdb.sharding.api.config.ShardingConfigurationConstant;
import com.gws.rdb.sharding.api.rule.ShardingRule;
import com.gws.rdb.sharding.exception.ShardingJdbcException;
import com.gws.rdb.sharding.executor.ExecutorEngine;
import com.gws.rdb.sharding.jdbc.ShardingConnection;
import com.gws.rdb.sharding.jdbc.ShardingContext;
import com.gws.rdb.sharding.jdbc.adapter.AbstractDataSourceAdapter;
import com.gws.rdb.sharding.metrics.MetricsContext;
import com.gws.rdb.sharding.metrics.ThreadLocalObjectContainer;
import com.gws.rdb.sharding.router.SQLRouteEngine;

/**
 * 支持分片的数据源.
 * 
 * @author wangdong
 */
public class ShardingDataSource extends AbstractDataSourceAdapter {
    
    private final ThreadLocalObjectContainer threadLocalObjectContainer = new ThreadLocalObjectContainer();
    
    private final ShardingContext context;
    
    public ShardingDataSource(final ShardingRule shardingRule) {
        this(shardingRule, new Properties());
    }
    
    public ShardingDataSource(final ShardingRule shardingRule, final Properties props) {
        Preconditions.checkNotNull(shardingRule);
        Preconditions.checkNotNull(props);
        ShardingConfiguration configuration = new ShardingConfiguration(props);
        initThreadLocalObjectContainer(configuration);
        DatabaseType type;
        try {
            type = DatabaseType.valueFrom(ShardingConnection.getDatabaseMetaDataFromDataSource(shardingRule.getDataSourceRule().getDataSources()).getDatabaseProductName());
        } catch (final SQLException ex) {
            throw new ShardingJdbcException("Can not get database product name", ex);
        }
        context = new ShardingContext(shardingRule, new SQLRouteEngine(shardingRule, type), new ExecutorEngine(configuration));
    }
    
    private void initThreadLocalObjectContainer(final ShardingConfiguration configuration) {
        if (configuration.getConfig(ShardingConfigurationConstant.METRICS_ENABLE, boolean.class)) {
            threadLocalObjectContainer.initItem(new MetricsContext(configuration.getConfig(ShardingConfigurationConstant.METRICS_SECOND_PERIOD, long.class),
                    configuration.getConfig(ShardingConfigurationConstant.METRICS_PACKAGE_NAME, String.class)));
        }
    }
    
    @Override
    public ShardingConnection getConnection() throws SQLException {
        threadLocalObjectContainer.build();
        return new ShardingConnection(context);
    }
    
    @Override
    public final Connection getConnection(final String username, final String password) throws SQLException {
        return getConnection();
    }
}
