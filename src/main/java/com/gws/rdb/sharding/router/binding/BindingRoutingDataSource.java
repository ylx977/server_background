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

package com.gws.rdb.sharding.router.binding;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.gws.rdb.sharding.api.rule.BindingTableRule;
import com.gws.rdb.sharding.router.single.SingleRoutingDataSource;
import com.gws.rdb.sharding.router.single.SingleRoutingTableFactor;

import lombok.Getter;
import lombok.ToString;

/**
 * Binding表路由数据源.
 * 
 * @author wangdong
 */
@Getter
@ToString(callSuper = true)
final class BindingRoutingDataSource extends SingleRoutingDataSource {
    
    BindingRoutingDataSource(final SingleRoutingDataSource routingDataSource) {
        super(routingDataSource.getDataSource());
        getRoutingTableFactors().addAll(Lists.transform(routingDataSource.getRoutingTableFactors(), new Function<SingleRoutingTableFactor, BindingRoutingTableFactor>() {
            
            @Override
            public BindingRoutingTableFactor apply(final SingleRoutingTableFactor input) {
                return new BindingRoutingTableFactor(input.getLogicTable(), input.getActualTable());
            }
        }));
    }
    
    void bind(final BindingTableRule bindingTableRule, final String bindingLogicTable) {
        for (SingleRoutingTableFactor each : getRoutingTableFactors()) {
            ((BindingRoutingTableFactor) each).getBindingRoutingTableFactors().add(
                    new BindingRoutingTableFactor(bindingLogicTable, bindingTableRule.getBindingActualTable(getDataSource(), bindingLogicTable, each.getActualTable())));
        }
    }
}
