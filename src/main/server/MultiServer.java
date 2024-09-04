package server;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.HashMap;
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
 * classe driver del server multithread.
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
            System.err.println("Socket not closed.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            LOGGER.severe("Terminating thread, wrong inputs from the client. ");
        }
    }

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
                        handleCalculateRequest(table, (int) in.readObject(), (int) in.readObject(), true);
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
     * Carica i dati del database dopo aver ricevuto dal Client CLI il nome della
     * table
     * SQL dove sono contenuti i dati.
     *
     * @return l'istanza data popolata con i dati contenuti nel DB, o null se non è
     *         possibile caricare i dati
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
                if (tableStrings.contains(tableName)) {
                    chosenTable = tableName;
                    out.writeObject("OK");
                    break;
                } else {

                }
            } catch (ClassNotFoundException e) {
                LOGGER.warning("Invalid data received from client");
                out.writeObject("Ricevuti dati invalidi.");
                attempts++;
            }

        }
        if (chosenTable == null) {
            out.writeObject("Dopo tre tentativi non è stato possibile caricare i dati. ");
            return null;
        }

        return chosenTable;
    }

    /**
     * 
     * @param clustering
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private void handleCLISaveDendrogram(HierachicalClusterMiner clustering)
            throws ClassNotFoundException, IOException {
        String fileName = (String) in.readObject();
        clustering.save(fileName);
        LOGGER.fine("file saved.");
    }

    private void handleGUIClient() {
        try {
            out.writeObject("OK");
            List<String> tableStrings = retrieveTableStrings();
            out.writeObject(generateJsonFromTableStrings(tableStrings));
            LOGGER.info("sent tables;");
            while (true) {
                LOGGER.info("new loop for the client");
                String requestJson = (String) in.readObject();
                System.err.println("client sent this: " + requestJson);
                ObjectMapper objectMapper = new ObjectMapper();
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
                        handleCalculateRequest(tableCalc, depth, distance, false);
                        break;
                    default:
                        LOGGER.info("Unknown request type.");
                        out.writeObject("ERROR");
                        break;

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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
     * @param stringheTabelle Lista delle stringhe delle tabelle.
     * @return Stringa JSON che rappresenta le stringhe delle tabelle.
     */
    private String generateJsonFromTableStrings(List<String> tableStrings) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();
        for (String tableString : tableStrings) {
            arrayNode.add(tableString);
        }

        ObjectNode jsonObject = mapper.createObjectNode();
        jsonObject.set("tables", arrayNode);

        String jsonString = "";
        try {
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(jsonString);
        return jsonString;
    }

    private void handleLoadRequest(String table, String fileName) throws IOException, ClassNotFoundException {
        try {
            Data data = new Data(table);

            HierachicalClusterMiner clustering = HierachicalClusterMiner.load(fileName);

            if (clustering.validateData(data)) {
                out.writeObject("OK");
                out.writeObject(clustering.toString(data));
            } else {
                out.writeObject(
                        "ERRORE: il dendrogramma non è stato costruito "
                                + "dai dati contenuti nel database.");
            }

        } catch (SQLException | EmptySetException | MissingNumberException | FileNotFoundException e) {
            out.writeObject(e.getMessage());
            e.printStackTrace();
        }

    }

    private void handleCalculateRequest(String table, int depth, int distanceInt, boolean isCLI)
            throws IOException, ClassNotFoundException {
        try {
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
                if (isCLI)
                    handleCLISaveDendrogram(clustering);
            } catch (InvalidDepthException e) {
                System.out.println("l'invalida profondità eccezione è avvenuta.");
                out.writeObject(e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException | EmptySetException | MissingNumberException e) {
            out.writeObject(e.getMessage());
            e.printStackTrace();
        }
    }

}