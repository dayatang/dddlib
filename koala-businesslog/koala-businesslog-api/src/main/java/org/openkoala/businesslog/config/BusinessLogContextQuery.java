package org.openkoala.businesslog.config;

import java.util.List;
import java.util.Map;

/**
 * User: zjzhai
 * Date: 12/3/13
 * Time: 11:34 AM
 */
public interface BusinessLogContextQuery {


    Map<String, Object> queryInContext(Map<String, Object> aContext);


}
