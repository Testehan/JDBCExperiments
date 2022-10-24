package com.testehan.database.postgresql.operations;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TableSqlOperations extends SqlOperationsBase{
    public TableSqlOperations() throws SQLException {
    }

    public void createTable(){
        final String createMovieSeqSql = "CREATE SEQUENCE IF NOT EXISTS movie_movie_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807";
        final String createMovieTableSql = "CREATE TABLE IF NOT EXISTS movie(" +
                "movie_id bigint primary key DEFAULT nextval('movie_movie_id_seq') , " +
                "title varchar(100), " +
                "year integer, " +
                "rating real, " +
                "director varchar(100), " +
                "description varchar(1000))";

        final String createActorSeqSql = "CREATE SEQUENCE IF NOT EXISTS actor_actor_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807";
        final String createActorTableSql = "CREATE TABLE IF NOT EXISTS actor(" +
                "actor_id bigint primary key DEFAULT nextval('actor_actor_id_seq') , " +
                "first_name varchar(100), " +
                "last_name varchar(100))";

        final String createMovieActorTableSql = "CREATE TABLE IF NOT EXISTS movie_actor(" +
                "movie_id bigint NOT NULL DEFAULT nextval('movie_movie_id_seq'), " +
                "actor_id bigint NOT NULL DEFAULT nextval('actor_actor_id_seq'), " +
                "PRIMARY KEY (movie_id, actor_id), " +
                "foreign key(movie_id) references movie, " +
                "foreign key(actor_id) references actor)";

        try (Connection connection = connectionPool.getConnection();
             Statement createSequenceStatement = connection.createStatement();
             Statement createTableStatement = connection.createStatement();
             Statement createActorSeqStatement = connection.createStatement();
             Statement createActorTableStatement = connection.createStatement();
             Statement createMovieActorStatement = connection.createStatement();)
        {
            connection.setAutoCommit(false);

            createSequenceStatement.execute(createMovieSeqSql);
            createTableStatement.execute(createMovieTableSql);
            createActorSeqStatement.execute(createActorSeqSql);
            createActorTableStatement.execute(createActorTableSql);
            createMovieActorStatement.execute(createMovieActorTableSql);

            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
