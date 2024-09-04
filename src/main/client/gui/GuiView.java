package client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

public class GuiView {
    private JFrame loginFrame;
    private JTextField ipField;
    private JTextField portField;
    private JLabel infoMessage;
    private JLabel errorMessage;
    private JButton connectButton;

    private JFrame mainFrame;
    private JComboBox<String> availableTabsBox;

    private JTextField fileNameField;
    private JRadioButton loadFromClient;
    private JRadioButton loadFromServer;
    private JButton loadButton;

    private JTextField depthField;
    private JComboBox<String> distanceTypeBox;
    private JButton calculateButton;
    private JCheckBox saveCheckBox;
    private JTextField saveNameField;

    private JTextArea outputArea;

    public GuiView() {
        createAndShowLoginGUI();
    }

    private void createAndShowLoginGUI() {
        loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // Add horizontal and vertical gaps

        ipField = new JTextField();
        gridPanel.add(new JLabel("IP Address:"));
        gridPanel.add(ipField);

        portField = new JTextField();
        gridPanel.add(new JLabel("Port:"));
        gridPanel.add(portField);

        connectButton = new JButton("Connect");
        gridPanel.add(connectButton);

        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        paddedPanel.add(gridPanel, BorderLayout.CENTER);

        errorMessage = new JLabel();
        errorMessage.setForeground(Color.RED);
        paddedPanel.add(errorMessage, BorderLayout.SOUTH);

        loginFrame.add(paddedPanel, BorderLayout.CENTER);

        loginFrame.setPreferredSize(new Dimension(400, 200));
        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    public void closeLoginFrame() {
        System.out.println("sto chiudendo il login frame");
        loginFrame.dispose();
    }

    public void createAndShowMainGUI(List<String> tables) {
        mainFrame = new JFrame("GuiClient");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // combobox SQL tables
        availableTabsBox = new JComboBox<>(tables.toArray(new String[0]));
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JLabel("Table name:    "), BorderLayout.WEST);
        tablePanel.add(availableTabsBox, BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.NORTH);

        // pannello adibito al caricamento del dendrogramma
        JPanel loadDataPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        loadDataPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        fileNameField = new JTextField();
        loadDataPanel.add(new JLabel("File name:"));
        loadDataPanel.add(fileNameField);

        loadFromClient = new JRadioButton("Load from client");
        loadFromServer = new JRadioButton("Load from server");
        ButtonGroup loadGroup = new ButtonGroup();
        loadFromServer.setSelected(true);
        loadGroup.add(loadFromClient);
        loadGroup.add(loadFromServer);
        loadDataPanel.add(loadFromClient);
        loadDataPanel.add(loadFromServer);

        loadButton = new JButton("Load Dendrogram");
        loadDataPanel.add(loadButton);

        // pannello adibito al calcolo del dendrogramma
        JPanel dendrogramPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        dendrogramPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        depthField = new JTextField();
        dendrogramPanel.add(new JLabel("Dendrogram depth:"));
        dendrogramPanel.add(depthField);

        distanceTypeBox = new JComboBox<>(new String[] { "Single-link", "Average-link" });
        dendrogramPanel.add(new JLabel("Distance type:"));
        dendrogramPanel.add(distanceTypeBox);

        saveCheckBox = new JCheckBox("Save with filename: ");
        saveNameField = new JTextField();
        saveNameField.setEnabled(false); // Initially disabled
        saveCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                saveNameField.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        dendrogramPanel.add(saveCheckBox);
        dendrogramPanel.add(saveNameField);

        calculateButton = new JButton("Calculate Dendrogram");
        dendrogramPanel.add(calculateButton);

        // unione dei due pannelli
        JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, loadDataPanel, dendrogramPanel);
        horizontalSplitPane.setResizeWeight(0.5);

        // pannello di output
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        outputPanel.add(scrollPane, BorderLayout.CENTER);
        outputPanel.add(errorMessage, BorderLayout.SOUTH);

        infoMessage = new JLabel();
        outputPanel.add(infoMessage, BorderLayout.SOUTH);

        // unione dei pannelli di input e output
        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, horizontalSplitPane, outputPanel);
        verticalSplitPane.setResizeWeight(0.3);

        mainPanel.add(verticalSplitPane, BorderLayout.CENTER);

        mainFrame.add(mainPanel, BorderLayout.CENTER);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    // login getters
    public String getIpAddress() {
        return ipField.getText();
    }

    public String getPort() {
        return portField.getText();
    }

    // load getter
    public String getTableString() {
        return availableTabsBox.getSelectedItem().toString();
    }

    public String getFileNameString() {
        return fileNameField.getText();
    }

    public boolean isLoadFromServer() {
        return loadFromServer.isSelected();
    }

    // calculate getters
    public String getDepthString() {
        return depthField.getText();
    }

    public String getDistanceString() {
        return distanceTypeBox.getSelectedItem().toString();
    }

    public String getSaveFileString() {
        return saveNameField.getText();
    }

    public boolean isSaving() {
        return saveCheckBox.isSelected();
    }

    // setters
    public void setErrorMessage(String message) {
        errorMessage.setText(message);
    }

    public void setInfoMessage(String message) {
        infoMessage.setText(message);
    }

    public void setOutputText(String text) {
        outputArea.setText(text);
    }

    public void setSaveFileString(String text) {
        saveNameField.setText(text);
    }

    // listeners
    public void addConnectButtonListener(ActionListener listener) {
        connectButton.addActionListener(listener);
    }

    public void addLoadButtonListener(ActionListener listener) {
        loadButton.addActionListener(listener);
    }

    public void addCalculateButtonListener(ActionListener listener) {
        calculateButton.addActionListener(listener);
    }

    public void addSaveCheckBoxListener(ActionListener listener) {
        saveCheckBox.addActionListener(listener);
    }
}