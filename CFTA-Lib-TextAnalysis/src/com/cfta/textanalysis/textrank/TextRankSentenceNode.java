// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.textanalysis.textrank;

import java.util.ArrayList;
import java.util.List;

// Sentence node for text summarization algorithm
public class TextRankSentenceNode extends TextRankNode {
    public int position = -1;
    public String originalString = "";
    public int originalStringLength = -1;
    public List<String> normalizedString = new ArrayList<>();
}
