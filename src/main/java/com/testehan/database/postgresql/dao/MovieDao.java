package com.testehan.database.postgresql.dao;

import com.testehan.database.postgresql.model.Movie;
import com.testehan.database.postgresql.operations.SqlOperationsBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieDao extends SqlOperationsBase implements Dao<Movie>{

    public MovieDao() throws SQLException {
    }

    @Override
    public Optional<Movie> get(int id) {
        final String selectSql = "SELECT * FROM movie where movie_id = ?";
        Movie returnedMovie = null;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSql);)
        {
            preparedStatement.setInt(1,id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    returnedMovie = new Movie();
                    returnedMovie.setMovieId(id);
                    returnedMovie.setTitle(resultSet.getString("title"));
                    returnedMovie.setRating(resultSet.getFloat("rating"));
                    returnedMovie.setDirector(resultSet.getString("director"));
                    returnedMovie.setDescription(resultSet.getString("description"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(returnedMovie);
    }

    @Override
    public List<Movie> getAll() {
        final String selectSql = "SELECT * FROM movie";
        List<Movie> returnedMovies = new ArrayList();

        try (Connection connection = connectionPool.getConnection();
             Statement statement = connection.createStatement();)
        {
            try (ResultSet resultSet = statement.executeQuery(selectSql)) {
                while (resultSet.next()) {
                    Movie m = new Movie();
                    m.setMovieId(resultSet.getInt("movie_id"));
                    m.setTitle(resultSet.getString("title"));
                    m.setRating(resultSet.getFloat("rating"));
                    m.setDirector(resultSet.getString("director"));
                    m.setDescription(resultSet.getString("description"));

                    returnedMovies.add(m);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return returnedMovies;
    }

    @Override
    public int save(Movie movie) {
        final String insertMovieSql = "INSERT INTO movie(title, year, rating, director, description) VALUES(?,?,?,?,?)";
        int id = 0;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertMovieSql,Statement.RETURN_GENERATED_KEYS);)
        {
            preparedStatement.setString(1,movie.getTitle());
            preparedStatement.setInt(2, movie.getYear());
            preparedStatement.setFloat(3,movie.getRating());
            preparedStatement.setString(4,movie.getDirector());
            preparedStatement.setString(5,movie.getDescription());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0){
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()){
                    if (resultSet.next()){
                        id = resultSet.getInt(1);
                        movie.setMovieId(id);
                    }
                }
            }

            System.out.println("Movie was inserted in DB " + movie);

        } catch (SQLException exception){
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception);
        }

        return id;
    }

    @Override
    public void update(Movie movie) {
        final String updateSql = "UPDATE movie SET title = ?, year = ?, rating = ?, director = ?, description = ? WHERE movie_id = ?";
        int affectedRows = 0;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(updateSql);)
        {
            updateStatement.setString(1,movie.getTitle());
            updateStatement.setInt(2,movie.getYear());
            updateStatement.setFloat(3,movie.getRating());
            updateStatement.setString(4,movie.getDirector());
            updateStatement.setString(5,movie.getDescription());
            updateStatement.setInt(6,movie.getMovieId());

            affectedRows = updateStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (affectedRows == 1){
            System.out.println("Movie " + movie.getTitle() + " was updated");
        }
        if (affectedRows == 0){
            System.out.println("Movie " + movie.getTitle() + " was not updated");
        }
    }

    @Override
    public void delete(Movie movie) {
        final String deleteSql = "DELETE FROM movie WHERE movie_id = ?";
        int affectedRows = 0;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);)
        {
            deleteStatement.setInt(1,movie.getMovieId());
            affectedRows = deleteStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (affectedRows == 1){
            System.out.println("Movie " + movie.getTitle() + " was deleted");
        }
        if (affectedRows == 0){
            System.out.println("Movie " + movie.getTitle() + " was not deleted");
        }
    }
}
