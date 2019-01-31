package com.cfta.rssfeed.util;

import com.cfta.cf.util.CFTASettings;
import org.pojava.datetime.DateTime;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// Common utilities used to find / parse node trees
public class NodeTools {

    public static final String EMPTY_STRING = "";

    // Returns attribute value or empty string if none
    static public String getAttributeValueOrEmpty(final String attrName, final Node node) {
        if (node.getAttributes() != null) {
            for (int i = 0; i < node.getAttributes().getLength(); i++) {
                if (node.getAttributes().item(i).getNodeName().trim().equalsIgnoreCase(attrName.trim())) {
                    return node.getAttributes().item(i).getNodeValue();
                }
            }
        }
        return EMPTY_STRING;
    }

    // Returns first child node with given name
    static public Node childNode(final Node n, final String nodeName) {
        for (int i = 0; i < n.getChildNodes().getLength(); i++) {
            Node child = n.getChildNodes().item(i);
            if (child.getNodeName() != null && child.getNodeName().trim().equalsIgnoreCase(nodeName)) {
                return child;
            }
        }
        return null;
    }

    // Returns all child node with given name
    static public List<Node> childNodes(final Node n, final String nodeName) {
        ArrayList<Node> childList = new ArrayList<>();
        for (int i = 0; i < n.getChildNodes().getLength(); i++) {
            Node child = n.getChildNodes().item(i);
            if (child.getNodeName() != null && child.getNodeName().trim().equalsIgnoreCase(nodeName)) {
                childList.add(child);
            }
        }
        return childList;
    }

    // Date formats themselves
    static final private String[] dateFormatStrings = {
            "EEE, dd MMM yyyy HH:mm:ss zzz",
            "dd MMM yyyy HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss",
            "EEE, dd MMM yyyy HH:mm:ss zzz",
            "dd MMM yyyy HH:mm:ss zzz",
            "dd.MM.yyyy HH:mm",
            "EEE, dd MMM yyyy HH:mm zzz",
            "EEE, dd MMM yyyy HH:mm zzz",
            "yyyy-MM-dd HH:mm:ss.S",
            "EEE, dd MMM yyyy HH:mm:ss zzz",
            "dd.MM.yyyy",
            "yyyy-MM-dd HH:mm:ss",
            "EEE, dd MMM yyyy HH:mm:ss",
            "MMM dd, yyyy HH:mm:ss a",
            "dd MMM yyyy HH:mm:ss",
            "EEE, dd MMM yyyy HH:mm:ss",
    };

    // Parses the date
    static public Date parseDate(String date, Locale locale) {
        // If feed defines locale, try also using it
        if (locale != null) {
            for (int i = 0; i < dateFormatStrings.length; i++) {
                SimpleDateFormat format = new SimpleDateFormat(dateFormatStrings[i], locale);
                try {
                    return new Date(format.parse(date).getTime());
                } catch (Exception ex) {
                }
            }
        }

        // Otherwise try running date through English locale
        for (int i = 0; i < dateFormatStrings.length; i++) {
            SimpleDateFormat format = new SimpleDateFormat(dateFormatStrings[i], Locale.ENGLISH);
            try {
                Date d = new Date(format.parse(date).getTime());
                return d;
            } catch (Exception ex) {
            }
        }

        // As a last-ditch resort, try using DateTime POJava class for parsing
        try {
            Date d = DateTime.parse(date).toDate();
            return d;
        } catch (Exception ex) {
        }

        if (CFTASettings.getDebug()) {
            System.err.println("Could not parse date: " + date);
        }

        return null;
    }

    // Private constructor, this class can never be instantiated
    private NodeTools() {
    }
}
