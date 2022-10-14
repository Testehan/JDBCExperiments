package com.testehan.database.postgresql.connectionpool;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceConnectionPool extends ConnectionPoolBase{

    private static DataSource ds;

    private DataSourceConnectionPool() throws SQLException {
        System.out.println("Using DataSource to obtain connections");

        ds = new PGSimpleDataSource() ;
        ((PGSimpleDataSource) ds).setUrl(super.url);
        ((PGSimpleDataSource) ds).setUser(super.user);
        ((PGSimpleDataSource) ds).setPassword(super.password);

        initConnectionPool();
    }

    @Override
    protected Connection createConnection(String url, String user, String password) throws SQLException {
        return ds.getConnection();
    }

    private static class LazyHolder {
        static final DataSourceConnectionPool INSTANCE;

        static {
            try {
                INSTANCE = new DataSourceConnectionPool();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static DataSourceConnectionPool getInstance() {
        return DataSourceConnectionPool.LazyHolder.INSTANCE;
    }
}
