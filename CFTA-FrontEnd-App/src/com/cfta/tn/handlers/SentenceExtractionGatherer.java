// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.tn.handlers;

import com.cfta.textnormalization.stanfordnlp.SentenceExtractorObserver;
import java.util.ArrayList;
import java.util.List;

// Gathers extracted sentences to a simple list
public class SentenceExtractionGatherer implements SentenceExtractorObserver {

    private List<String> sentences = new ArrayList<>();
    
    @Override
    // Adds new sentence to list
    public void sentenceExtracted(edu.stanford.nlp.util.CoreMap sentence) {
        sentences.add(sentence.toString());
    } 
    
    // Returns list
    public List<String> getSentences() {
        return sentences;
    }
}
