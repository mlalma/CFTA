// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.ta.handlers.protocol;

import java.util.ArrayList;
import java.util.List;

// Named entities
public class NamedEntityResponse {

    public class NamedEntity {
        public String entity = "";
        public int count = 0;
    }

    public static final int RESPONSE_OK = 0;
    public static final int RESPONSE_FAIL = -1;

    public int errorCode = RESPONSE_OK;
    public List<NamedEntity> entities = new ArrayList<>();

    public NamedEntity newEntity() {
        return new NamedEntity();
    }
}
