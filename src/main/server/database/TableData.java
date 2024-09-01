package server.database;

import java.util.LinkedList;
import java.util.List;

import server.data.Example;
import server.database.exceptions.DatabaseConnectionException;
import server.database.exceptions.EmptySetException;
import server.database.exceptions.MissingNumberException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Questa classe è responsabile per gestire le operazioni relative a una tabella
 * del database.
 * Fornisce un metodo per ottenere transazioni distinte da una tabella
 * specificata.
 * 
 * Ogni istanza di questa classe è associata a un oggetto {@link DbAccess},
 * che rappresenta la connessione al database.
 *
 */
public class TableData {
    private DbAccess db;

    /**
     * Costruttore della classe TableData.
     * 
     * Instanzia un nuovo oggetto TableData associato a un oggetto DbAccess
     * specificato,
     * che rappresenta la connessione al database.
     * 
     * @param db l'oggetto DbAccess che rappresenta la connessione al database.
     */
    public TableData(DbAccess db) {
        this.db = db;
    }

    public List<String> getTables() throws SQLException {
        String queryString = "SHOW TABLES";

        List<String> tableStrings = new LinkedList<>();

        try (PreparedStatement preparedStatement = db.getConnection().prepareStatement(queryString)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int i = 1;
                tableStrings.add(resultSet.getString(i++));
            }

        } catch (DatabaseConnectionException e) {
            e.printStackTrace();
        }
        return tableStrings;
    }

    /**
     * Ottiene le transazioni distinte da una tabella specificata.
     * 
     * Questo metodo esegue una query SQL sulla tabella specificata per ottenere
     * tutte le transazioni distinte.
     * Ogni transazione è rappresentata come un oggetto Example e tutti gli oggetti
     * Example sono restituiti come una lista.
     * 
     * Se la tabella è vuota, viene lanciata un'eccezione EmptySetException.
     *
     * @param table il nome della tabella da cui ottenere le transazioni
     * @return una lista di oggetti Example, ognuno rappresentante una transazione
     *         distinta
     * @throws SQLException           se si verifica un errore durante l'esecuzione
     *                                della query SQL
     * @throws EmptySetException      se la tabella è vuota
     * @throws MissingNumberException se manca un numero nella transazione
     */
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
