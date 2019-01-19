// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.ta.handlers.protocol;

import java.util.ArrayList;
import java.util.List;

// Returns parsed keywords
public class KeywordResponse {

    // Single keyword
    public class Keyword {
        public String word = "";
        public double value = 0.0;
        public double normalizedValue = 0.0;
    }

    // Summary statistics for keyword values
    public double keywordAvgValue = 0.0;
    public double keywordStdDev = 0.0;

    public static final int RESPONSE_OK = 0;
    public static final int RESPONSE_FAIL = -1;

    public int errorCode = RESPONSE_OK;

    public List<Keyword> keywords = new ArrayList<>();

    public Keyword newKeyword() {
        return new Keyword();
    }
}
