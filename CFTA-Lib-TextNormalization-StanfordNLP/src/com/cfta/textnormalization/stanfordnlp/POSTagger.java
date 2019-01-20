// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.textnormalization.stanfordnlp;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;

// Does part-of-speech tagging to text, not thread-safe!
public class POSTagger implements SentenceExtractorObserver {

    private List<POSTag> tags = null;

    // POS-tags whole text, not individual sentences -- text can be lemmatized at the same time
    List<POSTag> POSTagText(String text, boolean useNewlineAsSeparator) {
        this.tags = new ArrayList<>();

        SentenceExtractor sExtractor = new SentenceExtractor();
        sExtractor.extractSentences(text, useNewlineAsSeparator, this);

        return tags;
    }

    @Override
    // Handles each sentence, extracts words and POS tags for words. Adds extra tag to signify the end of each sentence
    public void sentenceExtracted(CoreMap sentence) {
        for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
            String word;
            String lemma;

            lemma = token.get(LemmaAnnotation.class);
            word = token.get(TextAnnotation.class);
            String pos = token.get(PartOfSpeechAnnotation.class);

            POSTag tag = new POSTag();
            tag.word = word;
            tag.tag = pos;
            tag.lemma = lemma;
            tags.add(tag);
        }

        POSTag tag = new POSTag();
        tag.word = "\n";
        tag.tag = "";
        tags.add(tag);
    }
}
