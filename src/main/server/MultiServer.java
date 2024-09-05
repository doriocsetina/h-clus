package server;

import java.io.*;
import java.net.*;
import java.sql.SQLException;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import server.clustering.HierachicalClusterMiner;
import server.clustering.exceptions.InvalidDepthException;
import server.data.Data;
import server.database.DbAccess;
import server.database.TableData;
import server.database.exceptions.EmptySetException;
import server.database.exceptions.MissingNumberException;
import server.distance.AverageLinkDistance;
import server.distance.ClusterDistance;
import server.distance.SingleLinkDistance;
import shared.Request;

/**
 * Classe driver del server multithread.
 * 
 * Contiene l'intera logica del server e l'implementazione dei thread
 * {@link ServerOneClient}, che si occupano di ogni client separatamente.
 */
public class MultiServer {
    static final int PORT = 8080;

    /**
     * Metodo main per avviare il server.
     *
     * @param args argomenti della riga di comando
     * @throws IOException se si verifica un errore di I/O
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Starting server on port: " + PORT);
        try (ServerSocket s = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = s.accept();
                try {
                    new ServerOneClient(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                    socket.close();
                }
            }
        }
    }
}

/**
 * Classe thread che gestisce la comunicazione con un singolo client.
 */
class ServerOneClient extends Thread {
    private static final Logger LOGGER = Logger.getLogger(ServerOneClient.class.getName());
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private boolean shouldClose = false;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    /**
     * Costruttore per creare un nuovo thread per gestire un client.
     *
     * @param s il socket del client
     * @throws IOException se si verifica un errore di I/O
     */
    public ServerOneClient(Socket s) throws IOException {
        socket = s;

        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());

        start();
    }

