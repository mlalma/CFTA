// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.textnormalization.stanfordnlp;

// Text lemmatization observer
public interface TextLemmatizerObserver {
    public void tokenLemmatized(String word);
}
