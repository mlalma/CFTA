// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.cf.httpfetch;

// Interface for different web page fetchers
public interface HttpFetcherBase {
    String getWebPage(final String url) throws Exception;
}
