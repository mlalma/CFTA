package com.cfta.cf.imageextraction;

import com.cfta.cf.xtract.dom.HtmlNode;
import com.cfta.cf.xtract.dom.ParsedWebPage;
import java.util.ArrayList;

// Custom Xtract-algorithm for content extraction
// NOT complete!
public class XtractMainImage {
    
    private double GOOD_IMAGE_SIZE = 640.0 * 480.0;
        
    private ArrayList<HtmlNode> potentialImages = new ArrayList<>();
    private ArrayList<Double> imageScores = new ArrayList<>();
    private HtmlNode mainImage = null;
    
    private void gatherAllArticleImages(HtmlNode n) {
        if (n.isArticleImage()) {
            potentialImages.add(n);
            return;
        }
        
        int i = 0;
        while (i < n.childNodes.size()) {
            gatherAllArticleImages(n.childNodes.get(i));
            i++;
        }
    }
    
    private boolean isAlignedWithHeader(HtmlNode header, HtmlNode image) {
        int imageCenter = image.xPos + image.width / 2;
        if (imageCenter > header.xPos && imageCenter < (header.xPos + header.width)) {
            return true;
        }
        return false;
    }

    private boolean isBelowHeader(HtmlNode header, HtmlNode image) {
        System.out.println(image.xPos + " " + image.yPos + " - " + header.yPos + " " + header.height);
        if (image.yPos > (header.yPos + header.height/2)) {
            return true;
        } else {
            return false;
        }
    }
    
    private void scoreImages(HtmlNode header) {
        for (int i = 0; i < potentialImages.size(); i++) {
            HtmlNode image = potentialImages.get(i);
            //int xPosDiff = Math.abs((image.xPos + image.width/2) - (header.xPos + header.width/2));
            if (isAlignedWithHeader(header, image)) {
                if (isBelowHeader(header, image)) {
                    double image_size_points = (double)(image.height * image.width) / GOOD_IMAGE_SIZE;
                    if (image_size_points > 1.0) {
                        image_size_points = 1.0;
                    }
                    
                    double yposDiff = image.yPos - (header.yPos + header.height);
                    double image_position_points = 1.0 - (yposDiff / 1500.0);
                    if (image_position_points < 0.0) {
                        image_position_points = 0.0;
                    }
                    
                    imageScores.add(image_position_points + image_size_points);                    
                } else {
                    imageScores.add(0.0);
                }
            } else {
                imageScores.add(0.0);
            }
        }
    }
    
    private void chooseMainImage() {
        double bestScore = 0.1;
        for (int i = 0; i  < imageScores.size(); i++) {
            if (imageScores.get(i) > bestScore) {
                bestScore = imageScores.get(i);
                mainImage = potentialImages.get(i);
            }
        }
    }
    
    public void extractImage(ParsedWebPage webPage, HtmlNode header) {
        potentialImages.clear();
        imageScores.clear();
        mainImage = null;
        
        gatherAllArticleImages(webPage.bodyNode);
        if (potentialImages.size() > 0) {
            scoreImages(header);
            chooseMainImage();
        }
    }
    
    public HtmlNode getMainImage() {
        return mainImage;
    }
}
