// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2014
package com.cfta.cf.imageextraction;

import org.w3c.dom.Document;

public interface ImageExtractionBase {
    public String extractMainImage(String html);
    public String extractMainImage(Document doc);
}

