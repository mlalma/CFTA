// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.textanalysis.textrank;

import com.cfta.ta.handlers.protocol.TextSummarizationResponse;
import com.cfta.ta.handlers.protocol.TextSummarizationResponse.SummarySentence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// Summarizes text using TextRank algorithm
public class TextRankSummarizer {

    // Calculates sentence "similarity" between two sentence nodes
    private void calculateSentenceSimilarity(TextRankGraph g) {
        for (int i = 0; i < g.graph.size(); i++) {
            TextRankSentenceNode n = (TextRankSentenceNode) g.graph.get(i);
            for (int j = i + 1; j < g.graph.size(); j++) {
                TextRankSentenceNode n2 = (TextRankSentenceNode) g.graph.get(j);

                // Calculate number of similar words in sentences
                double wCount = 0.0;
                for (String s : n.normalizedString) {
                    for (String s2 : n2.normalizedString) {
                        if (s.equalsIgnoreCase(s2)) {
                            wCount += 1.0;
                            break;  // one word can only signify for one point, repetition doesn't help
                        }
                    }
                }

                // Calculate weight for the sentences
                double weight = wCount / (Math.log((double) n.normalizedString.size()) + Math.log((double) n2.normalizedString.size()));
                if (weight > 0.00001) {
                    n.setEdge(n2, weight);
                }
            }
        }
    }

    // Sorts sentence candidates
    private void sortSentenceCandidates(TextRankGraph g) {
        g.graph.sort((o1, o2) -> -Double.compare(o1.rank, o2.rank) );
    }

    // Sorts the sentence candidates by position
    private void sortSentenceCandidatesByPosition(List<TextRankSentenceNode> summary) {
        summary.sort( (p1, p2) -> -Integer.compare(p1.position, p2.position));
    }

    // Summarizes all sentences
    private TextSummarizationResponse summarizeAllSentences(TextRankGraph sentences) {
        TextSummarizationResponse response = new TextSummarizationResponse();
        for (int i = 0; i < sentences.graph.size(); i++) {
            TextRankSentenceNode node = (TextRankSentenceNode) sentences.graph.get(i);
            SummarySentence sentence = response.newSentence();
            sentence.position = node.position;
            sentence.rank = node.rank;
            sentence.text = node.originalString;
            response.sentences.add(sentence);
        }
        return response;
    }

    // Summarizes text to a defined number of sentences
    private TextSummarizationResponse summarizeToNumOfSentences(TextRankGraph sentences, int numOfSentences) {
        TextSummarizationResponse response = new TextSummarizationResponse();
        ArrayList<TextRankSentenceNode> summary = new ArrayList<>();
        for (int i = 0; i < Math.min(numOfSentences, sentences.graph.size()); i++) {
            summary.add((TextRankSentenceNode) sentences.graph.get(i));
        }
        sortSentenceCandidatesByPosition(summary);
        for (TextRankSentenceNode node : summary) {
            SummarySentence sentence = response.newSentence();
            sentence.position = node.position;
            sentence.rank = node.rank;
            sentence.text = node.originalString;
            response.sentences.add(sentence);
        }
        return response;
    }

    // Summarizes to word count (approximately, goes usually above)
    private TextSummarizationResponse summarizeToWordCount(TextRankGraph sentences, int numOfWords) {
        TextSummarizationResponse response = new TextSummarizationResponse();
        ArrayList<TextRankSentenceNode> summary = new ArrayList<>();
        int wordCount = 0;
        for (int i = 0; i < sentences.graph.size(); i++) {
            TextRankSentenceNode node = (TextRankSentenceNode) sentences.graph.get(i);
            summary.add((TextRankSentenceNode) sentences.graph.get(i));
            wordCount += node.originalString.split(" ").length;
            if (wordCount >= numOfWords) {
                break;
            }
        }

        sortSentenceCandidatesByPosition(summary);
        for (TextRankSentenceNode node : summary) {
            SummarySentence sentence = response.newSentence();
            sentence.position = node.position;
            sentence.rank = node.rank;
            sentence.text = node.originalString;
            response.sentences.add(sentence);
        }

        return response;
    }

    // Summarizes the text by using percentage threshold of total text
    private TextSummarizationResponse summarizeToPercentageOfText(TextRankGraph sentences, double textThreshold) {
        TextSummarizationResponse response = new TextSummarizationResponse();
        int totalWordCount = 0;
        for (int i = 0; i < sentences.graph.size(); i++) {
            TextRankSentenceNode node = (TextRankSentenceNode) sentences.graph.get(i);
            totalWordCount += node.originalString.split(" ").length;
        }

        ArrayList<TextRankSentenceNode> summary = new ArrayList<>();
        int wordCount = 0;
        for (int i = 0; i < sentences.graph.size(); i++) {
            TextRankSentenceNode node = (TextRankSentenceNode) sentences.graph.get(i);
            summary.add((TextRankSentenceNode) sentences.graph.get(i));
            wordCount += node.originalString.split(" ").length;
            if ((double) wordCount / (double) totalWordCount >= textThreshold) {
                break;
            }
        }
        sortSentenceCandidatesByPosition(summary);

        for (TextRankSentenceNode node : summary) {
            SummarySentence sentence = response.newSentence();
            sentence.position = node.position;
            sentence.rank = node.rank;
            sentence.text = node.originalString;
            response.sentences.add(sentence);
        }
        return response;
    }

    // Summarizes the text using TextRank algorithm
    public TextSummarizationResponse summarize(TextRankGraph sentences, boolean removeHeaderFirstSentence, TextRankSummarizationMode summarizationMode) {
        calculateSentenceSimilarity(sentences);
        sentences.doPageRank();
        sortSentenceCandidates(sentences);

        if (removeHeaderFirstSentence) {
            for (int i = 0; i < sentences.graph.size(); i++) {
                TextRankSentenceNode sentence = (TextRankSentenceNode) sentences.graph.get(i);
                if (sentence.position == 0) {
                    sentences.graph.remove(i);
                    break;
                }
            }
        }

        TextSummarizationResponse response;
        if (summarizationMode.allSentences) {
            response = summarizeAllSentences(sentences);
        } else if (summarizationMode.numOfSentences > 0) {
            response = summarizeToNumOfSentences(sentences, summarizationMode.numOfSentences);
        } else if (summarizationMode.approxNumOfWords > 0) {
            response = summarizeToWordCount(sentences, summarizationMode.approxNumOfWords);
        } else if (summarizationMode.approxPercentageOfText > 0.0) {
            response = summarizeToPercentageOfText(sentences, summarizationMode.approxPercentageOfText);
        } else {
            throw new RuntimeException("Invalid summarization response mode");
        }

        return response;
    }
}
