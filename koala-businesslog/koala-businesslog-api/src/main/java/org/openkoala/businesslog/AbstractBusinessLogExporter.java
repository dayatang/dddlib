package org.openkoala.businesslog;

import java.util.Map;

/**
 * User: zjzhai
 * Date: 12/11/13
 * Time: 10:20 AM
 */
public abstract class AbstractBusinessLogExporter implements BusinessLogExporter {

    private String log;
    private Map<String, Object> context;

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
