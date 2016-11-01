// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.cf.textextraction;

// Naive text filtering of text that has been extracted by Boilerpipe -- reduces "garbage" at the beginning & end of the text
public class NaiveTextFilter {
        
    // Naive filtering of text from Boilerpipe's extraction based on the fact that at the beginning / end of text there are "garbage" lines of text
    // "garbage" line is a text line that is short and does not end to a ?!. mark
    public String doNaiveFiltering(String text, String title) {
        String lines[] = text.split("\n");
        int MIN_SENTENCE_LENGTH = 120;
        
        for (int i = 0; i < lines.length; i++) {
            String s = lines[i].replaceAll("\n", " ").replaceAll(" ", " ").replaceAll("( )+", " ");
            s = s.trim();
            if (s.endsWith(".") || s.endsWith("!") || s.endsWith("?") || s.endsWith(".\"") || s.endsWith("!\"") || s.endsWith("?\"")) {
                if (title == null) {
                    break;
                } else if (title != null && !title.equalsIgnoreCase(s)) {
                    break;
                } else {
                    lines[i] = "";
                }
            } else {
                if (s.trim().length() > MIN_SENTENCE_LENGTH) {
                    if (title == null) {
                        break;
                    } else  if (title != null && !title.equalsIgnoreCase(s)) {
                        break;
                    } else {
                        lines[i] = "";
                    }
                } else {
                    lines[i] = "";
                }
            }
        }
        
        for (int i = lines.length - 1; i >= 0; i--) {
            String s = lines[i].replaceAll("\n", " ").replaceAll(" ", " ").replaceAll("( )+", " ");
            s = s.trim();
            if (s.endsWith(".") || s.endsWith("!") || s.endsWith("?") || s.endsWith(".\"") || s.endsWith("!\"") || s.endsWith("?\"")) {
                break;
            } else {
                if (s.trim().length() > MIN_SENTENCE_LENGTH) {
                    break;
                } else {
                    lines[i] = "";
                }
            }
        }
        
        String newText = "";
        for (int i = 0; i < lines.length; i++) {
            newText = newText + lines[i] + "\n";
        }
        
        return newText.replaceAll(" ", " ").replaceAll("  ", " ").trim();
    }
}
