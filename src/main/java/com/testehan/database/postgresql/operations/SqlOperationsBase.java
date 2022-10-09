package com.testehan.database.postgresql.operations;

import com.testehan.database.postgresql.connectionpool.BasicConnectionPool;
import com.testehan.database.postgresql.connectionpool.ConnectionPool;

import java.sql.SQLException;

public abstract class SqlOperationsBase {

    protected final ConnectionPool connectionPool;

    public SqlOperationsBase() throws SQLException {
        connectionPool = BasicConnectionPool.getInstance();
    }

    public void cleanConnections() throws SQLException {
        connectionPool.shutdown();
    }
}
