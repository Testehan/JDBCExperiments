package com.testehan.database.postgresql.operations;

import com.testehan.database.postgresql.model.Movie;

import java.sql.*;

public class MovieSqlOperations extends SqlOperationsBase{

    public MovieSqlOperations() throws SQLException {
        super();
    }

    public int insertMovie(final Movie movie){
        final String insertMovieSql = "INSERT INTO movie(title, year, rating, director, description) VALUES(?,?,?,?,?)";
        int id = 0;

        try (PreparedStatement preparedStatement = connectionPool.getConnection().prepareStatement(insertMovieSql,Statement.RETURN_GENERATED_KEYS))
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

    public int getMovieCount(){
        final String selectCountSql = "SELECT count(*) FROM movie";
        int count = 0;

        try (Statement statement = connectionPool.getConnection().createStatement();
             ResultSet rs = statement.executeQuery(selectCountSql))
        {
            rs.next();
            count = rs.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        return count;
    }

    public void deleteAllMovies(){
        final String deleteSql = "DELETE FROM movie";
        try (Statement deleteStatement = connectionPool.getConnection().createStatement();)
        {
            deleteStatement.execute(deleteSql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteMoviesOlderThan(final int year){
        final String deleteSql = "DELETE FROM movie WHERE year < ?";
        try (PreparedStatement deleteStatement = connectionPool.getConnection().prepareStatement(deleteSql))
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
        try (PreparedStatement updateStatement = connectionPool.getConnection().prepareStatement(updateSql)){
            updateStatement.setInt(1,year);
            updateStatement.setString(2,title);

            affectedRows = updateStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return affectedRows;
   }

}
