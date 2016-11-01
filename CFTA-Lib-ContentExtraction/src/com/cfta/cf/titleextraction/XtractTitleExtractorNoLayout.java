// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.cf.titleextraction;

import com.cfta.cf.util.CFTASettings;
import java.util.ArrayList;
import nu.validator.htmlparser.common.DoctypeExpectation;
import nu.validator.htmlparser.common.Heuristics;
import nu.validator.htmlparser.common.XmlViolationPolicy;
import nu.validator.htmlparser.dom.HtmlDocumentBuilder;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// Title extractor based on document meta-tags
public class XtractTitleExtractorNoLayout {
  
    private Node headerNode = null;
    private ArrayList<String> headers = new ArrayList<>();
    private ArrayList<String> originalHeaders = new ArrayList<>();
    private String headerFromTitleTag = "";
    private Node titleHeaderNode = null;
    
    private String BODY_ELEMENT = "body";

    private final String TEXT_NODE = "#text";
    private final String SCRIPT_NODE = "script";
    private final String NOSCRIPT_NODE = "noscript";
    private final String COMMENT_NODE = "#comment";
    private final String STYLE_NODE = "style";
    private final String IFRAME_NODE = "iframe";
    
    private final String TITLE_TAG = "title";
    private final String META_TAG = "meta";
    private final String LINK_TAG = "link";

    private final String TYPE_ATTR = "type";
    private final String OEMBED_ATTR = "text/xml+oembed";
    private final String PROPERTY_ATTR = "property";
    private final String NAME_ATTR = "name";
    private final String FB_PROPERTY = "og:title";
    private final String TWITTER_PROPERTY = "twitter:title";
    private final String TWEETMEME_PROPERTY = "tweetmeme-title";
    private final String CONTENT_ATTR = "content";
    private final String DC_PROPERTY = "dcterms.title";
    private final String HEADLINE_PROPERTY = "headline";
    private final String ADDTHISTITLE_ATTR = "addthis:title";
    private final String TITLE_ATTR= "title";
    
    private final String H1_TAG = "h1";
    private final String H2_TAG = "h2";
    private final String H3_TAG = "h3";
    private final String H4_TAG = "h4";
    private final String H5_TAG = "h5";
    private final String H6_TAG = "h6";

    private final int MIN_HEADER_LENGTH = 5;   
    private final int MAX_HEADER_LENGTH = 150;
    
    private class PotentialHeader {
        public double styleScore = 0.0;
        public int textScore = 0;
        public Node n;
    }    

    ArrayList<PotentialHeader> headerCandidates = new ArrayList<PotentialHeader>();
                        
    // Returns header node
    public Node getHeaderNode() {
        return headerNode;
    }
    
