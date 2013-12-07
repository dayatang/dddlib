package org.openkoala.businesslog.impl;

import com.dayatang.domain.InstanceFactory;
import org.openkoala.businesslog.common.FreemarkerProcessor;
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

    private String targetClass;

    private String targetBeanName;

    private String method;

    private List<String> args;

    private Map<String, Object> context;

    public BusinessLogDefaultContextQuery() {
    }

    @Override
    public Map<String, Object> queryInContext(Map<String, Object> aContext) {
        this.context = aContext;

        return null;
    }

    private Object[] convertArgs(List<String> args) {
        if (null == args || args.isEmpty()) {
            return null;
        }
        Object[] result = new Object[args.size()];
        for (int i = 0; i < args.size(); i++) {
            result[i] = convertArg(args.get(i));
        }
        return result;
    }

    private Object convertArg(String arg) {
        if (null == arg) {
            return arg;
        }
        return FreemarkerProcessor.process(arg, context);

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

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    public void setTargetBeanName(String targetBeanName) {
        this.targetBeanName = targetBeanName;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    public String getContextKey() {
        return contextKey;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public String getTargetBeanName() {
        return targetBeanName;
    }

    public String getMethod() {
        return method;
    }

    public List<String> getArgs() {
        return args;
    }
}
