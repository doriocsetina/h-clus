package database;

import java.util.LinkedList;
import java.util.List;
import data.Example;
import database.exceptions.DatabaseConnectionException;
import database.exceptions.EmptySetException;
import database.exceptions.MissingNumberException;

import java.sql.SQLException;

public class TableData {
    private DbAccess db;

    TableData(DbAccess db) {
        try {
            db.initConnection();
        } catch (DatabaseConnectionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    List<Example> getDistinctTransactions(String table) throws SQLException, EmptySetException, MissingNumberException {
        return new LinkedList<>();
    }
}
