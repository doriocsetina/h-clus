package server;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
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

                case "tui":
                    while (true) {

                        Data data = loadDataFromClient();
                        if (data == null) {
                            break;
                        }
                        if (shouldClose) {
                            break;
                        }

                        int operationType = (int) in.readObject();
                        switch (operationType) {
                            case 1:
                                // carica dendrogramma da file
                                handleLoadDendrogram(data);
                                break;
                            case 2:
                                // apprendi dendrogramma da database
                                handleLearnDendrogram(data);
                                break;
                            default:
                                LOGGER.info("Codice operazione sconosciuto.");
                                break;
                        }

                    }
                    LOGGER.info("closing thread...");

                case "gui":
                    String tableStrings = findTableStrings();
                    LOGGER.info("sent tables;");
                    out.writeObject(tableStrings);
                    while (true) {

                    }
            }
        } catch (IOException e) {
            System.err.println("Socket not closed.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            LOGGER.severe("Terminating thread, wrong inputs from the client. ");
        }
    }

    private String findTableStrings() {
        DbAccess db = new DbAccess();
        TableData tableData = new TableData(db);
        List<String> tableStrings = new LinkedList<>();
        try {
            tableStrings = tableData.getTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

    /**
     * Carica i dati del database dopo aver ricevuto dal Client il nome della table
     * SQL dove sono contenuti i dati.
     *
     * @return l'istanza data popolata con i dati contenuti nel DB, o null se non è
     *         possibile caricare i dati
     * @throws IOException se si verifica un errore di I/O
     */
    private Data loadDataFromClient() throws IOException {
        LOGGER.fine("The current thread is: " + Thread.currentThread().toString());

        Data data = null;
        int attempts = 0;
        while (data == null && attempts < 3) {
            LOGGER.info("The current thread has done: " + attempts + " attempts.");
            try {
                int operationType = (int) in.readObject();
                if (operationType == -1) {
                    shouldClose = true;
                    break;
                }
                String tableName = (String) in.readObject();
                data = new Data(tableName);
                LOGGER.info("Loaded the database;");
                LOGGER.fine("data checksum: " + data.generateChecksum());
                out.writeObject("OK");
            } catch (SQLException | EmptySetException | MissingNumberException e) {
                LOGGER.warning(e.getMessage());
                out.writeObject(e.getMessage());
                attempts++;
            } catch (ClassNotFoundException e) {
                LOGGER.warning("Invalid data received from client");
                out.writeObject("Ricevuti dati invalidi.");
                attempts++;
            }

        }
        if (data == null) {
            out.writeObject("Dopo tre tentativi non è stato possibile caricare i dati. ");
            return null;
        }

        return data;
    }

    /**
     * Gestisce l'operazione di caricamento del dendrogramma a partire dal file
     * specificato dal client e restituisce al client la stringa contenente il
     * dendrogramma popolato; restituisce un messaggio di errore
     * nel caso vengano lanciate delle eccezioni.
     * 
     * Nel caso in cui il dendrogramma caricato da file è stato costruito su dei
     * dati diversi da quelli contenuti nella table specificata il metodo ritorna al
     * client un messaggio d'errore.
     *
     * @param data i dati da utilizzare
     * @throws IOException            se si verifica un errore di I/O
     * @throws ClassNotFoundException se si verifica un errore durante la lettura
     *                                dell'oggetto
     */
    private void handleLoadDendrogram(Data data) throws IOException, ClassNotFoundException {
        LOGGER.info("Handling loading dendrogram from file.");
        String fileName = (String) in.readObject();
        try {
            HierachicalClusterMiner clustering = HierachicalClusterMiner.load(fileName);

            if (clustering.validateData(data)) {
                out.writeObject("OK");
                out.writeObject(clustering.toString(data));
            } else {
                out.writeObject(
                        "ERRORE: il dendrogramma non è stato costruito "
                                + "dai dati contenuti nel database.");
            }
        } catch (FileNotFoundException e) {
            out.writeObject(e.getMessage());
        }
    }

    /**
     * Gestisce l'operazione di apprendimento del dendrogramma a partire dai dati
     * precedendemente ottenuti tramite il database e inizializza l'operazione di
     * mining sui dati forniti in input. Al client viene richiesto la distanza da
     * utilizzare. Al termine dell'operazione ritorna al client la stringa
     * rappresentante del dendrogramma ottenuto. Successivamente il dendrogramma
     * ottenuto viene serializzato su disco con il nome specificato dal client.
     *
     * @param data i dati da utilizzare
     * @throws IOException            se si verifica un errore di I/O
     * @throws ClassNotFoundException se si verifica un errore durante la lettura
     *                                dell'oggetto
     */
    private void handleLearnDendrogram(Data data) throws IOException, ClassNotFoundException {
        LOGGER.info("Handling learning dendrogram from database.");
        // apprendi dendrogramma da database;
        int depth = (int) in.readObject();
        HierachicalClusterMiner clustering = new HierachicalClusterMiner(depth);
        int distanceInt = (int) in.readObject();
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
            handleSaveDendrogram(clustering);
        } catch (InvalidDepthException e) {
            e.printStackTrace();
        } catch (EOFException e) {
            System.err.println("catched an eof exception. ");
            e.printStackTrace();
        }

    }

    private void handleSaveDendrogram(HierachicalClusterMiner clustering) throws ClassNotFoundException, IOException {
        String fileName = (String) in.readObject();
        clustering.save(fileName);
        LOGGER.fine("file saved.");
    }
}