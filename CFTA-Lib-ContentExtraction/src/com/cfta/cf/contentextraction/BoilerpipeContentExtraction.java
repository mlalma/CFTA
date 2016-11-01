// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2014
package com.cfta.cf.contentextraction;

import com.cfta.cf.textextraction.BoilerpipeExtraction;
import com.cfta.cf.textextraction.NaiveTextFilter;
import com.cfta.cf.textextraction.TextExtractionBase;
import com.cfta.cf.titleextraction.XtractTitleExtractorNoLayout;

// Extract the text and content using boilerpipe algorithm
public class BoilerpipeContentExtraction implements ContentExtractionBase {

    private XtractTitleExtractorNoLayout titleExtractor =  null;
    private TextExtractionBase textExtractor = null;
    private NaiveTextFilter textExtractionPostProcessor = null;
    
    private String title = "";
    private String text = "";
    
    // Constructor
    public BoilerpipeContentExtraction() {
        titleExtractor = new XtractTitleExtractorNoLayout();
        textExtractor = new BoilerpipeExtraction();
        textExtractionPostProcessor = new NaiveTextFilter();
    }
    
    @Override
    // Runs boilerpipe + custom title fetching algorithm
    public void extractContent(String html, boolean extractTitle, boolean extractMainImage, boolean extractText) {
        title = "";
        text = "";
                
        if (extractTitle) {
            title = titleExtractor.extractTitle(html);
        }

        if (extractText) {
            text = textExtractor.extractText(html);
        
            if (title.length() > 0) {
                text = textExtractionPostProcessor.doNaiveFiltering(text, title);
            } else {
                text = textExtractionPostProcessor.doNaiveFiltering(text, null);                        
            }
        }
    }    
    
    @Override
    // Returns extracted title of an article
    public String getTitle() {
        return title;
    }
    
    @Override
    // Extracting article's main image is not supported on Boilerpipe
    public String getMainImageUrl() {
        return "";
    }
    
    @Override
    // Returns extracted text of an article
    public String getArticleText() {
        return text;
    }

}
