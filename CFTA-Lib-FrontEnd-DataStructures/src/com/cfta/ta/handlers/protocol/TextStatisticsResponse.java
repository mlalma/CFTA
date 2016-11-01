// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.ta.handlers.protocol;

// Text statistics
public class TextStatisticsResponse {
    public static final int RESPONSE_OK = 0;
    public static final int RESPONSE_FAIL = -1;
    
    public int errorCode = RESPONSE_OK;  
    
    public int wordCount = -1;
    public int polysyllableWordCount = -1;
    public int sentenceCount = -1;
    public int totalSyllableCount = -1;
    
    public double fleschReadabilityRating = -100.0;
    public double fleschKincaidReadaiblityRating = -100.0;
}
