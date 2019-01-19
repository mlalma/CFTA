// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.cf.handlers.protocol;

// Returns extracted content
public class ContentExtractionResponse {
    public static final int RESPONSE_OK = 0;
    public static final int RESPONSE_FAIL = -1;

    public int errorCode = RESPONSE_OK;

    public String title = "";
    public String text = "";
    public String mainImageUrl = "";
    public String language = "";
}
