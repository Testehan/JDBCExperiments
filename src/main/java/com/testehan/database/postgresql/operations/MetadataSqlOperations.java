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
}
