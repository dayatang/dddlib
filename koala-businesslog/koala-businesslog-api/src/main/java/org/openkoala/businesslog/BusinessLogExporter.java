package org.openkoala.businesslog;

import java.util.Map;

/**
 * User: zjzhai
 * Date: 12/1/13
 * Time: 9:36 PM
 */
public interface BusinessLogExporter {
    void export();

    void setLog(String log);

    void setContext(Map<String, Object> context);
}
