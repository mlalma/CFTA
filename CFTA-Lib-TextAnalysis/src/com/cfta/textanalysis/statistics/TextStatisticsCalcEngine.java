// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.textanalysis.statistics;

import com.cfta.ta.handlers.protocol.POSTagResponse;
import com.cfta.ta.handlers.protocol.POSTagResponse.POSTag;
import com.cfta.ta.handlers.protocol.TextStatisticsResponse;

import java.util.List;

// Calculates basic text statistics for article
public class TextStatisticsCalcEngine {

    private final String ENGLISH_LANGUAGE = "en";
    private final EnglishSyllableCounter enSyllableCounter = new EnglishSyllableCounter();

    // Constructor
    public TextStatisticsCalcEngine() {

    }

    // Checks if the word is a word based on its word class
    private boolean isWord(String tag) {
        return (tag.startsWith("NN") || tag.startsWith("VB") || tag.startsWith("JJ") || tag.startsWith("DT") || tag.startsWith("IN") ||
                tag.startsWith("TO") || tag.startsWith("MD") || tag.startsWith("WD") || tag.startsWith("PR") || tag.startsWith("RB") ||
                tag.startsWith("CC") || tag.startsWith("CD") || tag.startsWith("WRB") || tag.startsWith("RP") || tag.startsWith("WP") ||
                tag.startsWith("EX"));
    }

    // Calculates total amount of words in text
    private int calculateTotalWordCount(List<POSTagResponse.POSTag> tags) {
        int wordCount = 0;
        for (POSTagResponse.POSTag t : tags) {
            if (isWord(t.tag)) {
                wordCount++;
            }
        }
        return wordCount;
    }

    // Calculates total amount of syllables in text
    private int calculateTotalEnSyllableCount(List<POSTagResponse.POSTag> tags) {
        int syllableCount = 0;
        for (POSTagResponse.POSTag t : tags) {
            if (isWord(t.tag)) {
                syllableCount += enSyllableCounter.countSyllables(t.word);
            }
        }
        return syllableCount;
    }

    // Calculates total amount of polysyllable words in text
    private int calculateTotalEnPolysyllableCount(List<POSTagResponse.POSTag> tags) {
        int polysyllableCount = 0;
        for (POSTagResponse.POSTag t : tags) {
            if (isWord(t.tag)) {
                if (enSyllableCounter.countSyllables(t.word) >= 3) {
                    polysyllableCount++;
                }
            }
        }
        return polysyllableCount;
    }

    // Calculates total amount of sentences in text
    private int calculateTotalSentenceNum(List<POSTagResponse.POSTag> tags) {
        int sentenceCount = 0;
        for (POSTagResponse.POSTag t : tags) {
            if (t.word.equalsIgnoreCase("\n")) {
                sentenceCount++;
            }
        }
        return sentenceCount;
    }

    // Calculates basic text statistics for English text
    private TextStatisticsResponse calcEnglishTextStats(List<POSTag> tags) {
        int sentences = calculateTotalSentenceNum(tags);
        int words = calculateTotalWordCount(tags);
        int syllables = calculateTotalEnSyllableCount(tags);
        int totalPolysyllableCount = calculateTotalEnPolysyllableCount(tags);

        // Uses standard Flesch and Flesch-Kincaid readability formulas to calculate relative "complexity" of the text
        double flesch = 206.835 - 1.015 * ((double) words / (double) sentences) - 84.6 * ((double) syllables / (double) words);
        double fleschKincaid = 0.39 * ((double) words / (double) sentences) + 11.8 * ((double) syllables / (double) words) - 15.59;

        TextStatisticsResponse response = new TextStatisticsResponse();

        response.wordCount = words;
        response.polysyllableWordCount = totalPolysyllableCount;
        response.totalSyllableCount = syllables;
        response.sentenceCount = sentences;
        response.fleschReadabilityRating = flesch;
        response.fleschKincaidReadaiblityRating = fleschKincaid;

        return response;
    }

    // Calculates text statistics
    public TextStatisticsResponse calcTextStatistics(List<POSTag> tags, String language) {
        if (language.equalsIgnoreCase(ENGLISH_LANGUAGE)) {
            return calcEnglishTextStats(tags);
        } else {
            return new TextStatisticsResponse();
        }
    }
}
