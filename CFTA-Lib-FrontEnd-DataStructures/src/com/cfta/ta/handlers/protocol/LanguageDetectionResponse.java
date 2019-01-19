// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.ta.handlers.protocol;

// Language detection response from text
public class LanguageDetectionResponse {
    public static final int RESPONSE_OK = 0;
    public static final int RESPONSE_FAIL = -1;

    public int errorCode = RESPONSE_OK;
    public String language = "";
}
