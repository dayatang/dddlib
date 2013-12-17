package org.openkoala.businesslog.impl;

import org.openkoala.businesslog.BusinessLogContextQueryExecutor;
import org.openkoala.businesslog.config.BusinessLogContextQuery;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: zjzhai
 * Date: 12/8/13
 * Time: 11:53 PM
 */
public class BusinessLogDefaultContextQueryExecutor implements BusinessLogContextQueryExecutor {
    @Override
    public Map<String, Object> startQuery(final Map<String, Object> aContext, BusinessLogContextQuery... queries) {
        synchronized (this) {

            if (null == queries) {
                return new HashMap<String, Object>();
            }
            Map<String, Object> result = new HashMap<String, Object>();
            if (null != aContext) {
                result.putAll(aContext);
            }

            for (BusinessLogContextQuery query : queries) {
                if (null == query) {
                    continue;
                }
                Map<String, Object> map = query.queryInContext(result);
                if (null == map) {
                    continue;
                }
                result.putAll(map);
            }

            return result;
        }
    }

}
