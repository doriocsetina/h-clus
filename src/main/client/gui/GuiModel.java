package client.gui;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class GuiModel {
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;

    private void connectToServer(String ip, int port) throws IOException {
        InetAddress addr = InetAddress.getByName(ip);
        socket = new Socket(addr, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        out.writeObject("gui");
    }

    private List<String> decodeJsonString(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        List<String> tableStrings = new ArrayList<>();

        try {
            JsonNode rootNode = mapper.readTree(jsonString);
            JsonNode tablesNode = rootNode.path("tables");

            if (tablesNode.isArray()) {
                for (JsonNode tableNode : tablesNode) {
                    tableStrings.add(tableNode.asText());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(tableStrings);
        return tableStrings;
    }

    private void loadDataOnServer() {
        // TODO: Implement this method
    }

}
