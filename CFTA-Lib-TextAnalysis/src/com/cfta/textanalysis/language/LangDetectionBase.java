// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.textanalysis.language;

// Base interface for language detection algorithms
public interface LangDetectionBase {

    String getLanguage(String text);
}
