// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.cf.feeds;

import com.cfta.cf.httpfetch.ApacheHttpClientFetcher;
import com.cfta.cf.httpfetch.HttpFetcherBase;
import java.util.ArrayList;
import java.util.List;
import nu.validator.htmlparser.common.DoctypeExpectation;
import nu.validator.htmlparser.common.Heuristics;
import nu.validator.htmlparser.common.XmlViolationPolicy;
import nu.validator.htmlparser.dom.HtmlDocumentBuilder;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// Simple RSS feed finder, looks only for "application/rss+xml" link attributes
public class RSSFeedFinder {
    
    private HttpFetcherBase fetcher = new ApacheHttpClientFetcher();
    
    private final String LINK_TAG = "link";
    private final String TYPE_ATTR = "type";
    private final String RSS_TYPE = "application/rss+xml";
    private final String HREF_ATTR = "href";
    private final String TITLE_ATTR = "title";
    
    // Constructor
    public RSSFeedFinder() {        
    }
    
    // Parses HTML page for finding RSS sources from HTML source
    public List<RSSFeedSource> parseHtmlPageForSources(String page) throws Exception {
        ArrayList<RSSFeedSource> sourceList = new ArrayList<>();
        HtmlDocumentBuilder builder = new HtmlDocumentBuilder();
        builder.setCommentPolicy(XmlViolationPolicy.ALTER_INFOSET);
        builder.setContentNonXmlCharPolicy(XmlViolationPolicy.ALTER_INFOSET);
        builder.setContentSpacePolicy(XmlViolationPolicy.ALTER_INFOSET);        
        builder.setNamePolicy(XmlViolationPolicy.ALTER_INFOSET);
        builder.setStreamabilityViolationPolicy(XmlViolationPolicy.ALTER_INFOSET);
        builder.setXmlnsPolicy(XmlViolationPolicy.ALTER_INFOSET);
        builder.setMappingLangToXmlLang(true);
        builder.setHtml4ModeCompatibleWithXhtml1Schemata(true);
        builder.setHeuristics(Heuristics.ALL);
        builder.setCheckingNormalization(false);
        builder.setDoctypeExpectation(DoctypeExpectation.NO_DOCTYPE_ERRORS);
        builder.setIgnoringComments(true);
        builder.setScriptingEnabled(true);
        builder.setXmlPolicy(XmlViolationPolicy.ALTER_INFOSET);
        Document doc = builder.parse(IOUtils.toInputStream(page));

        NodeList nodes = doc.getElementsByTagName(LINK_TAG);
        for (int i  = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            if (n.hasAttributes() && n.getAttributes().getNamedItem(TYPE_ATTR) != null && n.getAttributes().getNamedItem(TYPE_ATTR).getNodeValue().trim().equalsIgnoreCase(RSS_TYPE)) {
                String linkUrl = null;
                String name = "";
                if (n.getAttributes().getNamedItem(HREF_ATTR) != null) {
                    linkUrl = n.getAttributes().getNamedItem(HREF_ATTR).getNodeValue();
                }
                if (n.getAttributes().getNamedItem(TITLE_ATTR) != null) {
                    name = n.getAttributes().getNamedItem(TITLE_ATTR).getNodeValue();
                }

                if (name != null && linkUrl != null) {
                    RSSFeedSource src = new RSSFeedSource();
                    src.name = name;
                    src.sourceUrl = linkUrl;
                    sourceList.add(src);
                }
            }
        }        
        return sourceList;
    }
    
    // Searches for RSS sources in 
    public List<RSSFeedSource> findRSSSources(String url) {
        List<RSSFeedSource> sourceList;
        try {
            String page = fetcher.getWebPage(url);
            sourceList = parseHtmlPageForSources(page);
        } catch (Exception ex) {            
            sourceList = new ArrayList<>();
        }        
        
        return sourceList;
    }
}
