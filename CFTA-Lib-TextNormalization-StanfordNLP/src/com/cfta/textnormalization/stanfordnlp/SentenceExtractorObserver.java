// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.textnormalization.stanfordnlp;

import edu.stanford.nlp.util.CoreMap;

// Observer interface for receiving extracted sentences
public interface SentenceExtractorObserver {
    public void sentenceExtracted(CoreMap sentence);
}