    // Look for header from meta and title tags
    private void extractHeaderFromTags(Node node) {
        if (node.getNodeName().equalsIgnoreCase(TITLE_TAG) && node.getTextContent().trim().length() > 0) {
            String header = node.getTextContent().trim();
            if (header.indexOf(" | ") != -1) {
                header = header.substring(0, header.indexOf(" | "));
            } else if (header.indexOf(" :: ") != -1) {
                header = header.substring(0, header.indexOf(" :: "));
            }         
            header = parseHeader(header);
            headerFromTitleTag = header;
            titleHeaderNode = headerNode;            
        } else if (node.getNodeName().equalsIgnoreCase(META_TAG) && node.getAttributes() != null && node.getAttributes().getNamedItem(PROPERTY_ATTR) != null && node.getAttributes().getNamedItem(PROPERTY_ATTR).getNodeValue().trim().equalsIgnoreCase(FB_PROPERTY)) {
            if (node.getAttributes().getNamedItem(CONTENT_ATTR) != null) {                
                String original = node.getAttributes().getNamedItem(CONTENT_ATTR).getNodeValue().trim();
                originalHeaders.add(original);
                String header = parseHeader(original);
                headers.add(header);
            }
        } else if (node.getNodeName().equalsIgnoreCase(META_TAG) && node.getAttributes() != null && node.getAttributes().getNamedItem(NAME_ATTR) != null && node.getAttributes().getNamedItem(NAME_ATTR).getNodeValue().trim().equalsIgnoreCase(FB_PROPERTY)) {
            if (node.getAttributes().getNamedItem(CONTENT_ATTR) != null) {                
                String original = node.getAttributes().getNamedItem(CONTENT_ATTR).getNodeValue().trim();
                originalHeaders.add(original);
                String header = parseHeader(original);
                headers.add(header);
            }
        } else if (node.getNodeName().equalsIgnoreCase(META_TAG) && node.getAttributes() != null && node.getAttributes().getNamedItem(NAME_ATTR) != null && node.getAttributes().getNamedItem(NAME_ATTR).getNodeValue().trim().equalsIgnoreCase(TWITTER_PROPERTY)) {
            if (node.getAttributes().getNamedItem(CONTENT_ATTR) != null) {                
                String original = node.getAttributes().getNamedItem(CONTENT_ATTR).getNodeValue().trim();
                originalHeaders.add(original);
                String header = parseHeader(original);
                headers.add(header);
            }
        } else if (node.getNodeName().equalsIgnoreCase(META_TAG) && node.getAttributes() != null && node.getAttributes().getNamedItem(NAME_ATTR) != null && node.getAttributes().getNamedItem(NAME_ATTR).getNodeValue().trim().equalsIgnoreCase(TWEETMEME_PROPERTY)) {            
            if (node.getAttributes().getNamedItem(CONTENT_ATTR) != null) {                
                String original = node.getAttributes().getNamedItem(CONTENT_ATTR).getNodeValue().trim();
                originalHeaders.add(original);
                String header = parseHeader(original);
                headers.add(header);
            }
        } else if (node.getNodeName().equalsIgnoreCase(META_TAG) && node.getAttributes() != null && node.getAttributes().getNamedItem(NAME_ATTR) != null && node.getAttributes().getNamedItem(NAME_ATTR).getNodeValue().trim().equalsIgnoreCase(DC_PROPERTY)) {            
            if (node.getAttributes().getNamedItem(CONTENT_ATTR) != null) {                
                String original = node.getAttributes().getNamedItem(CONTENT_ATTR).getNodeValue().trim();
                originalHeaders.add(original);
                String header = parseHeader(original);
                headers.add(header);
            }
        } else if (node.getNodeName().equalsIgnoreCase(META_TAG) && node.getAttributes() != null && node.getAttributes().getNamedItem(NAME_ATTR) != null && node.getAttributes().getNamedItem(NAME_ATTR).getNodeValue().trim().equalsIgnoreCase(HEADLINE_PROPERTY)) {            
            if (node.getAttributes().getNamedItem(CONTENT_ATTR) != null) {                
                String original = node.getAttributes().getNamedItem(CONTENT_ATTR).getNodeValue().trim();
                originalHeaders.add(original);
                String header = parseHeader(original);
                headers.add(header);
            }
        } else if (node.getNodeName().equalsIgnoreCase(LINK_TAG) && node.getAttributes() != null && node.getAttributes().getNamedItem(TYPE_ATTR) != null && node.getAttributes().getNamedItem(TYPE_ATTR).getNodeValue().trim().equalsIgnoreCase(OEMBED_ATTR)) {
            if (node.getAttributes().getNamedItem(TITLE_ATTR) != null) {                
                String original = node.getAttributes().getNamedItem(TITLE_ATTR).getNodeValue().trim();
                originalHeaders.add(original);
                String header = parseHeader(original);
                headers.add(header);
            }
        } else if (node.getAttributes() != null && node.getAttributes().getNamedItem(ADDTHISTITLE_ATTR) != null && node.getAttributes().getNamedItem(ADDTHISTITLE_ATTR).getNodeValue().length() > 0) {
            String original = node.getAttributes().getNamedItem(ADDTHISTITLE_ATTR).getNodeValue();
            originalHeaders.add(original);
            String header = parseHeader(original);
            headers.add(header);
        }

        int i = 0;
        while (i < node.getChildNodes().getLength()) {
            extractHeaderFromTags(node.getChildNodes().item(i));
            i++;
        }
    }
                                         
