package main.database.exceptions;

public class DatabaseConnectionException extends Exception {
    public DatabaseConnectionException() {
        super("hai sfrasciato");
    }

    public DatabaseConnectionException(String message) {
        super(message);
    }
}
