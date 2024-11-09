/**
 * DictionaryFileHandler.java
 * Author: Marshall Zhang
 * Student ID: 1160040
 * This file is the file handler class that reads and writes dictionary data to a file.
 * Key features include:
 * - Load dictionary data from file
 * - Write dictionary data to file
 */
package server;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DictionaryFileHandler {
    // Load dictionary data from file
    public static Map<String, List<String>> loadDictionary(String dictionaryFilePath) {
        Map<String, List<String>> dictionary = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dictionaryFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    String word = parts[0];
                    List<String> meanings = Arrays.asList(parts[1].split(";"));
                    dictionary.put(word, meanings);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading dictionary file: " + e.getMessage());
        }
        return dictionary;
    }

    // Write dictionary data to file
    public static void saveDictionary(Map<String, List<String>> dictionary, String dictionaryFilePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dictionaryFilePath))) {
            for (Map.Entry<String, List<String>> entry : dictionary.entrySet()) {
                String line = entry.getKey() + ":" + String.join(";", entry.getValue());
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing dictionary file: " + e.getMessage());
        }
    }
}
