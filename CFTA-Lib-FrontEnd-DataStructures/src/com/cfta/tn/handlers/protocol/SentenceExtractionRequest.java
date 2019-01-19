// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.tn.handlers.protocol;

// Divides text to individual sentences
public class SentenceExtractionRequest {
    public String text = "";
    // If this is true then newlines are used to separate paragraphs, headlines, and sub-headlines
    public boolean newlineAsParagraphSeparation = true;
}
