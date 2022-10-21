package com.testehan.database.postgresql.dao;

import com.testehan.database.postgresql.model.Movie;

import java.sql.SQLException;
import java.util.List;

public class MainDao {
    public static void main(String[] args) throws SQLException {
        Dao movieDao = new MovieDao();

        System.out.println("Get by id:");
        System.out.println(movieDao.get(1199).get());

        System.out.println("Inserting a new movie :");
        Movie newMovie = new Movie("The Rock",1996,7.4f,"Michael Bay","A mild-mannered chemist and an ex-con must lead the counterstrike when a rogue group of military men, led by a renegade general, threaten a nerve gas attack from Alcatraz against San Francisco");
        int id = movieDao.save(newMovie);
        newMovie.setMovieId(id);

        System.out.println("Get all :");
        List<Movie> movies = movieDao.getAll();
        for (Movie m: movies) {
            System.out.println(m);
        }

        System.out.println("Updating movie " + newMovie);
        newMovie.setDescription("");
        newMovie.setDirector("");
        newMovie.setTitle("IDK");
        movieDao.update(newMovie);

        System.out.println("Get all :");
        movies = movieDao.getAll();
        for (Movie m: movies) {
            System.out.println(m);
        }

        System.out.println("Deleting a movie with id " + id);
        movieDao.delete(newMovie);

        ((MovieDao)movieDao).cleanConnections();
    }
}
