package database.exceptions;

public class DatabaseConnectionException extends Exception {
    public DatabaseConnectionException() {
        super("Si è verificato un errore durante la connessione al database");
    }

    public DatabaseConnectionException(String message) {
        super(message);
    }
}
