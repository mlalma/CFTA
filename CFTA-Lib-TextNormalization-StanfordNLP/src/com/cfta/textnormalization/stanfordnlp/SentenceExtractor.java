// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.textnormalization.stanfordnlp;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;

// Extracts sentences from text
public class SentenceExtractor {

    // Extract sentences, can use new line as a separation to recognize better extracted text with headers and mid-quotes
    public void extractSentences(String text, boolean useNewlineAsSeparation, SentenceExtractorObserver observer) {
        ArrayList<String> textFragments = new ArrayList<>();

        if (!useNewlineAsSeparation) {
            textFragments.add(text);
        } else {
            String[] parts = text.split("\n");
            for (String s : parts) {
                if (s.trim().length() > 0) {
                    textFragments.add(s);
                }
            }
        }

        for (String textPart : textFragments) {
            Annotation document = new Annotation(textPart);
            StanfordNLPFactory.getEngineInstance().annotate(document);
            List<CoreMap> sentenceFrags = document.get(SentencesAnnotation.class);
            for (CoreMap sentence : sentenceFrags) {
                observer.sentenceExtracted(sentence);
            }
        }
    }
}
