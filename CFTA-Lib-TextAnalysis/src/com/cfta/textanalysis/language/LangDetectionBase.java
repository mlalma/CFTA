// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.textanalysis.language;

// Base interface for language detection algorithms
public interface LangDetectionBase {
    
    public String getLanguage(String text);
}
