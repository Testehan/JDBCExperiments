package com.testehan.database.postgresql.operations;

import java.sql.*;

public class MovieStoredFunctions extends SqlOperationsBase{

    public MovieStoredFunctions() throws SQLException {
        super();
    }

    public String getProperCase(final String text){
        String result = text;

        try (Connection connection = connectionPool.getConnection();
             CallableStatement callableStatement = connection.prepareCall("{ ? = call initcap( ? ) }");)
        {
            callableStatement.registerOutParameter(1, Types.VARCHAR);
            callableStatement.setString(2,text);
            callableStatement.execute();

            result = callableStatement.getString(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public void selectMoviesWithTitleLikePattern(final String pattern){
        final String selectSql = "SELECT * FROM get_movie_with_name_like_pattern(?)";

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql))
        {
            preparedStatement.setString(1,pattern);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                System.out.println(rs.getString("movie_title") + "  " + rs.getInt("movie_year"));
            }

            rs.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
