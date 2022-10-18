package com.testehan.database.postgresql.operations;

import com.testehan.database.postgresql.model.Movie;

import javax.sql.RowSetEvent;
import javax.sql.RowSetListener;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.SQLException;

public class RowsetOperations extends SqlOperationsBase{

    public static final String URL_POSTGRESQL_DB = "jdbc:postgresql://localhost/danteshte";
    public static final String USER_POSTGRES_DB = "postgres";
    public static final String PASSWORD_POSTGRES_DB = "postgres";

    public RowsetOperations() throws SQLException {
        super();
    }

    public void insertMovieViaJDBCRowSet(final Movie movieToBeInserted) throws SQLException {
        final String selectSql = "SELECT * FROM movie";
        RowSetFactory factory = RowSetProvider.newFactory();

        try (JdbcRowSet jdbcRs = factory.createJdbcRowSet())
        {
            // not nice to hardcode here...but time constraints :)
            jdbcRs.setUrl(URL_POSTGRESQL_DB);
            jdbcRs.setUsername(USER_POSTGRES_DB);
            jdbcRs.setPassword(PASSWORD_POSTGRES_DB);
            jdbcRs.setCommand(selectSql);

            jdbcRs.execute();

            jdbcRs.moveToInsertRow();

            // starting from 2 because first column is the pk that is generated
            jdbcRs.updateString(2, movieToBeInserted.getTitle());
            jdbcRs.updateInt(3, movieToBeInserted.getYear());
            jdbcRs.updateFloat(4, movieToBeInserted.getRating());
            jdbcRs.updateString(5, movieToBeInserted.getDirector());
            jdbcRs.updateString(6, movieToBeInserted.getDescription());

            jdbcRs.insertRow();      // inserts the new row in the jdbcRs object but also in the DB

            jdbcRs.beforeFirst();    // better to move the cursor from the newly inserted row, because if the resultset is used again, errors might appear

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void updateAndInsertMovieViaCachedRowSet(final Movie movieToBeInserted) throws SQLException {

        try (Connection connection = connectionPool.getConnection()) {

            connection.setAutoCommit(false);
            RowSetFactory factory = RowSetProvider.newFactory();
            CachedRowSet crs = factory.createCachedRowSet();

            crs.setUsername(USER_POSTGRES_DB);
            crs.setPassword(PASSWORD_POSTGRES_DB);
            crs.setUrl(URL_POSTGRESQL_DB);
            crs.setCommand("SELECT * FROM movie");
            int[] keys = {1};
            crs.setKeyColumns(keys);

            crs.addRowSetListener(new DummyListener());

            // populates CachedRowSet with data based on the sql command from above
            crs.execute();

            // UPDATING something in the db
            while (crs.next()) {
                if (crs.getInt("year") == 1972) {
                    crs.updateFloat("rating", 3);
                    crs.updateRow();
                }
                System.out.println("Inside while !!!!!");
            }

            // Syncing the row back to the DB
            crs.acceptChanges(connection);

            crs.moveToInsertRow();

            // we need to provide an id in a better way; without this, the insert fails
            crs.updateInt(1, 9999999);
            crs.updateString(2, movieToBeInserted.getTitle());
            crs.updateInt(3, movieToBeInserted.getYear());
            crs.updateFloat(4, movieToBeInserted.getRating());
            crs.updateString(5, movieToBeInserted.getDirector());
            crs.updateString(6, movieToBeInserted.getDescription());

            crs.insertRow();
            crs.moveToCurrentRow();

            // Syncing the row back to the DB
            crs.acceptChanges(connection);

            crs.close();
            connection.setAutoCommit(true);
        }
    }

    private class DummyListener implements RowSetListener {

        @Override
        public void rowSetChanged(RowSetEvent event) {
            System.out.println("Rowset changed");
        }

        @Override
        public void rowChanged(RowSetEvent event) {
            System.out.println("Row changed");
        }

        @Override
        public void cursorMoved(RowSetEvent event) {
            System.out.println("Cursor moved");
        }
    }
}
