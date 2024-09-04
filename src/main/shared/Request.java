package shared;

import java.io.Serializable;
import java.util.Map;

/**
 * La classe Request rappresenta una richiesta che può essere inviata al server.
 * 
 * La classe è costituita di un campo header e dei dati, rapprsentati tramite una Map.
 * 
 * Implementa l'interfaccia Serializable per consentirne la trasformazione in
 * Json.
 */
public class Request implements Serializable {
    public static final String LOAD_REQUEST = "LOAD_REQUEST";
    public static final String CALC_REQUEST = "CALC_REQUEST";
    public static final String SAVE_REQUEST = "SAVE_REQUEST";

    private String header;
    private Map<String, Object> data;

    /**
     * Costruttore di default per la classe Request.
     */
    public Request() {
    }

    /**
     * Costruttore che inizializza una richiesta con un header e dei dati
     * specificati.
     *
     * @param header l'header della richiesta
     * @param data   i dati della richiesta
     */
    public Request(String header, Map<String, Object> data) {
        this.header = header;
        this.data = data;
    }

    /**
     * Ottiene l'header della richiesta.
     *
     * @return l'header della richiesta
     */
    public String getHeader() {
        return header;
    }

    /**
     * Imposta l'header della richiesta.
     *
     * @param header l'header della richiesta
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Ottiene i dati della richiesta.
     *
     * @return i dati della richiesta
     */
    public Map<String, Object> getData() {
        return data;
    }

    /**
     * Imposta i dati della richiesta.
     *
     * @param data i dati della richiesta
     */
    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}