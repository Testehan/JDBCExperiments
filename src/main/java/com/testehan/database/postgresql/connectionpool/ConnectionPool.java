package com.testehan.database.postgresql.connectionpool;

import java.sql.Connection;
import java.sql.SQLException;

// Usually you would use a lib...this is for practice purposes
public interface ConnectionPool{
    Connection getConnection() throws SQLException;
    boolean releaseConnection(Connection connection);

    void shutdown() throws SQLException;
}
