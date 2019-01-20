// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.cf.titleextraction;

import com.cfta.cf.xtract.dom.HtmlNode;
import com.cfta.cf.xtract.dom.ParsedWebPage;
import com.cfta.cf.xtract.dom.XtractUtil;
import com.cfta.log.CFTALog;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;

// Tries to extract header from the DOM tree
public class XtractHeader {

    private HtmlNode headerNode = null;
    private ArrayList<String> headers = new ArrayList<>();
    private String headerFromTitleTag = "";
    //private HtmlNode titleHeaderNode = null;

    private final String TITLE_TAG = "title";
    private final String META_TAG = "meta";
    private final String PROPERTY_ATTR = "property";
    private final String NAME_ATTR = "name";
    private final String FB_PROPERTY = "og:title";
    private final String TWITTER_PROPERTY = "twitter:title";
    private final String TWEETMEME_PROPERTY = "tweetmeme-title";
    private final String CONTENT_ATTR = "content";
    private final String LINK_TAG = "LINK";
    private final String TYPE_ATTR = "type";
    private final String OEMBER_ATTR = "text/xml+oembed";
    private final String TITLE_ATTR = "title";
    private final String HEADLINE_PROPERTY = "headline";
    private final String DC_PROPERTY = "dcterms.title";
    private final String ADDTHISTITLE_ATTR = "addthis:title";
    private final String ITEMPROP_ATTR = "itemprop";
    private final String NAME_PROPERTY = "name";

    private final String H1_TAG = "h1";
    private final String H2_TAG = "h2";
    private final String H3_TAG = "h3";
    private final String H4_TAG = "h4";
    private final String H5_TAG = "h5";
    private final String H6_TAG = "h6";
    //private final String HEADER_NODE = "header";

    private final int MIN_HEADER_LENGTH = 5;
    private final int MAX_HEADER_LENGTH = 150;

    private final int MAX_YPOS_FOR_HEADER = 1500;
    private final int MAX_HEIGHT_FOR_HEADER = 350;
    private final double MIN_FONT_SIZE_RATIO = 1.0;
    private final double MAX_XPOS_RATIO = 0.7;

    // Constructor
    public XtractHeader() {
    }

    // Returns header node
    public HtmlNode getHeaderNode() {
        return headerNode;
    }

    // Look for header from meta and title tags
    private void extractHeaderFromTags(HtmlNode node) {
        if (node.nodeName.equalsIgnoreCase(TITLE_TAG) && node.text.trim().length() > 0) {
            //headerNode = node;
            String header = node.text;
            if (header.contains(" | ")) {
                header = header.substring(0, header.indexOf(" | "));
            } else if (header.contains(" :: ")) {
                header = header.substring(0, header.indexOf(" :: "));
            }

            header = parseHeader(header);
            if (headerFromTitleTag == null) {
                headerFromTitleTag = header;
                //titleHeaderNode = headerNode;            
            }
        } else if (node.nodeName.equalsIgnoreCase(META_TAG) && node.attributes.containsKey(PROPERTY_ATTR) && node.attributes.get(PROPERTY_ATTR).equalsIgnoreCase(FB_PROPERTY)) {
            if (node.attributes.containsKey(CONTENT_ATTR)) {
                String header = parseHeader(node.attributes.get(CONTENT_ATTR));
                headers.add(header);
            }
        } else if (node.nodeName.equalsIgnoreCase(META_TAG) && node.attributes.containsKey(NAME_ATTR) && node.attributes.get(NAME_ATTR).equalsIgnoreCase(FB_PROPERTY)) {
            if (node.attributes.containsKey(CONTENT_ATTR)) {
                String header = parseHeader(node.attributes.get(CONTENT_ATTR));
                headers.add(header);
            }
        } else if (node.nodeName.equalsIgnoreCase(META_TAG) && node.attributes.containsKey(NAME_ATTR) && node.attributes.get(NAME_ATTR).equalsIgnoreCase(TWITTER_PROPERTY)) {
            if (node.attributes.containsKey(CONTENT_ATTR)) {
                String header = parseHeader(node.attributes.get(CONTENT_ATTR));
                headers.add(header);
            }
        } else if (node.nodeName.equalsIgnoreCase(META_TAG) && node.attributes.containsKey(NAME_ATTR) && node.attributes.get(NAME_ATTR).equalsIgnoreCase(TWEETMEME_PROPERTY)) {
            if (node.attributes.containsKey(CONTENT_ATTR)) {
                String header = parseHeader(node.attributes.get(CONTENT_ATTR));
                headers.add(header);
            }
        } else if (node.nodeName.equalsIgnoreCase(META_TAG) && node.attributes.containsKey(NAME_ATTR) && node.attributes.get(NAME_ATTR).equalsIgnoreCase(DC_PROPERTY)) {
            if (node.attributes.containsKey(CONTENT_ATTR)) {
                String header = parseHeader(node.attributes.get(CONTENT_ATTR));
                headers.add(header);
            }
        } else if (node.nodeName.equalsIgnoreCase(META_TAG) && node.attributes.containsKey(NAME_ATTR) && node.attributes.get(NAME_ATTR).equalsIgnoreCase(HEADLINE_PROPERTY)) {
            if (node.attributes.containsKey(CONTENT_ATTR)) {
                String header = parseHeader(node.attributes.get(CONTENT_ATTR));
                headers.add(header);
            }
        } else if (node.nodeName.equalsIgnoreCase(META_TAG) && node.attributes.containsKey(ITEMPROP_ATTR) && node.attributes.get(ITEMPROP_ATTR).equalsIgnoreCase(NAME_PROPERTY)) {
            if (node.attributes.containsKey(CONTENT_ATTR)) {
                String header = parseHeader(node.attributes.get(CONTENT_ATTR));
                headers.add(header);
            }
        } else if (node.nodeName.equalsIgnoreCase(LINK_TAG) && node.attributes.containsKey(TYPE_ATTR) && node.attributes.get(TYPE_ATTR).equalsIgnoreCase(OEMBER_ATTR)) {
            if (node.attributes.containsKey(TITLE_ATTR)) {
                String header = parseHeader(node.attributes.get(TITLE_ATTR));
                headers.add(header);
            }
        } else if (node.attributes.containsKey(ADDTHISTITLE_ATTR)) {
            String header = parseHeader(node.attributes.get(ADDTHISTITLE_ATTR));
            headers.add(header);
        }

        int i = 0;
        while (i < node.childNodes.size()) {
            extractHeaderFromTags(node.childNodes.get(i));
            i++;
        }
    }

