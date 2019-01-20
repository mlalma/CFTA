// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.cf.contentextraction;

// Interface to content extractor
public interface ContentExtractionBase {
    void extractContent(String html, boolean extractTitle, boolean extractMainImage, boolean extractText);

    String getTitle();

    String getMainImageUrl();

    String getArticleText();
}
