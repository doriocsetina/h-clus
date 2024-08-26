package database;

import java.util.LinkedList;
import java.util.List;
import data.Example;
import database.exceptions.DatabaseConnectionException;
import database.exceptions.EmptySetException;
import database.exceptions.MissingNumberException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableData {
    private DbAccess db;

    public TableData(DbAccess db) {
        this.db = db;
    }

    public List<Example> getDistinctTransactions(String table)
            throws SQLException, EmptySetException, MissingNumberException {
        /* crea la query */
        String queryString = "SELECT * FROM " + table;

        List<Example> examples = new LinkedList<Example>();

        try (PreparedStatement preparedStatement = db.getConnection().prepareStatement(queryString)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int i = 1;

                Example example = new Example();
                example.add(resultSet.getDouble(i++));
                example.add(resultSet.getDouble(i++));
                example.add(resultSet.getDouble(i++));
                examples.add(example);
            }

        } catch (DatabaseConnectionException e) {
            e.printStackTrace();
        }
        if (examples.isEmpty())
            throw new EmptySetException();
        return examples;
    }
}
