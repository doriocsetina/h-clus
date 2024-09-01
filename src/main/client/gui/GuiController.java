package client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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
            /**
             * try {
             * String ipAddress = view.getIpAddress();
             * int port = Integer.parseInt(view.getPort());
             * model.setConnectionDetails(ipAddress, port);
             * 
             * // Simulate server connection and data retrieval
             * String tabsJson = "{\"tables\": [\"Table1\", \"Table2\"]}";
             * model.decodeJsonString(tabsJson);
             * 
             * view.setOutputText(model.decodeJsonString(tabsJson).toString());
             * view.setErrorMessage("");
             * } catch (NumberFormatException ex) {
             * view.setErrorMessage("Invalid port number.");
             * } catch (Exception ex) {
             * view.setErrorMessage("Connection failed: " + ex.getMessage());
             * }
             */
        }
    }

    class LoadDendrogramButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO: Implement load data functionality
        }
    }

    class LearnDendrogramButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    class SaveDendrogramButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            
        }
    }
}