// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.cf.textextraction;

import com.cfta.cf.util.CFTASettings;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

// Boilerpipe text extractor
public class BoilerpipeExtraction implements TextExtractionBase {

    @Override
    // Extracts text using Boilerpipe's ArticleExtractor
    public String extractText(String html) {
        String text = "";
        try {
            text = ArticleExtractor.INSTANCE.getText(html);
        } catch (Exception ex) {
            if (CFTASettings.getDebug()) {
                ex.printStackTrace();
            }
        }
                        
        return text;
    }    
}
