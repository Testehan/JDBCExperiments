package com.testehan.database.postgresql.operations;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MetadataSqlOperations extends SqlOperationsBase{

    public MetadataSqlOperations() throws SQLException {
    }

    public void printDatabaseTables(){

        try (Connection connection = connectionPool.getConnection())
        {
            DatabaseMetaData dbmd = connection.getMetaData();
            try (ResultSet tablesResultSet = dbmd.getTables(null, "public", "%", null);){
                while (tablesResultSet.next()) {
                    System.out.println((tablesResultSet.getString("TABLE_NAME")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void printTransactionIsolationLevel() {
        try (Connection connection = connectionPool.getConnection()) {
            DatabaseMetaData dbmd = connection.getMetaData();

            System.out.println("Current transaction isolation level is " + connection.getTransactionIsolation());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void printSupportedResultSetTypes(){
        try (Connection connection = connectionPool.getConnection()) {
            DatabaseMetaData dbmd = connection.getMetaData();

            System.out.println("Database name and version");
            System.out.println(dbmd.getDatabaseProductName()+ " -- " + dbmd.getDatabaseProductVersion());
            System.out.println("Driver name and version");
            System.out.println(dbmd.getDriverName() + " --- " + dbmd.getDriverMajorVersion());

            boolean isForwardOnlySupported = dbmd.supportsResultSetType(ResultSet.TYPE_FORWARD_ONLY);
            if(isForwardOnlySupported) {
                System.out.println("Underlying database supports TYPE_FORWARD_ONLY ResultSet type");
            } else {
                System.out.println("Underlying database does not supports TYPE_FORWARD_ONLY ResultSet type");
            }

            boolean isScrollInsensitiveSupported = dbmd.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
            if(isScrollInsensitiveSupported) {
                System.out.println("Underlying database supports TYPE_SCROLL_INSENSITIVE ResultSet type");
            } else {
                System.out.println("Underlying database does not supports TYPE_SCROLL_INSENSITIVE ResultSet type");
            }

            boolean isScrollSensitiveSupported = dbmd.supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE);
            if(isScrollSensitiveSupported) {
                System.out.println("Underlying database supports TYPE_SCROLL_INSENSITIVE ResultSet type");
            } else {
                System.out.println("Underlying database does not supports TYPE_SCROLL_INSENSITIVE ResultSet type");
            }

            boolean isConcurrentReadOnlySupported = dbmd.supportsResultSetType(ResultSet.CONCUR_READ_ONLY);
            if(isConcurrentReadOnlySupported) {
                System.out.println("Underlying database supports CONCUR_READ_ONLY ResultSet type");
            } else {
                System.out.println("Underlying database does not supports CONCUR_READ_ONLY ResultSet type");
            }

            boolean isConcurrentUpdatableSupported = dbmd.supportsResultSetType(ResultSet.CONCUR_UPDATABLE);
            if(isConcurrentUpdatableSupported) {
                System.out.println("Underlying database supports CONCUR_UPDATABLE ResultSet type");
            } else {
                System.out.println("Underlying database does not supports CONCUR_UPDATABLE ResultSet type");
            }

            boolean isHoldCursorsOverCommitSupported = dbmd.supportsResultSetType(ResultSet.HOLD_CURSORS_OVER_COMMIT);
            if(isHoldCursorsOverCommitSupported) {
                System.out.println("Underlying database supports HOLD_CURSORS_OVER_COMMIT ResultSet type");
            } else {
                System.out.println("Underlying database does not supports HOLD_CURSORS_OVER_COMMIT ResultSet type");
            }

            boolean isCloseCursorsAtCommitSupported = dbmd.supportsResultSetType(ResultSet.CLOSE_CURSORS_AT_COMMIT);
            if(isCloseCursorsAtCommitSupported) {
                System.out.println("Underlying database supports CLOSE_CURSORS_AT_COMMIT ResultSet type");
            } else {
                System.out.println("Underlying database does not supports CLOSE_CURSORS_AT_COMMIT ResultSet type");
            }

        }  catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
