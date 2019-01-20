// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.textnormalization.stanfordnlp;

import edu.stanford.nlp.ling.CoreLabel;

// Observer interface for receiving extracted tokens
public interface TextTokenizerObserver {
    void tokenExtracted(CoreLabel token);
}
