// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.cf.xtract.dom;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;

// Utility methods
public class XtractUtil {

    public static final boolean DEBUG = true;

    // Logs line of text if debug is on
    public static void L(String line) {
        if (DEBUG) {
            System.out.print(line);
        }
    }

    // Converts URL to absolute URL given a base URL
    public static String toAbsoluteUrl(String baseUrl, String curUrl) {
        String potentialUrl = curUrl;
        if (!potentialUrl.startsWith("http://") || !potentialUrl.startsWith("https://")) {
            try {
                URL relativeUrl = new URL(baseUrl);
                URL absoluteUrl = new URL(relativeUrl, potentialUrl);
                potentialUrl = absoluteUrl.toString();
            } catch (MalformedURLException ignored) {
            }
        }

        return potentialUrl;
    }

    // Find longest substring of two strings
    public static int longestSubstr(String first, String second) {
        if (first == null || second == null || first.length() == 0 || second.length() == 0) {
            return 0;
        }

        int maxLen = 0;
        int fl = first.length();
        int sl = second.length();
        int[][] table = new int[fl][sl];

        for (int i = 0; i < fl; i++) {
            for (int j = 0; j < sl; j++) {
                if (first.charAt(i) == second.charAt(j)) {
                    if (i == 0 || j == 0) {
                        table[i][j] = 1;
                    } else {
                        table[i][j] = table[i - 1][j - 1] + 1;
                    }
                    if (table[i][j] > maxLen) {
                        maxLen = table[i][j];
                    }
                }
            }
        }
        return maxLen;
    }

    // Prints text under a node
    static public String printText(HtmlNode n) {
        StringBuilder text = new StringBuilder();

        if (n.display.equalsIgnoreCase("none")) {
            return text.toString();
        }

        int childNode = 0;
        int textLength = 0;
        for (int i = 0; i < n.docNode.getChildNodes().getLength(); i++) {
            org.w3c.dom.Node child = n.docNode.getChildNodes().item(i);
            if (child.getNodeName().equalsIgnoreCase("#text")) {
                if (!n.display.equalsIgnoreCase("none")) {
                    String addedTextString = child.getTextContent().replaceAll("\\s+", " ");
                    textLength += addedTextString.length();
                    text.append(addedTextString);
                }
            } else if (child.getNodeName().equalsIgnoreCase("br")) {
                text.append("\n");
                childNode++;
            } else {
                String addedTextString = printText(n.childNodes.get(childNode));
                textLength += addedTextString.length();
                text.append(addedTextString);
                //text = text + printText(n.childNodes.get(childNode));
                childNode++;
            }
        }

        if (!n.display.startsWith("inline") && textLength > 0) {
            if (!n.nodeName.equalsIgnoreCase("span")) {
                text.append("\n");
            }
        }

        if (textLength > 0 && (n.docNode.getNodeName().equalsIgnoreCase("blockquote") || n.docNode.getNodeName().equalsIgnoreCase("p") || n.docNode.getNodeName().equalsIgnoreCase("h1") || n.docNode.getNodeName().equalsIgnoreCase("h2") ||
                n.docNode.getNodeName().equalsIgnoreCase("h3") || n.docNode.getNodeName().equalsIgnoreCase("h4") || n.docNode.getNodeName().equalsIgnoreCase("h5") ||
                n.docNode.getNodeName().equalsIgnoreCase("h6"))) {
            text.append("\n");
        }

        return text.toString();
    }

    // Prints text under a node
    private static String printTextSeparateSpan(HtmlNode n) {
        StringBuilder text = new StringBuilder();

        if (n.display.equalsIgnoreCase("none")) {
            return text.toString();
        }

        int childNode = 0;
        int textLength = 0;

        for (int i = 0; i < n.docNode.getChildNodes().getLength(); i++) {
            org.w3c.dom.Node child = n.docNode.getChildNodes().item(i);
            if (child.getNodeName().equalsIgnoreCase("#text")) {
                if (!n.display.equalsIgnoreCase("none")) {
                    String addedTextString = child.getTextContent().replaceAll("\\s+", " ");
                    textLength += addedTextString.length();
                    text.append(" ").append(addedTextString);
                }
            } else if (child.getNodeName().equalsIgnoreCase("br")) {
                text.append("\n");
                childNode++;
            } else {
                String addedTextString = printTextSeparateSpan(n.childNodes.get(childNode));
                textLength += addedTextString.length();
                text.append(" ").append(addedTextString);
                //text = text + printText(n.childNodes.get(childNode));
                childNode++;
            }
        }

        if ((!n.display.startsWith("inline") || n.nodeName.equalsIgnoreCase("li") || n.nodeName.equalsIgnoreCase("span"))) {
            text.append("\n");
        }

        if (textLength > 0 && (n.docNode.getNodeName().equalsIgnoreCase("blockquote") || n.docNode.getNodeName().equalsIgnoreCase("p") || n.docNode.getNodeName().equalsIgnoreCase("h1") || n.docNode.getNodeName().equalsIgnoreCase("h2") ||
                n.docNode.getNodeName().equalsIgnoreCase("h3") || n.docNode.getNodeName().equalsIgnoreCase("h4") || n.docNode.getNodeName().equalsIgnoreCase("h5") ||
                n.docNode.getNodeName().equalsIgnoreCase("h6"))) {
            text.append("\n");
        }

        return text.toString();
    }

    // Reads a text file
    public static String readTextFile(String fileName) {
        String output = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                output = output + line + "\n";
            }
            br.close();
        } catch (Exception ignored) {
        }
        return output;
    }

    // Debug prints node and its subnodes
    private static void printNode(org.w3c.dom.Node n, int level, boolean printText) {
        for (int i = 0; i < level; i++) {
            System.out.print("\t");
        }

        if (!printText) {
            System.out.println(n.getNodeName());
        } else {
            System.out.println(n.getNodeName() + ": " + n.getTextContent());
        }

        for (int i = 0; i < n.getChildNodes().getLength(); i++) {
            printNode(n.getChildNodes().item(i), level + 1, printText);
        }
    }
}
