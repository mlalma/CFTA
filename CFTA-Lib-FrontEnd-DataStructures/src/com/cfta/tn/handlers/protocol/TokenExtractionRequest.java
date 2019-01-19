// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.tn.handlers.protocol;

// Request to separate text to tokens
public class TokenExtractionRequest {
    public String text = "";
    public boolean addSentenceSeparationTokens = true;
    public boolean newlineAsParagraphSeparation = true;
}
