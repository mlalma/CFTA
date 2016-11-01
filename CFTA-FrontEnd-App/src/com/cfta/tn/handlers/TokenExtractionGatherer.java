// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.tn.handlers;

import com.cfta.textnormalization.stanfordnlp.TextTokenizerObserver;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import java.util.ArrayList;
import java.util.List;

// Gathers tokens to a single list
public class TokenExtractionGatherer implements TextTokenizerObserver {

    private List<String> tokens = new ArrayList<>();
    
    @Override
    // Token extraction observer
    public void tokenExtracted(CoreLabel token) {
        tokens.add(token.get(CoreAnnotations.TextAnnotation.class));
    }
    
    // Returns all tokens
    public List<String> getTokens() {
        return tokens;
    }
    
    // Clears token list
    public void clearTokens() {
        tokens.clear();
    }
}
