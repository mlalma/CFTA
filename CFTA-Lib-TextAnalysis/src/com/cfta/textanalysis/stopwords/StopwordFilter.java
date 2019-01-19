// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.textanalysis.stopwords;

import com.cfta.cf.util.CFTASettings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

// Simple stopword filter class
public class StopwordFilter {

    private HashSet<String> stopwordList = new HashSet<>();
    private String langCode;

    // Constructor
    StopwordFilter(String langCode) {
        this.langCode = langCode;
    }

    // Loads stopword list to hash set
    void loadStopwordlist(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                stopwordList.add(line.trim().toLowerCase());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Returns language code of the stopword list
    public String getLangCode() {
        return langCode;
    }

    // Checks if the given word is a stopword
    public boolean isStopword(String word) {
        return stopwordList.contains(word.trim().toLowerCase());
    }
}
