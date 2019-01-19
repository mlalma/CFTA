// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.textanalysis.statistics;

import java.util.regex.*;

// Originally code is adapted from MorphAdorner (http://morphadorner.northwestern.edu/) by Nic Watson in 2009 for school project
// See license agreement at the end for MorphAdorner's license
// In this version, there are changes to code formatting, but not to functionality
class EnglishSyllableCounter {
    //protected Map syllableCountMap = new HashMap();

    private static final Pattern[] SubtractSyllables =
            new Pattern[]{
                    Pattern.compile("cial"),
                    Pattern.compile("tia"),
                    Pattern.compile("cius"),
                    Pattern.compile("cious"),
                    Pattern.compile("giu"),                 // belgium!
                    Pattern.compile("ion"),
                    Pattern.compile("iou"),
                    Pattern.compile("sia$"),
                    Pattern.compile(".ely$")                // absolutely! (but not ely!)
            };

    private static final Pattern[] AddSyllables =
            new Pattern[]
                    {
                            Pattern.compile("ia"),
                            Pattern.compile("riet"),
                            Pattern.compile("dien"),
                            Pattern.compile("iu"),
                            Pattern.compile("io"),
                            Pattern.compile("ii"),
                            Pattern.compile("[aeiouym]bl$"),        // -Vble, plus -mble
                            Pattern.compile("[aeiou]{3}"),          // agreeable
                            Pattern.compile("^mc"),
                            Pattern.compile("ism$"),                // -isms
                            Pattern.compile("([^aeiouy])\1l$"),     // middle twiddle battle bottle, etc.
                            Pattern.compile("[^l]lien"),            // alien, salient [1]
                            Pattern.compile("^coa[dglx]."),         // [2]
                            Pattern.compile("[^gq]ua[^auieo]"),    // i think this fixes more than it breaks
                            Pattern.compile("dnt$")                 // couldn't
                    };

    // Constructor
    EnglishSyllableCounter() {
    }

    // Calculates syllable count for a word
    int countSyllables(String word) {
        int result = 0;
        if ((word == null) || (word.length() == 0)) {
            return result;
        }
        String lcWord = word.toLowerCase();
        lcWord = lcWord.replaceAll("'", "").replaceAll("e$", "");
        String[] vowelGroups = lcWord.split("[^aeiouy]+");
        for (Pattern p : SubtractSyllables) {
            Matcher m = p.matcher(lcWord);
            if (m.find()) {
                result--;
            }
        }

        for (Pattern p : AddSyllables) {
            Matcher m = p.matcher(lcWord);
            if (m.find()) {
                result++;
            }
        }

        if (lcWord.length() == 1) {
            result++;
        }

        if ((vowelGroups.length > 0) && (vowelGroups[0].length() == 0)) {
            result += vowelGroups.length - 1;
        } else {
            result += vowelGroups.length;
        }
        return Math.max(result, 1);
    }
}




