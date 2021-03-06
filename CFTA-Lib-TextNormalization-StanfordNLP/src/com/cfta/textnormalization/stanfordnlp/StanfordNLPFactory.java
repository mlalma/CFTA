// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.textnormalization.stanfordnlp;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.Properties;

// Factory class to return the Stanford Core NLP class
class StanfordNLPFactory {

    private static StanfordCoreNLP engine = null;

    // Private constructor, this class can never be instantiated
    private StanfordNLPFactory() {
    }

    // StanfordCoreNLP instance is thread-safe
    static StanfordCoreNLP getEngineInstance() {
        if (engine == null) {
            Properties props = new Properties();
            props.put("annotators", "tokenize, ssplit, pos, lemma");
            engine = new StanfordCoreNLP(props);
        }

        return engine;
    }
}
