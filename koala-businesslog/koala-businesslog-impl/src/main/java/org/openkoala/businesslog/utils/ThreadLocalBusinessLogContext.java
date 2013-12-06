package org.openkoala.businesslog.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * User: zjzhai
 * Date: 11/28/13
 * Time: 2:18 PM
 */
public class ThreadLocalBusinessLogContext {
    private static Map<String, Object> contextMap = new HashMap<String, Object>();

    private static ThreadLocal<Map<String, Object>> context = new ThreadLocal<Map<String, Object>>() {
        protected synchronized Map<String, Object> initialValue() {
            Map<String, Object> map = new HashMap<String, Object>();
            map.putAll(contextMap);
            return map;
        }
    };

    public static Map<String, Object> get() {
        return context.get();
    }


    public static void put(String key, Object value) {
        contextMap.put(key, value);
        context.set(contextMap);
    }


}
