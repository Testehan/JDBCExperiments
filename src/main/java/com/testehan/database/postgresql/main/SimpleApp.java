package com.testehan.database.postgresql.main;


import com.testehan.database.postgresql.model.Actor;
import com.testehan.database.postgresql.model.Movie;
import com.testehan.database.postgresql.operations.MetadataSqlOperations;
import com.testehan.database.postgresql.operations.MovieSqlOperations;
import com.testehan.database.postgresql.operations.MovieStoredFunctions;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SimpleApp {

    public static void main(String... param) throws SQLException {
        System.out.println("Starting application...");

        MovieSqlOperations movieSqlOperations = new MovieSqlOperations();
//        movieSqlOperations.deleteAllMoviesAndRelatedActors();

        MetadataSqlOperations metadataSqlOperations = new MetadataSqlOperations();
        metadataSqlOperations.printDatabaseTables();


        SecureRandom random = new SecureRandom();
        int maxYear=2022;
        int minYear=1900;
        int year = random.nextInt(maxYear-minYear+1)+minYear;
        movieSqlOperations.insertMovie(new Movie("Lord of the rings " + random.nextInt(), year, 9.1f, "Peter Jackson","a movie about a ring and hobbits"));

        List<Movie> movies = new ArrayList();
        for (int i=0; i<50; i++){
            year = random.nextInt(maxYear-minYear+1)+minYear;
            movies.add(new Movie("Harry Potter and the Goblet of Fire " + random.nextInt(), year, 7.7f, "Mike Newell","a movie about wizards"));
        }
        movieSqlOperations.insertMovies(movies);

        System.out.println("Movie titles with a rating bigger than 8.5:");
        movieSqlOperations.selectMovieTitleWhereRatingBiggerThan(8.5f);

        System.out.println("Number of movies in the DB is " + movieSqlOperations.selectMovieCount());

//        year = random.nextInt(maxYear-minYear+1)+minYear;
//        System.out.println("Deleting all movies from the DB that are smaller than " + year);
//        int affectedRows = movieSqlOperations.deleteMoviesOlderThan(year);
//        System.out.println("Deleted " + affectedRows + " rows from the DB.");

//        int affectedRows = movieSqlOperations.updateMovieYearWhereTitle(2003,"Lord of the rings 1575701633");
//        System.out.println("Number of movies that were updated " + affectedRows);

        movieSqlOperations.insertMovieAndActor(
                new Movie("Harry Potter and the Goblet of Fire " + random.nextInt(), year, 7.7f, "Mike Newell","a movie about wizards"),
                new Actor("Daniel","Radcliffe")
        );

        MovieStoredFunctions movieStoredFunctions = new MovieStoredFunctions();
        System.out.println(movieStoredFunctions.getProperCase("this string will have uppercase for each word"));

        movieStoredFunctions.selectMoviesWithTitleLikePattern("Ha%");

        movieSqlOperations.cleanConnections();
        System.out.println("Closing application...");
    }
}