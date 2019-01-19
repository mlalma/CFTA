// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.rssfeed.xmlparser;

import java.util.HashMap;

public interface XMLParserHandler {
    void startElement(final String tag, final HashMap<String, String> attributes);
    void endElement(final String tag);
    void startDocument();
    void endDocument();
    void text(final String str);
}
