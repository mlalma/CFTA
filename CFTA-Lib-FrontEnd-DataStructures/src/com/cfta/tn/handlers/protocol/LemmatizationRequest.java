// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.tn.handlers.protocol;

// Lemmatization request
public class LemmatizationRequest {
    public String text = "";
    public boolean addSentenceSeparationTokens = true;
    public boolean newlineAsParagraphSeparation = true;
}
