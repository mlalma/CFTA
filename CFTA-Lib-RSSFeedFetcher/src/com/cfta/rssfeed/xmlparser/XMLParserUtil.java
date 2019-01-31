// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.rssfeed.xmlparser;

import com.cfta.cf.util.CFTASettings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.pojava.datetime.DateTime;

public class XMLParserUtil {

    static public XMLNode findNode(final String nodeName, final List<XMLNode> list) {
        for (int i = 0; i < list.size(); i++) {
            XMLNode n = list.get(i);
            if (n.name.equalsIgnoreCase(nodeName.toLowerCase())) {
                return n;
            }
        }

        return null;
    }

    static public List<XMLNode> findNodes(final String nodeName, final List<XMLNode> list) {
        List<XMLNode> outputList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            XMLNode n = list.get(i);
            if (n.name.equalsIgnoreCase(nodeName)) {
                outputList.add(n);
            }
        }
        return outputList;
    }


}
