// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.textanalysis.textrank;

import java.util.HashMap;

// Single text rank node, can be used for keyword filtering and summarization
class TextRankNode {
    double rank = 1.0;
    double newRank = 1.0;
    double normalizedRank = 0.0;

    HashMap<TextRankNode, Double> edges = new HashMap<>();

    // Sets undirected edge between two nodes with given weight
    void setEdge(TextRankNode otherWord, double weight) {
        this.edges.put(otherWord, weight);
        otherWord.edges.put(this, weight);
    }
}
