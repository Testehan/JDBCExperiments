package com.testehan.database.postgresql.operations;

import com.testehan.database.postgresql.model.Movie;

import java.sql.*;
import java.util.List;

public class MovieSqlOperations extends SqlOperationsBase{

    public MovieSqlOperations() throws SQLException {
        super();
    }

    public int insertMovie(final Movie movie){
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

    public void insertMovies(final List<Movie> movies){
        final String insertMovieSql = "INSERT INTO movie(title, year, rating, director, description) VALUES(?,?,?,?,?)";
        int count = 0;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertMovieSql);)
        {
            for (Movie movie : movies) {
                preparedStatement.setString(1, movie.getTitle());
                preparedStatement.setInt(2, movie.getYear());
                preparedStatement.setFloat(3, movie.getRating());
                preparedStatement.setString(4, movie.getDirector());
                preparedStatement.setString(5, movie.getDescription());

                preparedStatement.addBatch();

                count++;
                // execute every 100 rows or when the list size is equal to count
                if (count % 100 == 0 || count == movies.size()) {
                    // Call the executeBatch() method to submit a batch of the INSERT statements to
                    // the database server for execution.
                    preparedStatement.executeBatch();
                }
            }

        } catch (SQLException exception){
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    public int selectMovieCount(){
        final String selectCountSql = "SELECT count(*) FROM movie";
        int count = 0;

        try (Connection connection = connectionPool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(selectCountSql);)
        {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        return count;
    }

    public void selectMovieTitleYear(){
        final String selectSql = "SELECT title, year FROM movie";

        try(Connection connection = connectionPool.getConnection();
            Statement selectStatement = connection.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(selectSql);)
        {

            while (resultSet.next()){
                System.out.println(resultSet.getString("title") + "    " + resultSet.getInt("year"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void selectMovieTitleWhereRatingBiggerThan(final float rating){
        final String selectSql = "SELECT title FROM movie where rating > ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSql);)
        {
            preparedStatement.setFloat(1,rating);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("title"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAllMovies(){
        final String deleteSql = "DELETE FROM movie";
        try (Connection connection = connectionPool.getConnection();
             Statement deleteStatement = connection.createStatement();)
        {
            deleteStatement.execute(deleteSql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteMoviesOlderThan(final int year){
        final String deleteSql = "DELETE FROM movie WHERE year < ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);)
        {
            deleteStatement.setInt(1,year);
            deleteStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

   public int updateMovieYearWhereTitle(final int year, final String title){
        final String updateSql = "UPDATE movie SET year = ? WHERE title = ?";
        int affectedRows = 0;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(updateSql);)
        {
            updateStatement.setInt(1,year);
            updateStatement.setString(2,title);

            affectedRows = updateStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return affectedRows;
   }

}
