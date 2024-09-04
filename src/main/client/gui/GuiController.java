package client.gui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

/**
 * La classe GuiController gestisce l'interazione tra il modello e la vista.
 */
public class GuiController {
    private GuiModel model;
    private GuiView view;

    /**
     * Costruisce un nuovo GuiController con il modello e la vista specificati.
     *
     * @param model il modello da utilizzare
     * @param view  la vista da utilizzare
     */
    public GuiController(GuiModel model, GuiView view) {
        this.model = model;
        this.view = view;

        this.view.addConnectButtonListener(new ConnectButtonListener());
    }

    /**
     * Listener per il pulsante di connessione.
     * 
     * Il model esegue la chiamata di connessione al server, in caso la connessione
     * va a buon fine vengono ricevute le tabelle SQL disponibili e successivamente
     * creato il frame principale della vista.
     */
    class ConnectButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                model.connectToServer(
                        view.getIpAddress(),
                        Integer.parseInt(view.getPort()));

                List<String> tables = model.decodeJsonString(model.receiveJsonTables());

                view.closeLoginFrame();

                view.createAndShowMainGUI(tables);
                view.addLoadButtonListener(new LoadDendrogramButtonListener());
                view.addCalculateButtonListener(new CalculateDendrogramButtonListener());
                view.addWindowListener(new ClosingWindowListener());
                view.setErrorMessage("");
            } catch (IOException | ClassNotFoundException ex) {
                view.setErrorMessage(ex.getMessage());
                ex.printStackTrace();
            } catch (NumberFormatException ex) {
                view.setErrorMessage("La porta dev'essere necessariamente un numero intero positivo.");
            }
        }
    }

    /**
     * Listener per il pulsante di caricamento del dendrogramma.
     * 
     * Esegue tramite il modello una richiesta LOAD.
     */
    class LoadDendrogramButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                view.setOutputText(
                        model.sendLoadRequest(
                                view.getTableString(),
                                view.getFileNameString()));
                view.setErrorMessage("");
                view.setInfoMessage("");
            } catch (IOException | ClassNotFoundException ex) {
                view.setErrorMessage(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    /**
     * Listener per il pulsante di calcolo del dendrogramma.
     * 
     * Assegna un intero alla distanza selezionata dalla vista, verifica l'opzione
     * di salvataggio e tramite il modello esegue una richiesta CALC.
     */
    class CalculateDendrogramButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int distance = 1;
                switch (view.getDistanceString()) {
                    case "Single-link":
                        distance = 1;
                        break;
                    case "Average-link":
                        distance = 2;
                        break;
                }
                if (!view.isSaving()
                        || (view.isSaving() && !view.getSaveFileString().equals(""))) {
                    view.setOutputText(
                            model.sendCalculateRequest(
                                    view.getTableString(),
                                    Integer.parseInt(view.getDepthString()),
                                    distance,
                                    view.isSaving(),
                                    view.getSaveFileString()));

                    view.setErrorMessage("");

                    if (view.isSaving())
                        view.setInfoMessage("File salvato con successo!");
                    else
                        view.setInfoMessage("");
                } else
                    view.setErrorMessage("Specificare un nome per salvare il file. ");
                view.setSaveFileString("");
            } catch (IOException | ClassNotFoundException ex) {
                view.setErrorMessage(ex.getMessage());
                ex.printStackTrace();
            } catch (NumberFormatException ex) {
                view.setErrorMessage("La profondità dev'essere necessariamente un numero intero positivo.");
            }
        }
    }

    /**
     * Listener per la chiusura dell'applicazione principale. 
     * 
     * Invia una richiesta di chiusura thread al server quando il client viene chiuso. 
     */
    class ClosingWindowListener extends WindowAdapter  {
        @Override
            public void windowClosing(WindowEvent e) {
                try {
                    model.sendCloseMessage();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
    }
}