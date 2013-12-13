package org.openkoala.businesslog;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * User: zjzhai
 * Date: 12/12/13
 * Time: 9:35 AM
 */

public class BusinessLog {

    private String log;

    private Map<String, Object> context = new HashMap<String, Object>();
    private String category;

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


    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
