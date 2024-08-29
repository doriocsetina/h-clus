package database.exceptions;

/**
 * Eccezione lanciata se all'interno del database sono presenti valori non numerici. 
 */
public class MissingNumberException extends Exception {
    public MissingNumberException() {
        super("I dati possiedono attributi non numerici. ");
    }
}
