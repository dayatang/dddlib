package org.openkoala.businesslog.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * User: zjzhai
 * Date: 11/28/13
 * Time: 2:18 PM
 */
public class ThreadLocalBusinessLogContext {
    //private static Map<String, Object> contextMap = new HashMap<String, Object>();

    private static ThreadLocal<Map<String, Object>> context = new ThreadLocal<Map<String, Object>>() {
        protected synchronized Map<String, Object> initialValue() {
            Map<String, Object> map = new HashMap<String, Object>();
            Map contextMap = new HashMap<String, Object>();
            map.putAll(contextMap);
            return map;
        }
    };

    public static Map<String, Object> get() {
        Map<String, Object> result = new HashMap<String, Object>();
        for (String key : context.get().keySet()) {
            result.put(key, context.get().get(key));
        }
        return result;
    }


    public static void put(String key, Object value) {
    	Map contextMap = new HashMap<String, Object>();
        contextMap.put(key, value);
        context.set(contextMap);
    }


    public static void clear() {
        //contextMap.clear();
        context.set(new HashMap<String, Object>());
    }
}
