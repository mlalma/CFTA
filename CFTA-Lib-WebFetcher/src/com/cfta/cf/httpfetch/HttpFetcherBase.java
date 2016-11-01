// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.cf.httpfetch;

// Interface for different web page fetchers
public interface HttpFetcherBase {
    public String getWebPage(String url) throws Exception;
}
