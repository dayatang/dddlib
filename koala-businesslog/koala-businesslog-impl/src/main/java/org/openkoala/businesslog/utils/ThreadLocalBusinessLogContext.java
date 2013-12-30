package org.openkoala.businesslog.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: zjzhai
 * Date: 11/28/13
 * Time: 2:18 PM
 */
public class ThreadLocalBusinessLogContext {
    //private static Map<String, Object> contextMap = new HashMap<String, Object>();

    private static ThreadLocal<Map<String, Object>> context = new ThreadLocal<Map<String, Object>>() {
        protected synchronized Map<String, Object> initialValue() {
            return new HashMap<String, Object>();
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
        context.get().put(key, value);
    }


    public static void clear() {
        //contextMap.clear();
        context.set(new HashMap<String, Object>());
    }
}
