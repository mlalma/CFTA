// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.textnormalization.stanfordnlp;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

// Text lemmatizer, not thread-safe!
public class TextLemmatizer implements TextTokenizerObserver {
    
    private final StanfordCoreNLP engine = StanfordNLPFactory.getEngineInstance();
    private TextLemmatizerObserver observer = null;

    @Override
    // After a token is extracted, lemmatize it and pass it to observer
    public void tokenExtracted(CoreLabel token) {
        String lemma = token.get(LemmaAnnotation.class);
        observer.tokenLemmatized(lemma);
    }
    
    // Lemmatizes the text
    public void lemmatizeText(String text, TextLemmatizerObserver observer) {
        TextTokenizer tokenizer = new TextTokenizer();
        this.observer = observer;
        tokenizer.tokenizeText(text, this);
    }
}
