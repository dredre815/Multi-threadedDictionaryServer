/**
 * DictionaryClient.java
 * Author: Marshall Zhang
 * Student ID: 1160040
 * This file is the client class that connects to the server and sends requests.
 * Key features include:
 * - Connect to the server
 * - Send requests to the server
 * - Get responses from the server
 * - Handle different actions (query, add, remove, update)
 */
package client;

import java.io.*;
import java.net.Socket;

public class DictionaryClient {
    private String serverAddress;
    private int serverPort;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private DictionaryGUI gui;

    public DictionaryClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void startClient() {
        try {
            socket = new Socket(serverAddress, serverPort);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            gui = new DictionaryGUI(this);
            gui.display();
        } catch (IOException e) {
            System.err.println("Client exception: " + e.getMessage());
            if (gui != null) {
                gui.showError("Could not connect to the server. Please check the server address and port.");
            }
        }
    }

    public void sendRequest(String action, String word, String[] meanings) {
        try {
            String request = action + ":" + word;
            if (meanings != null && meanings.length > 0) {
                request += ":" + String.join(";", meanings);
            }
            writer.write(request);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            if (gui != null) {
                gui.showError("Error sending request: " + e.getMessage());
            }
        }
    }

    public String getResponse() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            if (gui != null) {
                gui.showError("Error getting response: " + e.getMessage());
            }
            return null;
        }
    }

    // Helper methods for different actions
    public void queryWord(String word) {
        sendRequest("query", word, null);
    }

    public void addWord(String word, String[] meanings) {
        sendRequest("add", word, meanings);
    }

    public void removeWord(String word) {
        sendRequest("remove", word, null);
    }

    public void updateWord(String word, String[] meanings) {
        sendRequest("update", word, meanings);
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar DictionaryClient.jar <server-address> <server-port>");
            System.exit(1);
        }

        String serverAddress = args[0];
        int serverPort = Integer.parseInt(args[1]);
        DictionaryClient client = new DictionaryClient(serverAddress, serverPort);
        client.startClient();
    }
}
