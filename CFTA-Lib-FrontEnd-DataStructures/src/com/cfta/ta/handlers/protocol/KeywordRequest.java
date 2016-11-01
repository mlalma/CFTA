// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.ta.handlers.protocol;

// Request for finding keywords from text
public class KeywordRequest {    
    public String url = "";
    
    public String title = "";
    public String text = "";
    
    public boolean returnAllKeywords = false;
    public int maxAmountOfKeywords = 5;
}
