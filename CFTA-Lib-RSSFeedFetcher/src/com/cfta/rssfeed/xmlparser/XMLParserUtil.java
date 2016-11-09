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
                    Date d = new Date(format.parse(date).getTime());
                    return d;
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
}
