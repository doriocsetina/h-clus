package client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowListener;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

/**
 * La classe GuiView rappresenta l'interfaccia grafica per l'applicazione.
 * All'interno della classe sono unicamente presenti i componenti visivi e i
 * metodi getters e setters utilizzati dal Controller per implementare la logica
 * dell'applicazione.
 */
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
    private JRadioButton loadFromServer;
    private JButton loadButton;

    private JTextField depthField;
    private JComboBox<String> distanceTypeBox;
    private JButton calculateButton;
    private JCheckBox saveCheckBox;
    private JTextField saveNameField;

    private JTextArea outputArea;

    /**
     * Costruisce una nuova GuiView e inizializza l'interfaccia di login.
     */
    public GuiView() {
        createAndShowLoginGUI();
    }

    /**
     * Crea e visualizza l'interfaccia di login.
     */
    private void createAndShowLoginGUI() {
        loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // Aggiungi spazi orizzontali e verticali

        ipField = new JTextField();
        gridPanel.add(new JLabel("Indirizzo IP:"));
        gridPanel.add(ipField);

        portField = new JTextField();
        gridPanel.add(new JLabel("Porta:"));
        gridPanel.add(portField);

        connectButton = new JButton("Connetti");
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

    /**
     * Chiude la finestra di login.
     */
    public void closeLoginFrame() {
        loginFrame.dispose();
    }

    /**
     * Crea e visualizza l'interfaccia principale.
     *
     * @param tables la lista dei nomi delle tabelle da visualizzare nella combo box
     */
    public void createAndShowMainGUI(List<String> tables) {
        mainFrame = new JFrame("GuiClient");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // combobox tabelle SQL
        availableTabsBox = new JComboBox<>(tables.toArray(new String[0]));
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JLabel("Nome tabella:    "), BorderLayout.WEST);
        tablePanel.add(availableTabsBox, BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.NORTH);

        // pannello adibito al caricamento del dendrogramma
        JPanel loadDataPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        loadDataPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        fileNameField = new JTextField();
        loadDataPanel.add(new JLabel("Nome file:"));
        loadDataPanel.add(fileNameField);

        loadFromServer = new JRadioButton("Carica dal server");
        ButtonGroup loadGroup = new ButtonGroup();
        loadFromServer.setSelected(true);

        loadGroup.add(loadFromServer);
        loadDataPanel.add(new JLabel());
        loadDataPanel.add(loadFromServer);

        loadButton = new JButton("Carica Dendrogramma");
        loadDataPanel.add(new JLabel()); // Etichetta vuota per allineare il pulsante
        loadDataPanel.add(loadButton);

        // pannello adibito al calcolo del dendrogramma
        JPanel dendrogramPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        dendrogramPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        depthField = new JTextField();
        dendrogramPanel.add(new JLabel("Profondità dendrogramma:"));
        dendrogramPanel.add(depthField);

        distanceTypeBox = new JComboBox<>(new String[] { "Single-link", "Average-link" });
        dendrogramPanel.add(new JLabel("Tipo di distanza:"));
        dendrogramPanel.add(distanceTypeBox);

        saveCheckBox = new JCheckBox("Salva con nome file: ");
        saveNameField = new JTextField();
        saveNameField.setEnabled(false); // Inizialmente disabilitato
        saveCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                saveNameField.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        dendrogramPanel.add(saveCheckBox);
        dendrogramPanel.add(saveNameField);

        calculateButton = new JButton("Calcola Dendrogramma");
        dendrogramPanel.add(new JLabel()); // Etichetta vuota per allineare il pulsante
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

        JPanel messagePanel = new JPanel(new GridLayout(2, 1));
        errorMessage = new JLabel();
        errorMessage.setForeground(Color.RED);
        messagePanel.add(errorMessage);

        infoMessage = new JLabel();
        messagePanel.add(infoMessage);

        outputPanel.add(messagePanel, BorderLayout.SOUTH);

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

    /**
     * Ottiene l'indirizzo IP inserito.
     *
     * @return l'indirizzo IP
     */
    public String getIpAddress() {
        return ipField.getText();
    }

    /**
     * Ottiene la porta inserita.
     *
     * @return la porta
     */
    public String getPort() {
        return portField.getText();
    }

    // load getters

    /**
     * Ottiene il nome della tabella selezionata.
     *
     * @return il nome della tabella
     */
    public String getTableString() {
        return availableTabsBox.getSelectedItem().toString();
    }

    /**
     * Ottiene il nome del file inserito.
     *
     * @return il nome del file
     */
    public String getFileNameString() {
        return fileNameField.getText();
    }

    /**
     * Verifica se l'opzione "Load from server" è selezionata.
     *
     * @return true se "Load from server" è selezionata, altrimenti false
     */
    public boolean isLoadFromServer() {
        return loadFromServer.isSelected();
    }

    // calculate getters

    /**
     * Ottiene la profondità del dendrogramma inserita.
     *
     * @return la profondità del dendrogramma
     */
    public String getDepthString() {
        return depthField.getText();
    }

    /**
     * Ottiene il tipo di distanza selezionato.
     *
     * @return il tipo di distanza
     */
    public String getDistanceString() {
        return distanceTypeBox.getSelectedItem().toString();
    }

    /**
     * Ottiene il nome del file di salvataggio inserito.
     *
     * @return il nome del file di salvataggio
     */
    public String getSaveFileString() {
        return saveNameField.getText();
    }

    /**
     * Verifica se l'opzione di salvataggio è selezionata.
     *
     * @return true se l'opzione di salvataggio è selezionata, altrimenti false
     */
    public boolean isSaving() {
        return saveCheckBox.isSelected();
    }

    // setters

    /**
     * Imposta il messaggio di errore.
     *
     * @param message il messaggio di errore
     */
    public void setErrorMessage(String message) {
        errorMessage.setText(message);
    }

    /**
     * Imposta il messaggio informativo.
     *
     * @param message il messaggio informativo
     */
    public void setInfoMessage(String message) {
        infoMessage.setText(message);
    }

    /**
     * Imposta il testo dell'area di output.
     *
     * @param text il testo dell'area di output
     */
    public void setOutputText(String text) {
        outputArea.setText(text);
    }

    /**
     * Imposta il nome del file di salvataggio.
     *
     * @param text il nome del file di salvataggio
     */
    public void setSaveFileString(String text) {
        saveNameField.setText(text);
    }

    // listeners

    /**
     * Aggiunge un listener al pulsante di connessione.
     *
     * @param listener il listener da aggiungere
     */
    public void addConnectButtonListener(ActionListener listener) {
        connectButton.addActionListener(listener);
    }

    /**
     * Aggiunge un listener al pulsante di caricamento.
     *
     * @param listener il listener da aggiungere
     */
    public void addLoadButtonListener(ActionListener listener) {
        loadButton.addActionListener(listener);
    }

    /**
     * Aggiunge un listener al pulsante di calcolo del dendrogramma.
     *
     * @param listener il listener da aggiungere
     */
    public void addCalculateButtonListener(ActionListener listener) {
        calculateButton.addActionListener(listener);
    }

    /**
     * Aggiunge un listener alla checkbox di salvataggio.
     *
     * @param listener il listener da aggiungere
     */
    public void addSaveCheckBoxListener(ActionListener listener) {
        saveCheckBox.addActionListener(listener);
    }

    /**
     * Aggiunge un listener alla finestra principale.
     *
     * @param listener il listener da aggiungere
     */
    public void addWindowListener(WindowListener listener) {
        mainFrame.addWindowListener(listener);
    }
}