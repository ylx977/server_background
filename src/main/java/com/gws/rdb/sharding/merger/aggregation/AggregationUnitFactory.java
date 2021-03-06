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

package com.gws.rdb.sharding.merger.aggregation;

import com.gws.rdb.sharding.parser.result.merger.AggregationColumn.AggregationType;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 聚合函数结果集归并单元工厂.
 * 
 * @author wangdong
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class AggregationUnitFactory {
    
    public static AggregationUnit create(final AggregationType type, final Class<?> returnType) {
        switch (type) {
            case MAX:
                return new ComparableAggregationUnit(false, returnType);
            case MIN:
                return new ComparableAggregationUnit(true, returnType);
            case SUM:
            case COUNT:
                return new AccumulationAggregationUnit(returnType);
            case AVG:
                return new AvgAggregationUnit(returnType);
            default:
                throw new UnsupportedOperationException(type.name());
        }
    }
}
