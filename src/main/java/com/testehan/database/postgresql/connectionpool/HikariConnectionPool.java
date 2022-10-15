package com.testehan.database.postgresql.connectionpool;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class HikariConnectionPool implements ConnectionPool{

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    private HikariConnectionPool(){
        System.out.println("Using HikariConnectionPool to obtain connections");
        String url = null;
        String user = null;
        String password = null;

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("dbConfig.properties"))
        {
            Properties prop = new Properties();
            prop.load(input);
            url = prop.getProperty("db.url");
            user = prop.getProperty("db.user");
            password = prop.getProperty("db.password");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // would be nice to extract these from properties file like in the case of ConnectionPoolBase
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        ds = new HikariDataSource( config );
    };

    private static class LazyHolder {
        static final HikariConnectionPool INSTANCE;

        static {
            INSTANCE = new HikariConnectionPool();
        }
    }

    public static HikariConnectionPool getInstance() {
        return HikariConnectionPool.LazyHolder.INSTANCE;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public void shutdown() throws SQLException {
        ds.close();
    }
}
