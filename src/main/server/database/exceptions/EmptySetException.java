package server.database.exceptions;

public class EmptySetException extends Exception {
    public EmptySetException() {
        super("Il database selezionato è vuoto.");
    }
}
