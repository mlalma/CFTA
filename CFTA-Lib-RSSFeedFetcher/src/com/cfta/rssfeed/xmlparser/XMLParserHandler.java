package com.cfta.rssfeed.xmlparser;

import java.util.HashMap;

public interface XMLParserHandler {
    public void startElement(final String tag, final HashMap<String, String> attributes);
    public void endElement(final String tag);
    public void startDocument();
    public void endDocument();
    public void text(final String str);
}
