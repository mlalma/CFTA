// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.cf.handlers.protocol;

// Response to web fetching
public class WebFetchResponse {
    public static final int RESPONSE_OK = 0;
    public static final int RESPONSE_TIMEOUT = -1;
    public static final int RESPONSE_FAIL = -2;
    
    public int errorCode = RESPONSE_OK;
    public String html = "";        
}
