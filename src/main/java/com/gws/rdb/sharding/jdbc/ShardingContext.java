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

package com.gws.rdb.sharding.jdbc;

import com.gws.rdb.sharding.api.rule.ShardingRule;
import com.gws.rdb.sharding.executor.ExecutorEngine;
import com.gws.rdb.sharding.router.SQLRouteEngine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * ShardingDataSource运行期上下文.
 * 
 * @author wangdong
 */
@RequiredArgsConstructor
@Getter
public final class ShardingContext {
    
    private final ShardingRule shardingRule;
    
    private final SQLRouteEngine sqlRouteEngine;
    
    private final ExecutorEngine executorEngine;
}
