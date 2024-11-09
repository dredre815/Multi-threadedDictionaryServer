/**
 * Protocol.java
 * Author: Marshall Zhang
 * Student ID: 1160040
 * This file defines the protocol class that handles client requests.
 * Key features include:
 * - Query a word
 * - Add a new word with meanings
 * - Remove a word
 * - Update meanings of a word
 */
package server;

import java.util.Arrays;
import java.util.List;

public class Protocol {
    public static String handleRequest(String request, Dictionary dictionary) {
        String[] parts = request.split(":", 3);
        String action = parts[0];
        String word = parts.length > 1 ? parts[1] : "";
        List<String> meanings = parts.length > 2 ? Arrays.asList(parts[2].split(";")) : null;

        switch (action) {
            case "query":
                List<String> foundMeanings = dictionary.getMeanings(word);
                if (foundMeanings == null || foundMeanings.isEmpty()) {
                    return "Error: Word not found";
                }
                return "Found: " + String.join(";", foundMeanings);
            case "add":
                if (meanings == null || meanings.isEmpty() || meanings.contains("")) {
                    return "Error: No meanings provided";
                }
                boolean added = dictionary.addWord(word, meanings);
                return added ? "Success: Word added" : "Error: Word already exists";
            case "remove":
                boolean removed = dictionary.removeWord(word);
                return removed ? "Success: Word removed" : "Error: Word not found";
            case "update":
                List<String> checkMeanings = dictionary.getMeanings(word);
                if (checkMeanings == null || checkMeanings.isEmpty()) {
                    return "Error: Word not found";
                }
                if (meanings == null || meanings.isEmpty() || meanings.contains("")) {
                    return "Error: No new meanings provided";
                }
                for (String meaning : meanings) {
                    if (checkMeanings.contains(meaning)) {
                        return "Error: Meaning already exists";
                    }
                }
                boolean updated = dictionary.updateMeanings(word, meanings);
                return updated ? "Success: Word updated" : "Error: Word not found";
            default:
                return "Error: Invalid action";
        }
    }
}
