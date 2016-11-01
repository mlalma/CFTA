// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2014
package com.cfta.cf.contentextraction;

// Interface to content extractor
public interface ContentExtractionBase {        
    public void extractContent(String html, boolean extractTitle, boolean extractMainImage, boolean extractText);

    public String getTitle();
    public String getMainImageUrl();
    public String getArticleText();
}
