/**
 * Dictionary.java
 * Author: Marshall Zhang
 * Student ID: 1160040
 * This file defines four methods to interact with the dictionary data.
 * Key features include:
 * - Get meanings of a word
 * - Add a new word with meanings
 * - Remove a word
 * - Update meanings of a word
 */
package server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Dictionary {
    private Map<String, List<String>> wordMeanings;
    private final String dictionaryFilePath;

    public Dictionary(String dictionaryFilePath) {
        this.dictionaryFilePath = dictionaryFilePath;
        this.wordMeanings = DictionaryFileHandler.loadDictionary(dictionaryFilePath);
    }

    // Get meanings of a word
    public synchronized List<String> getMeanings(String word) {
        return wordMeanings.get(word);
    }

    // Add a new word with meanings
    public synchronized boolean addWord(String word, List<String> meanings) {
        if (wordMeanings.containsKey(word)) {
            return false;
        }
        // Use HashSet to remove duplicates
        wordMeanings.put(word, new ArrayList<>(new HashSet<>(meanings)));
        DictionaryFileHandler.saveDictionary(wordMeanings, dictionaryFilePath);
        return true;
    }

    // Remove a word
    public synchronized boolean removeWord(String word) {
        if (!wordMeanings.containsKey(word)) {
            return false;
        }
        wordMeanings.remove(word);
        DictionaryFileHandler.saveDictionary(wordMeanings, dictionaryFilePath);
        return true;
    }

    // Update meanings of a word
    public synchronized boolean updateMeanings(String word, List<String> newMeanings) {
        if (!wordMeanings.containsKey(word)) {
            return false;
        }
        List<String> existingMeanings = wordMeanings.get(word);
        // Only add new meanings
        for (String meaning : newMeanings) {
            if (!existingMeanings.contains(meaning)) {
                existingMeanings.add(meaning);
            }
        }
        DictionaryFileHandler.saveDictionary(wordMeanings, dictionaryFilePath);
        return true;
    }
}
