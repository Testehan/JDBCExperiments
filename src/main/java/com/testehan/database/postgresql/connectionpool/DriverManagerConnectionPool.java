package com.testehan.database.postgresql.connectionpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DriverManagerConnectionPool extends ConnectionPoolBase {
    private DriverManagerConnectionPool() throws SQLException {
        System.out.println("Using DriverManager to obtain connections");
        initConnectionPool();
    }

    @Override
    protected Connection createConnection(String url, String user, String password) throws SQLException {
         return DriverManager.getConnection(url, user, password);
    }
    private static class LazyHolder {
        static final DriverManagerConnectionPool INSTANCE;

        static {
            try {
                INSTANCE = new DriverManagerConnectionPool();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static DriverManagerConnectionPool getInstance() {
        return LazyHolder.INSTANCE;
    }
}
