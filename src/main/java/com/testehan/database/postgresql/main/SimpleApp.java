package com.testehan.database.postgresql.main;


import com.testehan.database.postgresql.model.Movie;
import com.testehan.database.postgresql.operations.MovieSqlOperations;

import java.security.SecureRandom;
import java.sql.SQLException;

public class SimpleApp {

    public static void main(String... param) throws SQLException {
        System.out.println("Starting application...");

        MovieSqlOperations movieSqlOperations = new MovieSqlOperations();
        SecureRandom random = new SecureRandom();
        movieSqlOperations.persistMovie(new Movie("Lord of the rings " + random.nextInt(), 2003, 9.1f, "Peter Jackson","a movie about a ring and hobbits"));

        System.out.println("Number of movies in the DB is " + movieSqlOperations.getMovieCount());

        movieSqlOperations.cleanConnections();
        System.out.println("Closing application...");


    }
}