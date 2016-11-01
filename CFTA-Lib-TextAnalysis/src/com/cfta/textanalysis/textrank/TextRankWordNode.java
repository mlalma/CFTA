// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.textanalysis.textrank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// TextRank word node
public class TextRankWordNode extends TextRankNode {   
    public int posOnText = -1;
    public List<Integer> allPositions = new ArrayList<>();
    public boolean multipart = false;
    public String treeBankClass = "";
    public String word = "";
    public String lemma = "";    
    public HashMap<Integer, String> possessiveAdd = new HashMap<>();
    
    public boolean compoundEnd = false;
        
    public boolean isAdjective() { return treeBankClass.startsWith("JJ"); }
    public boolean isNoun() { return treeBankClass.startsWith("NN"); }
    public boolean isVerb() { return treeBankClass.startsWith("VB"); }
}
