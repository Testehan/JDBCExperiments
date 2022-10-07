package com.testehan.database.postgresql.connectionpool;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BasicConnectionPool implements ConnectionPool {

    private static final int MAX_TIMEOUT = 5;

    private String url;
    private String user;
    private String password;
    private int initialPoolSize = 10;
    private List<Connection> connectionPool;
    private List<Connection> usedConnections = new ArrayList<>();

    private BasicConnectionPool() throws SQLException{

        readDBProperties();

        this.connectionPool = new ArrayList<>(this.initialPoolSize);
        for (int i = 0; i < this.initialPoolSize; i++) {
            connectionPool.add(createConnection(this.url, this.user, this.password));
        }
    }

    private void readDBProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("dbConfig.properties")) {

            Properties prop = new Properties();
            prop.load(input);

            this.url = prop.getProperty("db.url");
            this.user = prop.getProperty("db.user");
            this.password = prop.getProperty("db.password");
            this.initialPoolSize = new Integer(prop.getProperty("db.initialPoolSize"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static class LazyHolder {
        static final BasicConnectionPool INSTANCE;

        static {
            try {
                INSTANCE = new BasicConnectionPool();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static BasicConnectionPool getInstance() {
        return LazyHolder.INSTANCE;
    }


    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = connectionPool.remove(connectionPool.size() - 1);

        if(!connection.isValid(MAX_TIMEOUT)){
            connection = createConnection(url, user, password);
        }

        usedConnections.add(connection);
        return connection;
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }

    private static Connection createConnection(final String url, final String user, final String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void shutdown() throws SQLException {
        for (Connection c : connectionPool) {
            c.close();
        }
        for (Connection c: usedConnections){
            c.close();
        }
        connectionPool.clear();
        usedConnections.clear();
    }
}
