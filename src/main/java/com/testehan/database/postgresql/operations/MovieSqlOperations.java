package com.testehan.database.postgresql.operations;

import com.testehan.database.postgresql.model.Movie;

import java.sql.*;

public class MovieSqlOperations extends SqlOperationsBase{

    public MovieSqlOperations() throws SQLException {
        super();
    }

    public void persistMovie(Movie movie){
        final String insertMovieSql = "INSERT INTO movie(title, year, rating, director, description) VALUES(?,?,?,?,?)";

        try (PreparedStatement preparedStatement = connectionPool.getConnection().prepareStatement(insertMovieSql))
        {
            preparedStatement.setString(1,movie.getTitle());
            preparedStatement.setInt(2, movie.getYear());
            preparedStatement.setFloat(3,movie.getRating());
            preparedStatement.setString(4,movie.getDirector());
            preparedStatement.setString(5,movie.getDescription());
            preparedStatement.execute();

            System.out.println("Movie with title "+ movie.getTitle() + " was inserted in the Database");

        } catch (SQLException exception){
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception);

        }
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

}
