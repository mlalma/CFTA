// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.tn.handlers.protocol;

import java.util.List;

// Returns extracted tokens in a list
public class TokenExtractionResponse {
    public static final int RESPONSE_OK = 0;
    public static final int RESPONSE_FAIL = -1;

    public int errorCode = RESPONSE_OK;
    public List<String> tokens;
}
