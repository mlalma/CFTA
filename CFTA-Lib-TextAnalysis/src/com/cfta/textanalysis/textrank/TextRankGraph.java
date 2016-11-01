// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.textanalysis.textrank;

import java.util.ArrayList;
import java.util.List;

// Text rank graph class
public class TextRankGraph {
    public final int MAX_ITERATIONS = 1000;
    public final double TEXTRANK_DAMPING_FACTOR = 0.85;
    public final double STANDARD_ERROR_THRESHOLD = 0.001;
    
    public List<TextRankNode> graph = new ArrayList<>();
        
    // Runs the page rank algorithm until it converges (error drops below the threshold) or maximum amount of iterations is run
    public void doPageRank() {
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            double err = 0.0;
            for (TextRankNode n : graph) {
		double rank = 0.0;
		for (TextRankNode e : n.edges.keySet()) {                    
                    double totalOutRankValue = 0.0;
                    for (TextRankNode e2 : e.edges.keySet()) {
                        totalOutRankValue += e.edges.get(e2);
                    }                                        
		    rank += e.rank * (n.edges.get(e)/totalOutRankValue);
		}

		rank *= TEXTRANK_DAMPING_FACTOR;
		rank += 1.0 - TEXTRANK_DAMPING_FACTOR;

		n.newRank = rank;
                err += Math.abs(n.rank - rank);
	    }
            
            for (TextRankNode w : graph) {
                w.rank = w.newRank;
            }

            if (err < STANDARD_ERROR_THRESHOLD) {	
                break;
	    }
        }
    }
}
