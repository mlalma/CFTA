// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.textanalysis.textrank;

import java.util.HashMap;

// Single text rank node, can be used for keyword filtering and summarization
public class TextRankNode {
    public double rank = 1.0;
    public double newRank = 1.0;
    public double normalizedRank = 0.0;
    
    public HashMap<TextRankNode, Double> edges = new HashMap<>();    
    
    // Sets undirected edge between two nodes with given weight
    public void setEdge(TextRankNode otherWord, double weight) {
        this.edges.put(otherWord, weight);
        otherWord.edges.put(this, weight);
    }    
}
