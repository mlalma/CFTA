// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.ta.handlers.protocol;

import java.util.ArrayList;
import java.util.List;

// Text summarization response, returns most significant sentences of text
public class TextSummarizationResponse {

    // Sentence included in the summary, incl. its position on document and rank value (signifigance)
    public class SummarySentence {
        public int position = 0;
        public double rank = 0.0;
        public String text = "";
    }

    public static final int RESPONSE_OK = 0;
    public static final int RESPONSE_FAIL = -1;

    public int errorCode = RESPONSE_OK;

    // Returned sentences that summarizes the article (depending on parameters)
    public List<SummarySentence> sentences = new ArrayList<>();

    public SummarySentence newSentence() {
        return new SummarySentence();
    }
}
