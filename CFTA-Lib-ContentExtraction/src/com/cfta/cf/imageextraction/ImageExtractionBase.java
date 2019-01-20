// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.cf.imageextraction;

import org.w3c.dom.Document;

public interface ImageExtractionBase {
    String extractMainImage(String html);

    String extractMainImage(Document doc);
}

