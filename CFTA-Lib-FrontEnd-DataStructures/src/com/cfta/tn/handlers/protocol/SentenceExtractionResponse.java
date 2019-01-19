// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.tn.handlers.protocol;

import java.util.List;

// Response is a list of strings that are sentences
public class SentenceExtractionResponse {
    public static final int RESPONSE_OK = 0;
    public static final int RESPONSE_FAIL = -1;

    public int errorCode = RESPONSE_OK;
    public List<String> sentences;
}
