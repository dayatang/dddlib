package org.openkoala.businesslog.impl;

import org.openkoala.businesslog.config.BusinessLogContextQuery;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: zjzhai
 * Date: 12/4/13
 * Time: 11:35 AM
 */
public class BusinessLogDefaultContextQuery implements BusinessLogContextQuery {

    private String contextKey;

    private String bean;

    private String method;

    private List<String> args;

    public BusinessLogDefaultContextQuery() {
    }

    @Override
    public Map<String, Object> queryInContext(Map<String, Object> aContext) {


        return null;
    }

    private Object invoke(Object methodObject, String methodName, Object[] args) {
        Class ownerClass = methodObject.getClass();
        Class[] argsClass = new Class[args.length];
        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }
        try {
            Method method = ownerClass.getMethod(methodName, argsClass);
            return method.invoke(methodObject, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void setContextKey(String contextKey) {
        this.contextKey = contextKey;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }
}
