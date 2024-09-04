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
                String ipAddress = view.getIpAddress();
                int port = Integer.parseInt(view.getPort());
                model.connectToServer(ipAddress, port);

                List<String> tables = model.decodeJsonString(model.receiveJsonTables());

                view.closeLoginFrame();

                view.createAndShowMainGUI(tables);
                view.addCalculateButtonListener(new CalculateDendrogramButtonListener());
                view.addLoadButtonListener(new LoadDendrogramButtonListener());
            } catch (IOException ex) {
                view.setErrorMessage(ex.getMessage());
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                view.setErrorMessage(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    class LoadDendrogramButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                view.setOutputText(
                        model.sendLoadRequest(
                                view.getTable(),
                                view.getFileName()));
            } catch (IOException ex) {
                view.setErrorMessage(ex.getMessage());
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
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

                view.setOutputText(model.sendCalculateRequest(
                        view.getTable(),
                        view.getDepth(),
                        distance));
            } catch (IOException ex) {
                view.setErrorMessage(ex.getMessage());
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                view.setErrorMessage(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    class SaveDendrogramButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}