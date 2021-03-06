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

package com.gws.rdb.sharding.jdbc.adapter;

import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Collection;

import com.gws.rdb.sharding.jdbc.unsupported.AbstractUnsupportedOperationStatement;

import lombok.RequiredArgsConstructor;

/**
 * 静态语句对象适配类.
 * 
 * @author wangdong
 */
@RequiredArgsConstructor
public abstract class AbstractStatementAdapter extends AbstractUnsupportedOperationStatement {
    
    private final Class<? extends Statement> recordTargetClass;
    
    private boolean closed;
    
    private boolean poolable;
    
    private int fetchSize;
    
    @Override
    public final void close() throws SQLException {
        for (Statement each : getRoutedStatements()) {
            each.close();
        }
        closed = true;
        getRoutedStatements().clear();
    }
    
    @Override
    public final boolean isClosed() throws SQLException {
        return closed;
    }
    
    @Override
    public final boolean isPoolable() throws SQLException {
        return poolable;
    }
    
    @Override
    public final void setPoolable(final boolean poolable) throws SQLException {
        this.poolable = poolable;
        if (getRoutedStatements().isEmpty()) {
            recordMethodInvocation(recordTargetClass, "setPoolable", new Class[] {boolean.class}, new Object[] {poolable});
            return;
        }
        for (Statement each : getRoutedStatements()) {
            each.setPoolable(poolable);
        }
    }
    
    @Override
    public final int getFetchSize() throws SQLException {
        return fetchSize;
    }
    
    @Override
    public final void setFetchSize(final int rows) throws SQLException {
        this.fetchSize = rows;
        if (getRoutedStatements().isEmpty()) {
            recordMethodInvocation(recordTargetClass, "setFetchSize", new Class[] {int.class}, new Object[] {rows});
            return;
        }
        for (Statement each : getRoutedStatements()) {
            each.setFetchSize(rows);
        }
    }
    
    @Override
    public final void setEscapeProcessing(final boolean enable) throws SQLException {
        if (getRoutedStatements().isEmpty()) {
            recordMethodInvocation(recordTargetClass, "setEscapeProcessing", new Class[] {boolean.class}, new Object[] {enable});
            return;
        }
        for (Statement each : getRoutedStatements()) {
            each.setEscapeProcessing(enable);
        }
    }
    
    @Override
    public final void cancel() throws SQLException {
        for (Statement each : getRoutedStatements()) {
            each.cancel();
        }
    }
    
    @Override
    public final void setCursorName(final String name) throws SQLException {
        if (getRoutedStatements().isEmpty()) {
            recordMethodInvocation(recordTargetClass, "setCursorName", new Class[] {String.class}, new Object[] {name});
            return;
        }
        for (Statement each : getRoutedStatements()) {
            each.setCursorName(name);
        }
    }
    
    @Override
    public final int getUpdateCount() throws SQLException {
        int result = 0;
        for (Statement each : getRoutedStatements()) {
            result += each.getUpdateCount();
        }
        return result;
    }
    
    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }
    
    @Override
    public void clearWarnings() throws SQLException {
    }
    
    /* 
     * 只有存储过程会出现多结果集, 因此不支持.
     */
    @Override
    public final boolean getMoreResults() throws SQLException {
        return false;
    }
    
    @Override
    public final boolean getMoreResults(final int current) throws SQLException {
        return false;
    }
    
    
    
    /**
     * 获取路由的静态语句对象集合.
     * 
     * @return 路由的静态语句对象集合
     * @throws SQLException
     */
    public abstract Collection<? extends Statement> getRoutedStatements() throws SQLException;
}
