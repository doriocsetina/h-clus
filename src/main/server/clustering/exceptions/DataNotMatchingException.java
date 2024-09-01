package server.clustering.exceptions;

/**
 * Eccezione lanciata quando un dendrogramma costruito su un dataset viene applicato a un dataset differente. 
 */
public class DataNotMatchingException extends Exception {
    public DataNotMatchingException() {
        super("Questo dendrogramma è stato costruito su un differente dataset.");
    }
}
