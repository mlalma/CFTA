// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.textanalysis.stopwords;

import com.cfta.cf.util.CFTASettings;
import java.util.HashMap;

// Factory class for managing stopword filters
public class StopwordFilterFactory {

    private static final HashMap<String, StopwordFilter> stopwordFilters = new HashMap<>();

    // Returns appropriate stopword filter
    static public StopwordFilter getStopwordFilter(String language) {        
        if (stopwordFilters.get(language) != null) {
            return stopwordFilters.get(language);
        } else {
            StopwordFilter filter = new StopwordFilter(language);
            filter.loadStopwordlist(CFTASettings.getStopwordListDir() + "stopwords_" + language + ".txt");
            stopwordFilters.put(language, filter);
            return filter;
        }
    }
    
    // Private constructor - this class can never be instantiated
    private StopwordFilterFactory() {}
}
