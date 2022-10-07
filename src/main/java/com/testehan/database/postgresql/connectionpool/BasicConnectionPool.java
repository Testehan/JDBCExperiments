package com.testehan.database.postgresql.connectionpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BasicConnectionPool implements ConnectionPool {

    private static final int MAX_TIMEOUT = 5;
    private static int INITIAL_POOL_SIZE = 10;

    private String url;
    private String user;
    private String password;
    private List<Connection> connectionPool;
    private List<Connection> usedConnections = new ArrayList<>();

    private BasicConnectionPool(String url, String user, String password, List<Connection> connectionPool) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.connectionPool = connectionPool;

//        try (InputStream input = new FileInputStream("path/to/config.properties")) {
//
//            Properties prop = new Properties();
//
//            // load a properties file
//            prop.load(input);
//
//            // get the property value and print it out
//            System.out.println(prop.getProperty("db.url"));
//            System.out.println(prop.getProperty("db.user"));
//            System.out.println(prop.getProperty("db.password"));
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }

//    private static class LazyHolder {
//        static final BasicConnectionPool INSTANCE = new BasicConnectionPool();
//    }


    public static BasicConnectionPool create(final String url, final String user, final String password) throws SQLException {

        List<Connection> connectionPool = new ArrayList<>(INITIAL_POOL_SIZE);
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            connectionPool.add(createConnection(url, user, password));
        }
        return new BasicConnectionPool(url, user, password, connectionPool);
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
