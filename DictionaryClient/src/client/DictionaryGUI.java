/**
 * DictionaryGUI.java
 * Author: Marshall Zhang
 * Student ID: 1160040
 * This file is the GUI class that displays the dictionary client interface and handles user input.
 * Key features include:
 * - Four buttons for querying, adding, removing, and updating words
 * - Text fields for word and meanings input
 * - Text area for displaying server responses and errors
 */
package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DictionaryGUI {
    private DictionaryClient client;
    private JFrame frame;
    private JTextField wordTextField;
    private JTextArea meaningsTextArea;
    private JTextArea responseTextArea;

    public DictionaryGUI(DictionaryClient client) {
        this.client = client;
        initializeGUI();
    }

    private void initializeGUI() {
        // Frame setup
        frame = new JFrame("Dictionary Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(5, 5));

        // North Panel for Input
        JPanel northPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        frame.add(northPanel, BorderLayout.NORTH);

        northPanel.add(new JLabel("Word:"));
        wordTextField = new JTextField();
        northPanel.add(wordTextField);

        northPanel.add(new JLabel("Meanings (semicolon separated):"));
        meaningsTextArea = new JTextArea(3, 20);
        northPanel.add(new JScrollPane(meaningsTextArea));

        // Center Panel for Response
        responseTextArea = new JTextArea();
        responseTextArea.setEditable(false);
        frame.add(new JScrollPane(responseTextArea), BorderLayout.CENTER);

        // South Panel for Buttons
        JPanel southPanel = new JPanel();
        frame.add(southPanel, BorderLayout.SOUTH);

        JButton queryButton = new JButton("Query");
        queryButton.addActionListener(this::queryAction);
        southPanel.add(queryButton);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(this::addAction);
        southPanel.add(addButton);

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(this::removeAction);
        southPanel.add(removeButton);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(this::updateAction);
        southPanel.add(updateButton);

        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    public void display() {
        frame.setVisible(true);
    }

    private void queryAction(ActionEvent e) {
        client.queryWord(wordTextField.getText());
        displayResponse();
    }

    private void addAction(ActionEvent e) {
        String word = wordTextField.getText();
        String[] meanings = meaningsTextArea.getText().split(";");
        client.addWord(word, meanings);
        displayResponse();
    }

    private void removeAction(ActionEvent e) {
        client.removeWord(wordTextField.getText());
        displayResponse();
    }

    private void updateAction(ActionEvent e) {
        String word = wordTextField.getText();
        String[] meanings = meaningsTextArea.getText().split(";");
        client.updateWord(word, meanings);
        displayResponse();
    }

    private void displayResponse() {
        String response = client.getResponse();
        responseTextArea.setText(response);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
