package org.openkoala.businesslog;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * User: zjzhai
 * Date: 12/12/13
 * Time: 9:35 AM
 */

public class RenderResult {

    private String log;

    private Map<String, Object> context = new HashMap<String, Object>();

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Map<String, Object> getContext() {
        return Collections.unmodifiableMap(context);
    }


    public void addContext(Map<String, Object> aContext) {
        context.putAll(aContext);
    }


}
