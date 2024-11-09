/**
 * DictionaryServer.java
 * Author: Marshall Zhang
 * Student ID: 1160040
 * This file is the main server class that starts the dictionary server and handles GUI interactions.
 * Key features include:
 * - Start and stop the server
 * - Use a thread pool to handle client connections
 * - Update server status in the GUI
 * - Handle command line arguments for port and dictionary file path
 */

package server;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DictionaryServer {
    private ServerSocket serverSocket;
    private int port;
    private Dictionary dictionary;
    private CustomThreadPool threadPool;
    private volatile boolean isStopped = false;
    // Thread pool size
    private static final int NUMBER_OF_THREADS = 16;

    public DictionaryServer(int port, String dictionaryFilePath) {
        this.port = port;
        this.dictionary = new Dictionary(dictionaryFilePath);
        this.threadPool = new CustomThreadPool(NUMBER_OF_THREADS);
    }

    public void startServer() {
        try {
            // Start the server socket
            this.serverSocket = new ServerSocket(this.port);
            SwingUtilities.invokeLater(() -> ServerGUI.updateStatus("Running"));
            System.out.println("Dictionary Server is running on port " + this.port);

            // Accept client connections and handle them in separate threads
            while (!isStopped) {
                try {
                    Socket clientSocket = this.serverSocket.accept();
                    this.threadPool.execute(new ClientHandler(clientSocket, dictionary));
                    System.out.println("Connected to client: " + clientSocket.getRemoteSocketAddress());
                } catch (Exception e) {
                    if (isStopped) {
                        System.out.println("Server has been stopped.");
                        break;
                    } else {
                        throw new RuntimeException("Error accepting client connection", e);
                    }
                }
            }
            this.threadPool.shutdown();
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + port, e);
        } finally {
            stopServer();
        }
    }

    public synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stopServer() {
        this.isStopped = true;
        try {
            if (this.serverSocket != null) {
                this.serverSocket.close();
            }
            // Update server status in GUI
            SwingUtilities.invokeLater(() -> ServerGUI.updateStatus("Stopped"));
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar DictionaryServer.jar <port> <dictionary-file>");
            System.exit(1);
        }

        // Check if the dictionary file exists, if not, create an empty file
        try {
            File dictionaryFile = new File(args[1]);
            if (!dictionaryFile.exists()) {
                dictionaryFile.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Error creating dictionary file: " + e.getMessage());
        }

        // Start the server GUI
        SwingUtilities.invokeLater(() -> new ServerGUI(args));
    }

    /**
     * ServerGUI class to create the server control panel GUI.
     * It allows the user to start and stop the server, and displays the server status.
     */
    static class ServerGUI extends JFrame {
        private static JButton startStopButton;
        private static JTextField portField, dictionaryFilePathField;
        private static JLabel statusLabel;
        private static DictionaryServer server;
        private static Thread serverThread;

        ServerGUI(String[] args) {
            super("Dictionary Server Control Panel");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(300, 150);
            setLayout(new GridLayout(0, 2));

            add(new JLabel(" Port:"));
            portField = new JTextField(args[0]);
            add(portField);

            add(new JLabel(" Dictionary File Path:"));
            dictionaryFilePathField = new JTextField(args[1]);
            add(dictionaryFilePathField);

            add(new JLabel(" Status:"));
            statusLabel = new JLabel("Stopped");
            add(statusLabel);

            startStopButton = new JButton("Start Server");
            startStopButton.addActionListener(e -> toggleServer());
            add(startStopButton);

            setLocationRelativeTo(null);
            setVisible(true);
        }

        private void toggleServer() {
            if (server == null || server.isStopped()) {
                int port = Integer.parseInt(portField.getText());
                String dictionaryFilePath = dictionaryFilePathField.getText();
                server = new DictionaryServer(port, dictionaryFilePath);
                serverThread = new Thread(server::startServer);
                serverThread.start();
                updateStatus("Running");
            } else {
                new Thread(() -> {
                    server.stopServer();
                    try {
                        serverThread.join(); // Wait for server thread to terminate
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    updateStatus("Stopped");
                }).start();
            }
        }

        static void updateStatus(String status) {
            statusLabel.setText(status);
            startStopButton.setText(status.equals("Running") ? "Stop Server" : "Start Server");
        }
    }
}
