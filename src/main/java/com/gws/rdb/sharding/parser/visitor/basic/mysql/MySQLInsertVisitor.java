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

package com.gws.rdb.sharding.parser.visitor.basic.mysql;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.google.common.base.Optional;
import com.gws.rdb.sharding.parser.result.router.Condition.BinaryOperator;

/**
 * MySQL的INSERT语句访问器.
 * 
 * @author wangdong, wangdong
 */
public class MySQLInsertVisitor extends AbstractMySQLVisitor {
    
    @Override
    public boolean visit(final MySqlInsertStatement x) {
        getParseContext().setCurrentTable(x.getTableName().toString(), Optional.fromNullable(x.getAlias()));
        for (int i = 0; i < x.getColumns().size(); i++) {
            getParseContext().addCondition(x.getColumns().get(i).toString(), x.getTableName().toString(), BinaryOperator.EQUAL, x.getValues().getValues().get(i), getDatabaseType(), getParameters());
        }
        return super.visit(x);
    }
}