    // Javascript-like String.search
    protected int search(String input, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(input);

        if (!matcher.find()) {
            return -1;
        }
        return matcher.start();
    }

    // Sanitizes header string
    private String parseHeader(String header) {
        header = header.replaceAll("\n", " ").replaceAll(" ", " ").replaceAll("( )+", " ");
        header = header.trim();
        header = Jsoup.parse(header).text().trim();

        StringBuilder strippedDownHeader = new StringBuilder();
        for (int i = 0; i < header.length(); i++) {
            if (Character.isDigit(header.charAt(i)) || Character.isLetter(header.charAt(i))) {
                strippedDownHeader.append(header.charAt(i));
            }
        }
        strippedDownHeader = new StringBuilder(strippedDownHeader.toString().replaceAll("( )+", " ").trim().toLowerCase());
        header = strippedDownHeader.toString();

        return header;
    }

    private int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    private int levenshteinDistance(String str1, String str2) {
        int[][] distance = new int[str1.length() + 1][str2.length() + 1];

        for (int i = 0; i <= str1.length(); i++) {
            distance[i][0] = i;
        }

        for (int j = 1; j <= str2.length(); j++) {
            distance[0][j] = j;
        }

        for (int i = 1; i <= str1.length(); i++) {
            for (int j = 1; j <= str2.length(); j++) {
                distance[i][j] = minimum(
                        distance[i - 1][j] + 1,
                        distance[i][j - 1] + 1,
                        distance[i - 1][j - 1] + ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1));
            }
        }

