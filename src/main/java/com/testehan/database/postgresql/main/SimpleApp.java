package com.testehan.database.postgresql.main;


import com.testehan.database.postgresql.connectionpool.BasicConnectionPool;
import com.testehan.database.postgresql.connectionpool.ConnectionPool;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SimpleApp {
    private final ConnectionPool connectionPool;

    public SimpleApp() throws SQLException {
        connectionPool = BasicConnectionPool.getInstance();
    }

    private SimpleApp close(final AutoCloseable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return this;
    }

    public void addMovieToDatabase(Movie movie){
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        // insert an actor into the actor table
        final String insertMovieSql = "INSERT INTO movie(title) VALUES(?)";

        try {
            // connect to the database
            conn = connectionPool.getConnection();
            conn.setAutoCommit(false);

            preparedStatement = conn.prepareStatement(insertMovieSql);
            preparedStatement.setString(1,movie.getTitle());
            preparedStatement.execute();

            conn.commit();
            System.out.println("Movie with title "+ movie.getTitle() + " was inserted in the Database");


        } catch (SQLException exception){
            System.out.println(exception.getMessage());
            System.out.println("Rolling back the transaction...");
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } finally {
            this.close(preparedStatement);
        }
    }

    public void cleanConnections() throws SQLException {
        connectionPool.shutdown();
    }

    public static void main(String... param) throws SQLException {
        System.out.println("Starting application...");

        SimpleApp app = new SimpleApp();
        SecureRandom random = new SecureRandom();
        app.addMovieToDatabase(new Movie("Lord of the rings " + random.nextInt()));

        app.cleanConnections();
        System.out.println("Closing application...");


    }
}