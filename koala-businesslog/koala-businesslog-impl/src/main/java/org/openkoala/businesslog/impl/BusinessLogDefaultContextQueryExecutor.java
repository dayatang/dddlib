package org.openkoala.businesslog.impl;

import org.openkoala.businesslog.BusinessLogContextQueryExecutor;
import org.openkoala.businesslog.config.BusinessLogContextQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * User: zjzhai
 * Date: 12/8/13
 * Time: 11:53 PM
 */
public class BusinessLogDefaultContextQueryExecutor implements BusinessLogContextQueryExecutor {
    @Override
    public Map<String, Object> startQuery(Map<String, Object> aContext, BusinessLogContextQuery... queries) {
        if (null == queries) {
            return aContext;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        if (null != aContext) {
            result.putAll(aContext);
        }

        for (BusinessLogContextQuery query : queries) {
            result.putAll(query.queryInContext(result));
        }

        return result;
    }
}
