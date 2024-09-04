package client.gui;

import shared.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Classe model che implementa la logica della GUI e che opera le chiamate al
 * Server.
 * 
 * La classe GuiModel fa uso degli oggetti Request trasformati in Json per
 * eseguire le richieste al Server.
 * Contiene le funzioni per creare la connessione al server
 * {@link #connectToServer(String, int)}, le funzioni per operare le richieste
 * chiamate
 * dall'interfaccia e alcune utility Json.
 */
public class GuiModel {
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;

    private InetAddress address;
    @SuppressWarnings("unused")
    private int port;

    // Single instance of ObjectMapper
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Crea una connessione al server.
     *
     * @param ip   l'indirizzo IP del server
     * @param port la porta del server
     * @return true se la connessione è stata stabilita con successo, altrimenti
     *         false
     * @throws IOException            se si verifica un errore di I/O
     * @throws ClassNotFoundException se la classe dell'oggetto letto non viene
     *                                trovata
     */
    public boolean connectToServer(String ip, int port) throws IOException, ClassNotFoundException {
        this.address = InetAddress.getByName(ip);
        this.port = port;
        socket = new Socket(address, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        out.writeObject("gui");
        if ((String) in.readObject() == "OK")
            return true;
        else
            return false;
    }

    /**
     * Riceve una stringa JSON contenente il nome delle tabelle SQL ricevute dal
     * server.
     *
     * @return una stringa JSON contenente le tabelle
     * @throws IOException            se si verifica un errore di I/O
     * @throws ClassNotFoundException se la classe dell'oggetto letto non viene
     *                                trovata
     */
    public String receiveJsonTables() throws IOException, ClassNotFoundException {
        String tables = (String) in.readObject();
        return tables;
    }

    /**
     * Decodifica una stringa JSON in una lista di nomi di tabelle SQL.
     *
     * @param jsonString la stringa JSON da decodificare
     * @return una lista di nomi di tabelle
     */
    public List<String> decodeJsonString(String jsonString) {
        List<String> tableStrings = new ArrayList<>();

        try {
            JsonNode rootNode = objectMapper.readTree(jsonString);
            JsonNode tablesNode = rootNode.path("tables");

            if (tablesNode.isArray()) {
                for (JsonNode tableNode : tablesNode) {
                    tableStrings.add(tableNode.asText());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tableStrings;
    }

    /**
     * Invia una richiesta di mining e calcolo del dendrogramma al Server.
     *
     * @param table    il nome della tabella SQL a cui accedere
     * @param depth    la profondità del dendrogramma
     * @param distance intero che rappresneta il tipo di distanza
     * @param save     true se il risultato deve essere salvato, altrimenti false
     * @param saveName il nome del file di salvataggio
     * @return la stringa rappresentante il dendrogramma in caso di riuscita della
     *         chiamata, un messaggio di errore in caso di malfunzionamento del
     *         server.
     * @throws IOException            se si verifica un errore di I/O
     * @throws ClassNotFoundException se la classe dell'oggetto letto non viene
     *                                trovata
     */
    public String sendCalculateRequest(String table, int depth, int distance, boolean save, String saveName)
            throws IOException, ClassNotFoundException {
        Map<String, Object> data = new HashMap<>();
        data.put("table", table);
        data.put("depth", depth);
        data.put("distance", distance);
        data.put("save", save);
        data.put("saveName", saveName);

        Request request = new Request("CALC_REQUEST", data);

        String calcJson = objectMapper.writeValueAsString(request);

        out.writeObject(calcJson);
        String response = (String) in.readObject();
        if (response.equals("OK"))
            return (String) in.readObject();
        else
            return "Si è verificato un errore: " + response;
    }

    /**
     * Invia una richiesta di caricamento di dendrogramma al server. Il dendrogramma
     * di cui si richiede il caricamento è accessibile dal FS del server stesso.
     *
     * @param table    il nome della tabella
     * @param fileName il nome del file da caricare
     * @return la stringa rappresentante il dendrogramma in caso di riuscita della
     *         chiamata, un messaggio di errore in caso il file scelto non sia stato
     *         creato sulla table scelta, un messaggio di errore nel caso di
     *         malfunzionamento del server
     * @throws IOException            se si verifica un errore di I/O
     * @throws ClassNotFoundException se la classe dell'oggetto letto non viene
     *                                trovata
     */
    public String sendLoadRequest(String table, String fileName) throws IOException, ClassNotFoundException {

        Map<String, Object> data = new HashMap<>();
        data.put("table", table);
        data.put("fileName", fileName);

        Request request = new Request("LOAD_REQUEST", data);

        String loadJson = objectMapper.writeValueAsString(request);

        out.writeObject(loadJson);
        String response = (String) in.readObject();
        if (response.equals("OK")) {
            return (String) in.readObject();
        } else {
            return "Si è verificato un errore: " + response;
        }
    }

    /**
     * Invia un messaggio di chiusura al server.
     *
     * @throws IOException se si verifica un errore di I/O
     */
    public void sendCloseMessage() throws IOException {
        Request request = new Request("CLOSE_REQUEST", new HashMap<>());
        String closeJson = objectMapper.writeValueAsString(request);

        out.writeObject(closeJson);
    }
}