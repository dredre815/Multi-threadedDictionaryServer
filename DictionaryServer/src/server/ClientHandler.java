/**
 * ClientHandler.java
 * Author: Marshall Zhang
 * Student ID: 1160040
 * This file is the client handler class that handles client requests.
 * It implements the Runnable interface to run in a separate thread.
 * Key features include:
 * - Read client requests and send responses
 * - Use the Protocol class to handle requests
 * - Close the client socket when done
 */
package server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Dictionary dictionary;
    private BufferedReader reader;
    private BufferedWriter writer;

    public ClientHandler(Socket clientSocket, Dictionary dictionary) {
        this.clientSocket = clientSocket;
        this.dictionary = dictionary;
        // Set up reader and writer for client communication
        try {
            this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            System.err.println("Error setting up client handler: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            String request;
            while ((request = reader.readLine()) != null) {
                // Use the Protocol class to handle the request
                String response = Protocol.handleRequest(request, dictionary);
                writer.write(response);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            System.err.println("Error handling client communication: " + e.getMessage());
        } finally {
            try {
                // Close the reader, writer, and client socket
                reader.close();
                writer.close();
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }
}
