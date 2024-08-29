package server;

import java.io.*;
import java.net.*;

import clustering.HierachicalClusterMiner;
import clustering.exceptions.InvalidDepthException;
import data.Data;
import distance.AverageLinkDistance;
import distance.ClusterDistance;
import distance.SingleLinkDistance;

/**
 * classe driver del server multithread.
 */
public class MultiServer {
    static final int PORT = 8080;

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

class ServerOneClient extends Thread {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ServerOneClient(Socket s) throws IOException {
        socket = s;

        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());

        start();
    }

    public void run() {
        try {
            while (true) {

                // Integer risposta = (Integer) in.readObject();
                // System.err.println("CLIENT SENT: " + risposta);
                int operationType = (int) in.readObject();

                if (operationType == -1)
                    break;

                System.out.println(Thread.currentThread());
                while (operationType != 0)
                    operationType = (int) in.readObject();
                String tableName = (String) in.readObject();
                Data data = new Data(tableName);
                System.out.println("DEBUG: ho caricato il database;");
                System.out.println("DEBUG: i dati nel database appena caricato ha hashcode: " + data.hashCode());
                out.writeObject("OK");

                operationType = (int) in.readObject();
                switch (operationType) {
                    case 1:
                        // carica dendrogramma da file
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
                        break;
                    case 2:
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
                            System.out.println("DEBUG: about to save data");
                            fileName = (String) in.readObject();
                            clustering.save(fileName);
                            System.out.println("DEBUG: file saved");
                        } catch (InvalidDepthException e) {
                            e.printStackTrace();
                        } catch (EOFException e) {
                            System.err.println("catched an eof exception. ");
                            e.printStackTrace();
                        }

                        break;
                    default:
                        System.out.println("Codice operazione sconosciuto.");
                        break;
                }

            }
            System.out.println("closing...");
        } catch (IOException e) {
            System.err.println("Socket not closed.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}