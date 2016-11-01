// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.textnormalization.stanfordnlp;

import java.util.ArrayList;
import java.util.List;

// Extracts named entities from text
public class NamedEntityExtractor {
    
    // Extracts named entities from text block
    public List<NamedEntity> extractEntities(String text, boolean useNewlineAsSeparator) {
        List<NamedEntity> entities = new ArrayList<>();
        List<String> entityText = new ArrayList<>();
        
        POSTagger tagger = new POSTagger();
        List<POSTag> tags = tagger.POSTagText(text, useNewlineAsSeparator);
        
        boolean NNP = false;
        String curEntity = "";
        
        for (POSTag t : tags) {
            if (t.tag.startsWith("NNP")) {
                if (NNP) {
                    curEntity = curEntity + "_" + t.word;
                } else {
                    curEntity = t.word;
                    NNP = true;
                }                    
            } else {
                if (NNP) {
                    boolean found = false;
                    /*
                    // Try to do too-simple and error-prone coreference resolution
                    for (String s : NNPs) {
                        if (s.endsWith(curWord)) {
                            curWord = s;
                            found = true;
                            break;
                        }
                    }
                    */

                    for (NamedEntity e : entities) {
                        if (e.name.equalsIgnoreCase(curEntity)) {
                            e.count++;
                            found = true;
                            break;
                        }
                    }
                    
                    if (!found) {
                        NamedEntity e = new NamedEntity();
                        e.count = 1;
                        e.name = curEntity;
                        entities.add(e);
                    }

                    entityText.add(text);
                    NNP = false;
                    curEntity = "";
                }
            }
        }
        
        return entities;
    }
}