    // Sanitizes header string
    private String parseHeader(String header) {
        header = header.replaceAll("\n", " ").replaceAll(" ", " ").replaceAll("( )+", " ");
        header = header.trim();
        header = Jsoup.parse(header).text().trim();
        
        String strippedDownHeader = "";
        for (int i = 0; i < header.length(); i++) {
            if (Character.isDigit(header.charAt(i)) || Character.isLetter(header.charAt(i))) {
                strippedDownHeader = strippedDownHeader + header.charAt(i);
            }
        }
        strippedDownHeader = strippedDownHeader.replaceAll("( )+", " ").trim().toLowerCase();
        header = strippedDownHeader;
        
        return header;
    }
           
    // Prints text under a node
    static public String printText(Node n) {
        String text = "";
                        
        int textLength = 0;        
        for (int i = 0; i < n.getChildNodes().getLength(); i++) {            
            org.w3c.dom.Node child = n.getChildNodes().item(i);
            if (child.getNodeName().equalsIgnoreCase("#text")) {
                String addedTextString = child.getTextContent().replaceAll("\\s+", " ");
                textLength += addedTextString.length();
                text = text + addedTextString;                
            } else if (child.getNodeName().equalsIgnoreCase("br")) {
                text = text + "\n";
            } else {
                String addedTextString = printText(n.getChildNodes().item(i));
                textLength += addedTextString.length();
                text = text + addedTextString;
                //text = text + printText(n.childNodes.get(childNode));
            }
        }
                        
        if (textLength > 0 && (n.getNodeName().equalsIgnoreCase("blockquote") || n.getNodeName().equalsIgnoreCase("p") || n.getNodeName().equalsIgnoreCase("h1") || n.getNodeName().equalsIgnoreCase("h2") ||
            n.getNodeName().equalsIgnoreCase("h3") || n.getNodeName().equalsIgnoreCase("h4") || n.getNodeName().equalsIgnoreCase("h5") ||
            n.getNodeName().equalsIgnoreCase("h6"))) {
                text = text + "\n";
        }
        
        return text;
    }
    
    // Tries to find matching header tag
    private void findHeader(Node n) {                                                 
        String text = parseHeader(printText(n));
        int textScore = 0;
                    
        // Check text length
        if (text.length() > MIN_HEADER_LENGTH && text.length() < MAX_HEADER_LENGTH) {
            if (headers.size() > 0) {
                for (String s : headers) {
                    
                    // TO_DO: First we should do levenshtein distance comparison, if < 2..3, that's the title
                    
                    if (s.contains(text)) {
                        if (text.length() > textScore) {
                            textScore = text.length();
                        }
                    } else if (text.contains(s)) {
                        if (s.length() > textScore) {
                            textScore = s.length();
                        }                                    
                    }
                }
            }

            if (headers.isEmpty() || (headers.size() > 0 && textScore > MIN_HEADER_LENGTH)) {
                PotentialHeader pHeader = new PotentialHeader();
                pHeader.n = n;
                pHeader.textScore = textScore;

                double styleScore = 0.0;
                if (n.getNodeName().equalsIgnoreCase(H1_TAG)) { styleScore = 8.0; }
                else if (n.getNodeName().equalsIgnoreCase(H2_TAG)) { styleScore = 8.0; }
                else if (n.getNodeName().equalsIgnoreCase(H3_TAG)) { styleScore = 8.0; }
                else if (n.getNodeName().equalsIgnoreCase(H4_TAG)) { styleScore = 4.0; }
                else if (n.getNodeName().equalsIgnoreCase(H5_TAG)) { styleScore = 4.0; }
                else if (n.getNodeName().equalsIgnoreCase(H6_TAG)) { styleScore = 4.0; }
                else { styleScore = 2.0; }
                pHeader.styleScore = styleScore;

                if (headerCandidates.isEmpty()) {
                    headerCandidates.add(pHeader);
                } else {
                    int addPos = 0;
                    for (int i = 0; i < headerCandidates.size(); i++) {
                        if (headerCandidates.get(i).textScore < pHeader.textScore) {
                            break;
                        } else if (headerCandidates.get(i).textScore == pHeader.textScore && headerCandidates.get(i).styleScore < pHeader.styleScore) {
                            break;
                        }
                        addPos++;
                    }

                    if (addPos < headerCandidates.size()) {
                        headerCandidates.add(addPos, pHeader);
                    } else {
                        headerCandidates.add(pHeader);
                    }                                
                }                            
            }
        }
        
        int i = 0;
        while (i < n.getChildNodes().getLength()) {
            findHeader(n.getChildNodes().item(i));
            i++;
        }
    }
        
