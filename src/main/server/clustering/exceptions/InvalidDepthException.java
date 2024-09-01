package server.clustering.exceptions;

/**
 * Eccezione lanciata quando la profondità con cui è stato istanziato il
 * dendrogramma è superiore al numero di esempi memorizzati nel dataset.
 */
public class InvalidDepthException extends Exception {

    public InvalidDepthException(int depth, int examples) {
        super("il valore di profondità (" + depth + ") è maggiore del numero di Examples (" + examples
                + ") contenuti nel dataset.");
    }

}
