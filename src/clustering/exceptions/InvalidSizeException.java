package clustering.exceptions;

/**
 * Eccezione lanciata quando si calcola la distanza tra due oggetti Example di
 * diverse dimensioni
 */
public class InvalidSizeException extends Exception {
    public InvalidSizeException() {
        super("Impossibile calcolare la distanza: i due oggetti Examples non hanno la stessa dimensione.");
    }
}