        return distance[str1.length()][str2.length()];
    }

    private class PotentialHeader {
        double styleScore = 0.0;
        int textScore = 0;
        HtmlNode n;
    }

    private ArrayList<PotentialHeader> headerCandidates = new ArrayList<>();
    private ParsedWebPage webPage;

    private void findHeaderStrict(HtmlNode n) {

        // Check vertical position & height of node for hard limits
        if (n.properNode &&
                (n.yPos > 0 && n.yPos < MAX_YPOS_FOR_HEADER) &&
                (n.height > 0 && n.height < MAX_HEIGHT_FOR_HEADER)) {

            // Check font size for hard limits
            if ((webPage.avgTotalFontSize / (double) n.fontSize) < MIN_FONT_SIZE_RATIO) {

                // Check xPos / width ratio for hard limits
                if ((double) n.xPos / (double) webPage.bodyNode.width < MAX_XPOS_RATIO) {

                    // Check text score for finding the header based on meta texts
                    int textScore = 0;
                    String text = parseHeader(XtractUtil.printText(n));

                    // Check text length
                    if (text.length() > MIN_HEADER_LENGTH && text.length() < MAX_HEADER_LENGTH) {
                        if (headers.size() > 0) {
                            for (String s : headers) {
                                if (levenshteinDistance(s, text) < 3) {
                                    if (text.length() < s.length()) {
                                        if (text.length() > textScore) {
                                            textScore = text.length();
                                        }
                                    } else {
                                        if (s.length() > textScore) {
                                            textScore = s.length();
                                        }
                                    }
                                } else if (s.contains(text)) {
                                    if (text.length() > textScore) {
                                        textScore = text.length();
                                    }
                                } else if (text.contains(s)) {
                                    // TO_DO: Does this generate false positives?
                                    if (s.length() > textScore) {
                                        textScore = s.length();
                                    }
                                }
                            }
                        }

                        if (headers.isEmpty() || textScore > MIN_HEADER_LENGTH) {
                            PotentialHeader pHeader = new PotentialHeader();
                            pHeader.n = n;
                            pHeader.textScore = textScore;

                            double styleScore = 0.0;
                            if (n.nodeName.equalsIgnoreCase(H1_TAG)) {
                                styleScore = 8.0;
                            } else if (n.nodeName.equalsIgnoreCase(H2_TAG)) {
                                styleScore = 8.0;
                            } else if (n.nodeName.equalsIgnoreCase(H3_TAG)) {
                                styleScore = 8.0;
                            } else if (n.nodeName.equalsIgnoreCase(H4_TAG)) {
                                styleScore = n.fontSize / webPage.avgTotalFontSize;
                                if (styleScore > 4.0) {
                                    styleScore = 4.0;
                                }
                            } else if (n.nodeName.equalsIgnoreCase(H5_TAG)) {
                                styleScore = n.fontSize / webPage.avgTotalFontSize;
                                if (styleScore > 4.0) {
                                    styleScore = 4.0;
                                }
                            } else if (n.nodeName.equalsIgnoreCase(H6_TAG)) {
                                styleScore = n.fontSize / webPage.avgTotalFontSize;
                                if (styleScore > 4.0) {
                                    styleScore = 4.0;
                                }
                            } else {
                                if (n.fontStyle.toLowerCase().contains("italic") || n.fontStyle.toLowerCase().contains("oblique") || n.fontWeight.toLowerCase().contains("bold")) {
                                    styleScore += 1.0;
                                }

                                double sizePoints = n.fontSize / webPage.avgTotalFontSize;
                                if (sizePoints > 4.0) {
                                    sizePoints = 4.0;
                                }
                                styleScore += sizePoints;
                            }
                            pHeader.styleScore = styleScore;

                            if (headerCandidates.isEmpty()) {
                                headerCandidates.add(pHeader);
                            } else {
                                int addPos = 0;
                                for (PotentialHeader headerCandidate : headerCandidates) {
                                    if (headerCandidate.textScore < pHeader.textScore) {
                                        break;
                                    } else if (headerCandidate.textScore == pHeader.textScore && headerCandidate.styleScore < pHeader.styleScore) {
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
                }
            }
        }

        int i = 0;
        while (i < n.childNodes.size()) {
            findHeaderStrict(n.childNodes.get(i));
            i++;
        }
    }

    // Tries to find header from all nodes
    public void findHeader(ParsedWebPage webPage) {
        CFTALog.LL("Starting to extract header..\n");

        this.webPage = webPage;
        headerNode = null;
        headers.clear();
        headerCandidates.clear();

        extractHeaderFromTags(webPage.headNode);
        extractHeaderFromTags(webPage.bodyNode);

        if (headers.isEmpty() && headerFromTitleTag.length() > 0) {
            headers.add(headerFromTitleTag);
        }

        CFTALog.LL("Tag scan complete, number of header texts from metafields: " + headers.size() + "\n");
        for (String s : headers) {
            CFTALog.LL("Header candidate: " + s + "\n");
        }
        CFTALog.LL("Starting to search potential header nodes from document..\n");
        findHeaderStrict(webPage.bodyNode);

        if (XtractUtil.DEBUG) {
            if (headerCandidates.size() > 0) {
                CFTALog.LL("Header extraction: Number of potential header nodes: " + headerCandidates.size() + "\n");
                PotentialHeader h = headerCandidates.get(0);
                CFTALog.LL("Chosen header: tag:" + h.n.nodeName + " textScore:" + h.textScore + " styleScore:" + h.styleScore + " text:" + XtractUtil.printText(h.n).replaceAll("\n", "") + "\n");
            } else {
                CFTALog.LL("Header extraction: No header nodes found!\n");
            }
        }

        if (headerCandidates.size() > 0) {
            headerNode = headerCandidates.get(0).n;
        }
    }
}
