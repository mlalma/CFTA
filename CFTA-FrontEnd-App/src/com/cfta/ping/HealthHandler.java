// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.ping;

import spark.Request;
import spark.Response;
import spark.Route;

// Health handler for supporting easy scaling using e.g. ELB, SmartStack or similar
public class HealthHandler extends Route {
    
    // HTTP OK status code
    final int RESPONSE_OK = 200;
    
    // Constructor
    public HealthHandler(String route) {
        super(route);
    }

    @Override
    // Returns all OK for the load balancing unit
    public Object handle(Request request, Response response) {
        response.status(RESPONSE_OK);
        return "OK";
    }
    
}
