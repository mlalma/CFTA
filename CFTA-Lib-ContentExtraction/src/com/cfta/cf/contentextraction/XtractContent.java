// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.cf.contentextraction;

import com.cfta.cf.imageextraction.XtractMainImage;
import com.cfta.cf.titleextraction.XtractHeader;
import com.cfta.cf.xtract.dom.DocumentPruner;
import com.cfta.cf.xtract.dom.HtmlDocumentParser;
import com.cfta.cf.xtract.dom.HtmlNode;
import com.cfta.cf.xtract.dom.ParsedWebPage;

// Custom Xtract-algorithm for content extraction
// NOT complete!
public class XtractContent implements ContentExtractionBase {

    private ParsedWebPage webPage = null;

    public void prepareWebPage(String html) throws Exception {
        DocumentPruner pruner = new DocumentPruner();

        HtmlDocumentParser domParser = new HtmlDocumentParser();
        domParser.initDOM(html);

        webPage = new ParsedWebPage(domParser.getDocument());
        webPage.parse();
        pruner.doClearFix(webPage.bodyNode);
        webPage.createFeatureValues();
    }

    public String extractMainImage() {
        XtractHeader headerExtractor = new XtractHeader();
        headerExtractor.findHeader(webPage);

        HtmlNode header = headerExtractor.getHeaderNode();
        if (header != null) {
            XtractMainImage imageExtractor = new XtractMainImage();
            imageExtractor.extractImage(webPage, header);

            if (imageExtractor.getMainImage() != null) {
                String url = imageExtractor.getMainImage().attributes.get("src").trim();
                if (url.startsWith("//")) {
                    url = webPage.url.substring(0, webPage.url.indexOf("/")) + url;
                }

                return url;
            }
        }

        return null;
    }

    @Override
    public void extractContent(String html, boolean extractTitle, boolean extractMainImage, boolean extractText) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getTitle() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getMainImageUrl() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getArticleText() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
