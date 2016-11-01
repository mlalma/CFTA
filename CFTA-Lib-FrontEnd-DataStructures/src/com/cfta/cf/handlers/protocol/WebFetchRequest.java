// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.cf.handlers.protocol;

// Web fetch request
public class WebFetchRequest {    
    public static final String FETCHER_APACHE = "apache";
    public static final String FETCHER_XTRACT = "xtract";
    
    public String usedFetcher = FETCHER_APACHE;
    public String url = "";    
}
