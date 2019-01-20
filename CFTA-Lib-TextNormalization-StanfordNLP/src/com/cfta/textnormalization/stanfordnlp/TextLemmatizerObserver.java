// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.textnormalization.stanfordnlp;

// Text lemmatization observer
public interface TextLemmatizerObserver {
    void tokenLemmatized(String word);
}
