// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.textanalysis.textrank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// TextRank word node
public class TextRankWordNode extends TextRankNode {
    int posOnText = -1;
    List<Integer> allPositions = new ArrayList<>();
    boolean multipart = false;
    String treeBankClass = "";
    String word = "";
    String lemma = "";
    HashMap<Integer, String> possessiveAdd = new HashMap<>();

    boolean compoundEnd = false;

    boolean isAdjective() {
        return treeBankClass.startsWith("JJ");
    }

    boolean isNoun() {
        return treeBankClass.startsWith("NN");
    }

    public boolean isVerb() {
        return treeBankClass.startsWith("VB");
    }
}
