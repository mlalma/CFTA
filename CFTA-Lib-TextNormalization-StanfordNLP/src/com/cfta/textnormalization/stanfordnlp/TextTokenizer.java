// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.textnormalization.stanfordnlp;

import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;

import java.util.List;

// Tokenizes given text, i.e. splits it to elements
class TextTokenizer {

    // Tokenizes text
    void tokenizeText(String text, TextTokenizerObserver observer) {
        Annotation document = new Annotation(text);
        StanfordNLPFactory.getEngineInstance().annotate(document);

        List<CoreLabel> labels = document.get(TokensAnnotation.class);
        for (CoreLabel l : labels) {
            observer.tokenExtracted(l);
        }
    }
}
