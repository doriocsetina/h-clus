package client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class GuiController {
    private GuiModel model;
    private GuiView view;

    public GuiController(GuiModel model, GuiView view) {
        this.model = model;
        this.view = view;

        this.view.addConnectButtonListener(new ConnectButtonListener());
        this.view.addLoadButtonListener(new LoadDendrogramButtonListener());
    }

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
                view.setErrorMessage("");
            } catch (IOException | ClassNotFoundException ex) {
                view.setErrorMessage(ex.getMessage());
                ex.printStackTrace();
            } catch (NumberFormatException ex) {
                view.setErrorMessage("La porta dev'essere necessariamente un numero intero positivo.");
            }
        }
    }

    class LoadDendrogramButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                view.setOutputText(
                        model.sendLoadRequest(
                                view.getTableString(),
                                view.getFileNameString()));
                view.setErrorMessage("");
            } catch (IOException | ClassNotFoundException ex) {
                view.setErrorMessage(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

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

}