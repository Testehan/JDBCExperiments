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
        int maxYear=2022;
        int minYear=1900;
        int year = random.nextInt(maxYear-minYear+1)+minYear;
        int id = movieSqlOperations.insertMovie(new Movie("Lord of the rings " + random.nextInt(), year, 9.1f, "Peter Jackson","a movie about a ring and hobbits"));

        System.out.println("Number of movies in the DB is " + movieSqlOperations.getMovieCount());
        System.out.println("Id of the newly insterted movie is = " + id);

//        year = random.nextInt(maxYear-minYear+1)+minYear;
//        System.out.println("Deleting all movies from the DB that are smaller than " + year);
//        movieSqlOperations.deleteMoviesOlderThan(year);

        int affectedRows = movieSqlOperations.updateMovieYearWhereTitle(2003,"Lord of the rings 1575701633");
        System.out.println("Number of movies that were updated " + affectedRows);

        movieSqlOperations.cleanConnections();
        System.out.println("Closing application...");
    }
}