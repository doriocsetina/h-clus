package client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiView {
    private JFrame loginFrame;
    private JTextField ipField;
    private JTextField portField;
    private JLabel errorMessage;

    private JFrame mainFrame;
    private JComboBox<String> availableTabsBox;
    private JTextField fileNameField;
    private JTextField depthField;
    private JComboBox<String> distanceTypeBox;
    private JTextArea outputArea;

    private void createAndShowLoginGUI() {
        loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new BorderLayout());

        // Create a panel with a GridLayout to hold the components
        JPanel gridPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // Add horizontal and vertical gaps

        ipField = new JTextField();
        gridPanel.add(new JLabel("IP Address:"));
        gridPanel.add(ipField);

        portField = new JTextField();
        gridPanel.add(new JLabel("Port:"));
        gridPanel.add(portField);

        JButton connectButton = new JButton("Connect");

        /*
         * connectButton.addActionListener(new ActionListener() {
         * 
         * @Override
         * public void actionPerformed(ActionEvent e) {
         * 
         * // connectToServer(ipField.getText(), Integer.parseInt(portField.getText()));
         * // String tabsJson = (String) in.readObject();
         * // System.out.println(tabsJson);
         * // createAndShowMainGUI(decodeJsonString(tabsJson));
         * // loginFrame.setVisible(false);
         * 
         * }
         * });
         */
        gridPanel.add(connectButton);

        // Create a panel with padding and add the gridPanel to it
        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        paddedPanel.add(gridPanel, BorderLayout.CENTER);

        // Initialize the error message label
        errorMessage = new JLabel();
        errorMessage.setForeground(Color.RED); // Set the text color to red for visibility
        paddedPanel.add(errorMessage, BorderLayout.SOUTH);

        // Add the paddedPanel to the main frame
        loginFrame.add(paddedPanel, BorderLayout.CENTER);

        loginFrame.setPreferredSize(new Dimension(400, 200));
        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    private void createAndShowMainGUI() {
        mainFrame = new JFrame("GuiClient");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new GridLayout(6, 2));

        availableTabsBox = new JComboBox<>();
        mainFrame.add(new JLabel("Table name:"));
        mainFrame.add(availableTabsBox);

        fileNameField = new JTextField();
        mainFrame.add(new JLabel("File name:"));
        mainFrame.add(fileNameField);

        depthField = new JTextField();
        mainFrame.add(new JLabel("Dendrogram depth:"));
        mainFrame.add(depthField);

        distanceTypeBox = new JComboBox<>(new String[] { "Single-link", "Average-link" });
        mainFrame.add(new JLabel("Distance type:"));
        mainFrame.add(distanceTypeBox);

        JButton loadButton = new JButton("Load data");

        mainFrame.add(loadButton);

        outputArea = new JTextArea();
        mainFrame.add(new JScrollPane(outputArea));

        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}
