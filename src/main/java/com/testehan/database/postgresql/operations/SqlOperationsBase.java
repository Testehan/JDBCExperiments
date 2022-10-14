package com.testehan.database.postgresql.operations;

import com.testehan.database.postgresql.connectionpool.ConnectionPool;
import com.testehan.database.postgresql.connectionpool.DataSourceConnectionPool;

import java.sql.SQLException;

public abstract class SqlOperationsBase {

    protected final ConnectionPool connectionPool;

    public SqlOperationsBase() throws SQLException {
        connectionPool = DataSourceConnectionPool.getInstance();
    }

    public void cleanConnections() throws SQLException {
        connectionPool.shutdown();
    }
}