    // Removes non-visible elements & iFrames from DOM tree
    protected void removeNonVisibleElements(Node n) {        
        try {
            NodeList nl = n.getChildNodes();
            int nn = nl.getLength();
            for (int i = 0; i < nn; i++) {            
                if (nl.item(i).getNodeName().equalsIgnoreCase(STYLE_NODE) || nl.item(i).getNodeName().equalsIgnoreCase(SCRIPT_NODE) || 
                    nl.item(i).getNodeName().equalsIgnoreCase(NOSCRIPT_NODE) || nl.item(i).getNodeName().equalsIgnoreCase(COMMENT_NODE) || 
                    nl.item(i).getNodeName().equalsIgnoreCase(IFRAME_NODE)) {
                    n.removeChild(nl.item(i));
                    i--;
                } else if (nl.item(i).getNodeName().equalsIgnoreCase(TEXT_NODE)) {                
                    if (nl.item(i).getTextContent().trim().length() == 0) {
                        n.removeChild(nl.item(i));
                        i--;
                    }
                } else {
                    removeNonVisibleElements(nl.item(i));
                }
            }
        } catch (Exception ex) {
        }
    }
     
    // Parses DOM tree
    private Document parseDOMTree(String html) {
        try {
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
            Document doc = builder.parse(IOUtils.toInputStream(html));
            if (doc.getElementsByTagName(BODY_ELEMENT).getLength() == 0) {
                throw new RuntimeException("Problem parsing document - invalid HTML, no body element found");
            }
            
            return doc;
        } catch (Exception ex) {
            if (CFTASettings.getDebug()) {
                ex.printStackTrace();
            }
            
            return null;
        }
    }
        
    // Extract title from web page
    public String extractTitle(String html) {       
        String title = "";
        Document doc = parseDOMTree(html);        
        if (doc != null) {
            removeNonVisibleElements(doc.getElementsByTagName(BODY_ELEMENT).item(0));
           
            boolean searchingFromTitleTag = false;
            headerFromTitleTag = "";
            titleHeaderNode = null;
            headerNode = null;
            headers.clear();
            headerCandidates.clear();
            extractHeaderFromTags(doc);
            
            if (headers.isEmpty() && headerFromTitleTag.length() > 0) {
                headers.add(headerFromTitleTag);
                searchingFromTitleTag = true;
            }
            
            //System.out.print("Tag scan complete, number of header texts from metafields: " + headers.size() + "\n");
            //System.out.print("Starting to search potential header nodes from document..\n");
            findHeader(doc.getElementsByTagName(BODY_ELEMENT).item(0));            
            
            if (headerCandidates.size() > 0) {
                //System.out.print("Header extraction: Number of potential header nodes: " + headerCandidates.size() + "\n");
                //PotentialHeader h = headerCandidates.get(0);
                //System.out.print("Chosen header: tag:" + h.n.getNodeName() + " textScore:" + h.textScore + " styleScore:" + h.styleScore + " text:" + printText(h.n).replaceAll("\n", "") + "\n");
            } else {
                //System.out.print("Header extraction: No header nodes found!\n");
                if (originalHeaders.size() > 0 && !searchingFromTitleTag) {
                    title = originalHeaders.get(0);
                    return title;
                }
                
                if (headerFromTitleTag.length() > 0 && !searchingFromTitleTag) {
                    headers.clear();
                    headers.add(headerFromTitleTag);                  
                    findHeader(doc.getElementsByTagName(BODY_ELEMENT).item(0));
                }
            }

            if (headerCandidates.size() > 0) {
                headerNode = headerCandidates.get(0).n;
            } else if (titleHeaderNode != null) {
                headerNode = titleHeaderNode;
            }
            
            title = printText(headerNode).replaceAll("\n", "");
        }
        
        return title;
    }    
}
