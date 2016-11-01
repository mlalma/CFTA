// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.ta.handlers;

import com.cfta.log.CFTALog;
import com.cfta.ta.handlers.protocol.POSTagRequest;
import com.cfta.ta.handlers.protocol.POSTagResponse;
import com.cfta.textnormalization.stanfordnlp.POSTag;
import com.cfta.textnormalization.stanfordnlp.POSTagger;
import com.google.gson.Gson;
import java.util.List;
import spark.Request;
import spark.Response;
import spark.Route;

// Part-of-speech tagging handler
public class POSHandler extends Route {

    private Gson gson = new Gson();

    // Constructor
    public POSHandler(String route) {
        super(route);
    }

    @Override
    // Handles POS tagging
    public Object handle(Request request, Response response) {
        long startTime = System.currentTimeMillis();        
        String resultString;
        response.type("application/json");
        POSTagResponse responseData = new POSTagResponse();
        
        try {
            POSTagRequest pRequest = gson.fromJson(request.body().trim(), POSTagRequest.class);
            POSTagger tagger = new POSTagger();
            
            List<POSTag> tags = tagger.POSTagText(pRequest.text, pRequest.newlineAsParagraphSeparation);
            for (POSTag t : tags) {
                POSTagResponse.POSTag tag = responseData.newTag();
                tag.tag = t.tag;
                tag.word = t.word;
                tag.lemma = t.lemma;
                responseData.tags.add(tag);
            }
            
            responseData.errorCode = POSTagResponse.RESPONSE_OK;            
        } catch (Exception ex) {
            responseData.errorCode = POSTagResponse.RESPONSE_FAIL;
        }
                
        CFTALog.LL("Part-of-speech tagging request done, took " + (System.currentTimeMillis() - startTime) + "ms");
        resultString = gson.toJson(responseData, POSTagResponse.class);
        return resultString;
    }
}
