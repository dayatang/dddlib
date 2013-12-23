package org.openkoala.businesslog.impl;

import com.dayatang.domain.InstanceFactory;
import org.apache.commons.beanutils.PropertyUtils;
import org.openkoala.businesslog.QueryMethodConvertArgException;
import org.openkoala.businesslog.QueryMethodInvokeException;
import org.openkoala.businesslog.common.ContextQueryHelper;
import org.openkoala.businesslog.config.BusinessLogContextQuery;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: zjzhai
 * Date: 12/4/13
 * Time: 11:35 AM
 */
public class BusinessLogDefaultContextQuery implements BusinessLogContextQuery {

    private String contextKey;

    private String beanClassName;

    private String beanName;

    private String methodSignature;

    /**
     * 方法调用的参数
     */
    private List<String> args;


    public BusinessLogDefaultContextQuery() {
    }

    @Override
    public Map<String, Object> queryInContext(Map<String, Object> aContext) {
        Map<String, Object> context = aContext;
        if (null == context) {
            context = new ConcurrentHashMap<String, Object>();
        }
        Map<String, Object> map = new ConcurrentHashMap<String, Object>();
        map.put(contextKey, invoke(getBean(), getMethodInstance(), getMethodParams(context)));
        return Collections.unmodifiableMap(map);
    }

    private Object[] getMethodParams(Map<String, Object> context) {
        if (null == args || args.isEmpty()) {
            return null;
        }
        Object[] result = new Object[args.size()];
        for (int i = 0; i < getMethodParamTypes().size(); i++) {
            Class clasz = getMethodParamClasses()[i];
            result[i] = convertArg(args.get(i), clasz, context);
        }

        return result;
    }

    private List<String> getMethodParamTypes() {
        return ContextQueryHelper.getMethodParamTypes(methodSignature);
    }


    private String getMethodName() {
        return ContextQueryHelper.getMethodName(methodSignature);
    }

    private Method getMethodInstance() {
        return ContextQueryHelper.getMethodInstanceOf(beanClassName, getMethodName(),
                getMethodParamClasses());
    }

    private Class[] getMethodParamClasses() {
        List<String> types = ContextQueryHelper.getMethodParamTypes(methodSignature);
        return ContextQueryHelper.getMethodParamClasses(types);
    }

    private Object getBean() {
        try {
            if (isStaticMethodWillInvoke()) {
                return getClassOfBean().newInstance();
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        if (null != beanName && !"".equals(beanName.trim())) {
            return InstanceFactory.getInstance(getClassOfBean(), beanName);
        }
        return InstanceFactory.getInstance(getClassOfBean());

    }

    private Class getClassOfBean() {
        try {
            return Class.forName(beanClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isStaticMethodWillInvoke() {
        return Modifier.isStatic(getMethodInstance().getModifiers());
    }


    private Object convertArg(String arg, Class aClass, Map<String, Object> context) {
        return ContextQueryHelper.contextQueryArgConvertStringToObject(arg, aClass, context);
    }


    private Object invoke(Object beanObject, Method method, Object... params) {
        try {
            if (null == params) {
                return method.invoke(beanObject);
            }
            return method.invoke(beanObject, params);
        } catch (IllegalAccessException e) {
            throw new QueryMethodInvokeException(e);
        } catch (InvocationTargetException e) {
            throw new QueryMethodInvokeException(e);
        }
    }

    public void setContextKey(String contextKey) {
        this.contextKey = contextKey;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void setMethodSignature(String methodSignature) {
        this.methodSignature = methodSignature;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    public String getContextKey() {
        return contextKey;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public String getBeanName() {
        return beanName;
    }

    public String getMethodSignature() {
        return methodSignature;
    }

    public List<String> getArgs() {
        return args;
    }
}