package org.openkoala.businesslog;

import java.util.Map;

/**
 * User: zjzhai
 * Date: 11/29/13
 * Time: 4:20 PM
 */
public interface BusinessLogRender {



    public String render(Map<String, Object> context, String... templates);

}
