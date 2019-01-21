// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.tn.handlers;

import com.cfta.textnormalization.stanfordnlp.TextLemmatizerObserver;

import java.util.ArrayList;
import java.util.List;

// Stores all lemmatized words
public class TextLemmatizationGatherer implements TextLemmatizerObserver {

    private List<String> tokens = new ArrayList<>();

    @Override
    public void tokenLemmatized(String word) {
        tokens.add(word);
    }

    // Returns all tokens
    List<String> getTokens() {
        return tokens;
    }

    // Clears token list
    void clearTokens() {
        tokens.clear();
    }
}
