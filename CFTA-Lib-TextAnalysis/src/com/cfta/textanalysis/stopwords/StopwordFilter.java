// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.textanalysis.stopwords;

import com.cfta.cf.util.CFTASettings;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

// Simple stopword filter class
public class StopwordFilter {

    private HashSet<String> stopwordList= new HashSet<>();
    private String langCode = "";
    
    // Constructor
    public StopwordFilter(String langCode) {
        this.langCode = langCode;
    }
    
    // Loads stopword list to hash set
    public void loadStopwordlist(String filename) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = br.readLine()) != null) {
                stopwordList.add(line.trim().toLowerCase());                            
            }
        } catch (Exception ex) {
            if (CFTASettings.getDebug()) {
                ex.printStackTrace();
            }
        } finally {
            if (br != null) {
                try { br.close(); } catch (Exception ex) {}
            }
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
