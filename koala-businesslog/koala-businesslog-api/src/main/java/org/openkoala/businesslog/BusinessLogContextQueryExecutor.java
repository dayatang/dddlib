package org.openkoala.businesslog;

import org.openkoala.businesslog.config.BusinessLogContextQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: zjzhai
 * Date: 12/4/13
 * Time: 2:33 PM
 */
public interface BusinessLogContextQueryExecutor {

    Map<String, Object> startQuery(Map<String, Object> aContext, BusinessLogContextQuery... queries);
}
