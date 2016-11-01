// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.ta.handlers.protocol;

import java.util.ArrayList;
import java.util.List;

// Part-of-speech tags
public class POSTagResponse {
    
    // Uses Penn Treebank tags to tag words
    public class POSTag {
        public String word;
        public String lemma;
        public String tag;
    }
    
    public static final int RESPONSE_OK = 0;
    public static final int RESPONSE_FAIL = -1;
    
    public int errorCode = RESPONSE_OK;    
    public List<POSTag> tags = new ArrayList<>();
    
    public POSTag newTag() { return new POSTag(); }
}
