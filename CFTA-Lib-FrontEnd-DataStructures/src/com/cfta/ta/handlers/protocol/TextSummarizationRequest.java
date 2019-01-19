// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.ta.handlers.protocol;

// Text summarization request, returns most significant sentences of text
public class TextSummarizationRequest {
    public String url = "";

    public String title = "";
    public String text = "";
    public boolean dropTitleFromSummaryCreation = true;

    // Choose one of these to define how you want the text to be summarized

    // If true, returns all sentences for further evaluation
    public boolean allSentences = false;
    // Number of sentences to be returned
    public int summarySentenceNum = -1;
    // Number of words (returns sentences that contain at least approxNumOfWords amount of words, exact word count depends on sentence length)
    public int approxNumOfWords = -1;
    // Returns most significant sentences that cover at approxPercentageOfText % of text to be used in summarization (usually goes a bit over)
    public double approxPercentageOfText = -1.0;
}
