// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.textanalysis.language;

import com.cfta.cf.util.CFTASettings;
import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

// Uses Nakatani Shyuo's / Cybozu Labs's Language Detection Library for Java (https://github.com/shuyo/language-detection)
public class LanguageDetection implements LangDetectionBase {

    // Constructor
    public LanguageDetection() throws LangDetectException {
        DetectorFactory.loadProfile(CFTASettings.getLangDetectProfileDir());
    }

    @Override
    // Recognizes language of the text and returns two-char code of the language
    public String getLanguage(String text) {
        String language = "";
        Detector langDetector;

        try {
            langDetector = DetectorFactory.create();
            langDetector.append(text);
            language = langDetector.detect();
        } catch (Exception ex) {
            if (CFTASettings.getDebug()) {
                ex.printStackTrace();
            }
        }

        return language;
    }
}
