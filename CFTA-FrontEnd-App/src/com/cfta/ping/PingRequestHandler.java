// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.ping;

import com.cfta.cf.util.CFTASettings;
import com.cfta.log.CFTALog;
import com.cfta.ping.protocol.PingResponse;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;


// Simple ping request that can be used to test if the server is up and running
public class PingRequestHandler extends Route {

    private Gson gson = new Gson();            

    public PingRequestHandler(String path) {
        super(path);
    }
    
    @Override
    public Object handle(Request request, Response response) {
        CFTALog.LL("Ping request");        
        try {
            PingResponse respData = new PingResponse();
            respData.result = "pong";
            
            String resultString = gson.toJson(respData, PingResponse.class);
            response.type("application/json");
            return resultString;            
        } catch (Exception ex) {
            if (CFTASettings.getDebug()) {
                ex.printStackTrace();
            }
        }        
        return "";        
    }    
}
