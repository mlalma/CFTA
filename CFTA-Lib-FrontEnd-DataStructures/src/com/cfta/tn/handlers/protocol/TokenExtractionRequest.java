package com.cfta.tn.handlers.protocol;

// Request to separate text to tokens
public class TokenExtractionRequest {
    public String text = "";
    public boolean addSentenceSeparationTokens = true;
    public boolean newlineAsParagraphSeparation = true;
}
