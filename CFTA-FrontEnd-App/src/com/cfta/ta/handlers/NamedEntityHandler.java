// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.ta.handlers;

import com.cfta.log.CFTALog;
import com.cfta.ta.handlers.protocol.NamedEntityRequest;
import com.cfta.ta.handlers.protocol.NamedEntityResponse;
import com.cfta.textnormalization.stanfordnlp.NamedEntity;
import com.cfta.textnormalization.stanfordnlp.NamedEntityExtractor;
import com.google.gson.Gson;

import java.util.List;

import spark.Request;
import spark.Response;
import spark.Route;

// Named entity extraction
public class NamedEntityHandler implements Route {

    private Gson gson = new Gson();

    // Constructor
    public NamedEntityHandler() {
    }

    @Override
    // Handles named entity extraction requests
    public Object handle(Request request, Response response) {
        long startTime = System.currentTimeMillis();
        String resultString;
        response.type("application/json");

        NamedEntityResponse responseData = new NamedEntityResponse();

        try {
            NamedEntityRequest nRequest = gson.fromJson(request.body().trim(), NamedEntityRequest.class);
            NamedEntityExtractor entityExtractor = new NamedEntityExtractor();
            List<NamedEntity> nEs = entityExtractor.extractEntities(nRequest.text, true);

            for (NamedEntity n : nEs) {
                NamedEntityResponse.NamedEntity nE = responseData.newEntity();
                nE.count = n.count;
                nE.entity = n.name;
                responseData.entities.add(nE);
            }

            responseData.errorCode = NamedEntityResponse.RESPONSE_OK;
        } catch (Exception ex) {
            responseData.errorCode = NamedEntityResponse.RESPONSE_FAIL;
        }

        CFTALog.LL("Named entity extraction request done, took " + (System.currentTimeMillis() - startTime) + "ms");
        resultString = gson.toJson(responseData, NamedEntityResponse.class);
        return resultString;
    }
}
