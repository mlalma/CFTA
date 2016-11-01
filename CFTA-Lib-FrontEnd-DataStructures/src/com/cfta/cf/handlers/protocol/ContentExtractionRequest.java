// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2014
package com.cfta.cf.handlers.protocol;

// Request for extracting content from a web page - Stick either url or the page html in
public class ContentExtractionRequest {    
    public static final String BOILERPIPE_ALGORITHM = "boilerpipe";
    public static final String XTRACT_ALGORITHM = "xtract";
    
    public String url = "";
    public String html = "";    
    
    public boolean extractHeader = true;
    public boolean extractText = true;
    public boolean extractMainImage = false;
    
    public String extractionAlgorithm = BOILERPIPE_ALGORITHM;
}
