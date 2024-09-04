package client.gui;

import shared.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private InetAddress address;
    @SuppressWarnings("unused")
    private int port;

    public boolean connectToServer(String ip, int port) throws IOException, ClassNotFoundException {
        this.address = InetAddress.getByName(ip);
        this.port = port;
        socket = new Socket(address, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        out.writeObject("gui");
        if ((String) in.readObject() == "OK")
            return true;
        else
            return false;
    }

    public String receiveJsonTables() throws IOException, ClassNotFoundException {
        String tables = (String) in.readObject();
        System.out.println(tables);
        return tables;
    }

    public List<String> decodeJsonString(String jsonString) {
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

    public String sendCalculateRequest(String table, int depth, int distance, boolean save, String saveName) throws IOException, ClassNotFoundException {
        Map<String, Object> data = new HashMap<>();
        data.put("table", table);
        data.put("depth", depth);
        data.put("distance", distance);
        data.put("save", save);
        data.put("saveName", saveName);

        Request request = new Request("CALC_REQUEST", data);

        ObjectMapper objectMapper = new ObjectMapper();
        String calcJson = objectMapper.writeValueAsString(request);
        System.out.println(calcJson);

        out.writeObject(calcJson);
        String response = (String) in.readObject();
        if (response.equals("OK"))
            return (String) in.readObject();
        else
            return "An error has occured: " + response;
    }

    public String sendLoadRequest(String table, String fileName) throws IOException, ClassNotFoundException {
        
        Map<String, Object> data = new HashMap<>();
        data.put("table", table);
        data.put("fileName", fileName);

        Request request = new Request("LOAD_REQUEST", data);

        // Convert the request object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String loadJson = objectMapper.writeValueAsString(request);
        System.out.println(loadJson);

        out.writeObject(loadJson);
        String response = (String) in.readObject();
        if (response.equals("OK")) {
            return (String) in.readObject();
        } else {
            return "An error has occured: " + response;
        }
    }

}


