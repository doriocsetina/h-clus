package client.gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.NumberFormatter;

import java.awt.*;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.List;

public class GuiView {
    private JFrame loginFrame;
    private JTextField ipField;
    private JTextField portField;
    private JLabel errorMessage;
    private JButton connectButton;

    private JFrame mainFrame;
    private JComboBox<String> availableTabsBox;

    private JTextField fileNameField;
    private JRadioButton loadFromClient;
    private JRadioButton loadFromServer;
    private JButton loadButton;

    private JFormattedTextField depthField;
    private JComboBox<String> distanceTypeBox;
    private JButton calculateButton;

    private JTextArea outputArea;
    private JTextField saveNameField;
    private JButton saveButton;

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
        mainPanel.add(new JLabel("Table name:"), BorderLayout.NORTH);
        mainPanel.add(availableTabsBox, BorderLayout.NORTH);

        // pannello adibito al caricamento del dendrogramma
        JPanel loadDataPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        loadDataPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        fileNameField = new JTextField();
        loadDataPanel.add(new JLabel("File name:"));
        loadDataPanel.add(fileNameField);

        loadFromClient = new JRadioButton("Load from client");
        loadFromServer = new JRadioButton("Load from server");
        ButtonGroup loadGroup = new ButtonGroup();
        loadFromClient.setSelected(true);
        loadGroup.add(loadFromClient);
        loadGroup.add(loadFromServer);
        loadDataPanel.add(loadFromClient);
        loadDataPanel.add(loadFromServer);

        loadButton = new JButton("Load Dendrogram");
        loadDataPanel.add(loadButton);

        // pannello adibito al calcolo del dendrogramma
        JPanel dendrogramPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        dendrogramPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        depthField = createIntegerField();
        dendrogramPanel.add(new JLabel("Dendrogram depth:"));
        dendrogramPanel.add(depthField);

        distanceTypeBox = new JComboBox<>(new String[] { "Single-link", "Average-link" });
        dendrogramPanel.add(new JLabel("Distance type:"));
        dendrogramPanel.add(distanceTypeBox);

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

        JPanel bottomPanel = new JPanel(new BorderLayout());
        saveNameField = new JTextField();
        saveButton = new JButton("Save dendrogram");

        bottomPanel.add(errorMessage, BorderLayout.NORTH);
        bottomPanel.add(saveNameField, BorderLayout.CENTER);
        bottomPanel.add(saveButton, BorderLayout.EAST);
        outputPanel.add(bottomPanel, BorderLayout.SOUTH);

        // unione dei pannelli di input e output
        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, horizontalSplitPane, outputPanel);
        horizontalSplitPane.setResizeWeight(0.3);

        mainPanel.add(verticalSplitPane, BorderLayout.CENTER);
        
        mainFrame.add(mainPanel, BorderLayout.CENTER);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private JFormattedTextField createIntegerField() {
        NumberFormat format = NumberFormat.getIntegerInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setAllowsInvalid(true); // Allow invalid input to be corrected
        formatter.setCommitsOnValidEdit(true);
        formatter.setMinimum(0); // Set minimum value if needed
        formatter.setMaximum(Integer.MAX_VALUE); // Set maximum value if needed

        JFormattedTextField field = new JFormattedTextField(formatter);
        field.setColumns(10); // Set the preferred width of the field
        return field;
    }
    // login getters

    public String getIpAddress() {
        return ipField.getText();
    }

    public String getPort() {
        return portField.getText();
    }

    // load getter

    public String getTable() {
        return availableTabsBox.getSelectedItem().toString();
    }

    public String getFileName() {
        return fileNameField.getText();
    }

    public boolean isLoadFromClient() {
        return loadFromClient.isSelected();
    }

    // calculate getters

    // TODO fix this
    public int getDepth() {
        return Integer.parseInt(depthField.getText());
    }

    public String getDistanceString() {
        return distanceTypeBox.getSelectedItem().toString();
    }

    // setters

    public void setErrorMessage(String message) {
        errorMessage.setText(message);
    }

    public void setOutputText(String text) {
        outputArea.setText(text);
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

    public void addSaveButtonListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }
}
