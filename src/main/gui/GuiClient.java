package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class GuiClient {

    private ObjectOutputStream out;
    private ObjectInputStream in;

    private JPanel panel;
    private JFrame loginFrame;
    private JTextField ipField;
    private JTextField portField;

    private JFrame mainFrame;
    private JComboBox<String> availableTabsBox;
    private JTextField fileNameField;
    private JTextField depthField;
    private JComboBox<String> distanceTypeBox;
    private JTextArea outputArea;

    public GuiClient() {
        createAndShowLoginGUI();
    }

    private void createAndShowLoginGUI() {
        panel = new JPanel();
        loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new GridLayout(3, 2));

        ipField = new JTextField();
        loginFrame.add(new JLabel("IP Address:"));
        loginFrame.add(ipField);

        portField = new JTextField();
        loginFrame.add(new JLabel("Port:"));
        loginFrame.add(portField);
        loginFrame.add(panel);

        panel.setLayout(null);

        JButton connectButton = new JButton("Connect");
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    connectToServer(ipField.getText(), Integer.parseInt(portField.getText()));
                    Object obj = in.readObject();

                    createAndShowMainGUI();
                    loginFrame.setVisible(false);
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        loginFrame.add(connectButton);

        loginFrame.pack();
        loginFrame.setVisible(true);
    }

    private void createAndShowMainGUI() {
        mainFrame = new JFrame("GuiClient");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new GridLayout(6, 2));

        availableTabsBox = new JComboBox<String>();
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
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDataOnServer();
            }
        });
        mainFrame.add(loadButton);

        outputArea = new JTextArea();
        mainFrame.add(new JScrollPane(outputArea));

        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private void connectToServer(String ip, int port) throws IOException {
        InetAddress addr = InetAddress.getByName(ip);
        try (Socket socket = new Socket(addr, port)) {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            out.writeObject("gui");
        }
    }

    private void loadDataOnServer() {
        // TODO: Implement this method
    }

    // TODO: Implement the other methods

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                new GuiClient();

            }
        });
    }
}