    /**
     * Metodo main del thread per gestire la comunicazione con il client.
     * 
     * Contiene un selettore per differenziare tra un client CLI e un client GUI.
     */
    public void run() {
        LOGGER.setLevel(Level.FINE);
        try {
            String clientType = (String) in.readObject();

            switch (clientType) {
                case "cli":
                    handleCLIClient();
                    break;
                case "gui":
                    handleGUIClient();
                    break;
            }
        } catch (IOException e) {
            LOGGER.severe("Socket not closed.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            LOGGER.severe("Terminating thread, wrong inputs from the client.");
        }
    }

    /**
     * Gestisce la comunicazione con un client GUI.
     * 
     * La comunicazione con i client GUI avviene successivamente l'invio da parte
     * del server delle tabelle SQL presenti. Successivamente il server attende le
     * Request, espresse sottoforma di oggetti Request trasformati in Json. In base
     * all'header della richiesta il server può chiamare le differenti operazioni
     * disponibili.
     */
    private void handleGUIClient() {
        try {
            out.writeObject("OK");
            List<String> tableStrings = retrieveTableStrings();
            out.writeObject(generateJsonFromTableStrings(tableStrings));
            LOGGER.info("Sent tables to GUI client;");
            while (true) {
                if (shouldClose) {
                    break;
                }
                LOGGER.info("GUI client entered new loop.");
                String requestJson = (String) in.readObject();
                Request request = objectMapper.readValue(requestJson, Request.class);

                String header = request.getHeader();
                Map<String, Object> data = request.getData();
                
                switch (header) {
                    case "LOAD_REQUEST":
                        String tableLoad = (String) data.get("table");
                        String fileName = (String) data.get("fileName");
                        handleLoadRequest(tableLoad, fileName);
                        break;

                    case "CALC_REQUEST":
                        String tableCalc = (String) data.get("table");
                        int depth = (Integer) data.get("depth");
                        int distance = (Integer) data.get("distance");
                        boolean save = (boolean) data.get("save");
                        String saveName = (String) data.get("saveName");
                        handleCalculateRequest(tableCalc, depth, distance, false, save, saveName);
                        break;
                    case "CLOSE_REQUEST":
                        shouldClose = true;
                        break;
                    default:
                        LOGGER.info("Unknown request type.");
                        out.writeObject("ERROR");
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gestisce la comunicazione con un client CLI.
     * 
     * Questa funzione gestisce il flow di istruzioni da parte della CLI, terminando
     * con lo switch tra le operazioni disponibili.
     */
    private void handleCLIClient() {
        try {
            while (true) {
                List<String> tables = retrieveTableStrings();
                String table = loadTableFromCLIClient(tables);
                if (table == null || shouldClose) {
                    break;
                }

                int operationType = (int) in.readObject();
                switch (operationType) {
                    case 1:
                        handleLoadRequest(table, (String) in.readObject());
                        break;
                    case 2:
                        handleCalculateRequest(table, (int) in.readObject(), (int) in.readObject(), true, false, "");
                        break;
                    default:
                        LOGGER.info("Codice operazione sconosciuto.");
                        break;
                }
            }
            LOGGER.info("closing thread...");
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.severe("Error handling CLI client: " + e.getMessage());
        }
    }

    /**
     * Verifica che la tabella data in input tramite CLI è presente all'interno del
     * database SQL, ritornando la tabella scelta.
     *
     * @param tableStrings Lista delle stringhe delle tabelle contenute nel database
     *                     SQL.
     * @return Il nome della tabella scelta o null se non è possibile caricare i
     *         dati.
     * @throws IOException se si verifica un errore di I/O
     */
    private String loadTableFromCLIClient(List<String> tableStrings) throws IOException {
        LOGGER.fine("The current thread is: " + Thread.currentThread().toString());

        String chosenTable = null;
        int attempts = 0;
        while (attempts < 3) {
            LOGGER.info("The current thread has done: " + attempts + " attempts.");
            try {
                int operationType = (int) in.readObject();
                if (operationType == -1) {
                    shouldClose = true;
                    break;
                }
                String tableName = (String) in.readObject();
                if (tableStrings.contains(tableName.toLowerCase())) {
                    chosenTable = tableName;
                    out.writeObject("OK");
                    break;
                } else {
                    out.writeObject("Nome tabella non valido.");
                    attempts++;
                }
            } catch (ClassNotFoundException e) {
                LOGGER.warning("Invalid data received from client");
                out.writeObject("Ricevuti dati invalidi.");
                attempts++;
            }
        }
        if (chosenTable == null) {
            out.writeObject("Dopo tre tentativi non è stato possibile caricare i dati.");
            return null;
        }

        return chosenTable;
    }

    /**
     * Gestisce una richiesta di caricamento di Dendrogramma del client.
     * 
     * Istanzia un oggetto Data e tramite HierarchicalClusterMiner deserializza il
     * file dal nome dato in input, inviando in output il dendrogramma contenuto
     * all'interno del file.
     *
     * @param table    Il nome della tabella SQL.
     * @param fileName Il nome del file da caricare.
     * @throws IOException            se si verifica un errore di I/O
     * @throws ClassNotFoundException se la classe dell'oggetto caricato non viene
     *                                trovata
     */
    private void handleLoadRequest(String table, String fileName) throws IOException, ClassNotFoundException {
        try {
            LOGGER.info("Handling loading request.");
            Data data = new Data(table);

            HierachicalClusterMiner clustering = HierachicalClusterMiner.load(fileName);

            if (clustering.validateData(data)) {
                out.writeObject("OK");
                out.writeObject(clustering.toString(data));
            } else {
                out.writeObject("ERRORE: il dendrogramma non è stato costruito dai dati contenuti nel database.");
            }
        } catch (SQLException | EmptySetException | MissingNumberException | FileNotFoundException e) {
            out.writeObject(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gestisce una richiesta di calcolo del dendrogramma dal client.
     * 
     * Istanzia un oggetto Data, istanzia un oggetto HierarchicalClusterMiner e
     * procede a eseguire il processo di mining. In base ai valori booleani isCLI e
     * save il metodo può procedere a salvare il dendrogramma appena creato. Nel
     * caso della CLI il nome è chiesto in input, nel caso della GUI il nome è dato
     * come parametro formale
     *
     * @param table       Il nome della tabella SQL.
     * @param depth       La profondità del dendrogramma.
     * @param distanceInt Il tipo di distanza.
     * @param isCLI       Indica se la richiesta proviene da un client CLI.
     * @param save        Indica se il risultato deve essere salvato.
     * @param fileName    Il nome del file di salvataggio.
     * @throws IOException            se si verifica un errore di I/O
     * @throws ClassNotFoundException se il socket riceve in input una classe non
     *                                aspettata dal client
     */
    private void handleCalculateRequest(String table, int depth, int distanceInt, boolean isCLI, boolean save,
            String fileName)
            throws IOException, ClassNotFoundException {
        try {
            LOGGER.info("Handling mining request.");
            Data data = new Data(table);
            HierachicalClusterMiner clustering = new HierachicalClusterMiner(depth);
            ClusterDistance distance = new AverageLinkDistance();
            switch (distanceInt) {
                case 1:
                    distance = new SingleLinkDistance();
                    break;
                case 2:
                    distance = new AverageLinkDistance();
                    break;
                default:
                    throw new IllegalArgumentException("Tipo di distanza invalida: " + distanceInt);
            }
            try {
                clustering.mine(data, distance);
                out.writeObject("OK");
                out.writeObject(clustering.toString(data));
                if (isCLI) {
                    fileName = (String) in.readObject();
                    clustering.save(fileName);
                } else if (save) {
                    clustering.save(fileName);
                }
            } catch (InvalidDepthException e) {
                out.writeObject(e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException | EmptySetException | MissingNumberException e) {
            out.writeObject(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Recupera le stringhe delle tabelle dal database.
     *
     * @return Lista delle stringhe delle tabelle.
     */
    private List<String> retrieveTableStrings() {
        DbAccess db = new DbAccess();
        TableData tableData = new TableData(db);
        List<String> tableStrings = new LinkedList<>();
        try {
            tableStrings = tableData.getTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableStrings;
    }

    /**
     * Genera una stringa JSON da una lista di stringhe delle tabelle.
     *
     * @param tableStrings Lista delle stringhe delle tabelle.
     * @return Stringa JSON che rappresenta le stringhe delle tabelle.
     */
    private String generateJsonFromTableStrings(List<String> tableStrings) {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (String tableString : tableStrings) {
            arrayNode.add(tableString);
        }

        ObjectNode jsonObject = objectMapper.createObjectNode();
        jsonObject.set("tables", arrayNode);

        String jsonString = "";
        try {
            jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}