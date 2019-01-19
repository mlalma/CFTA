// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.rssfeed.xmlparser;

public class XMLParserException extends Exception {

    private static final long serialVersionUID = 1L;

    public XMLParserException(String reason) {
        super(reason);
    }
}
