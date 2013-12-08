package org.openkoala.businesslog.impl;

import com.dayatang.domain.InstanceFactory;
import org.openkoala.businesslog.common.MethodProcesser;
import org.openkoala.businesslog.config.BusinessLogContextQuery;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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

    private String beanClassName;

    private String beanName;

    private String methodSignature;

    private List<String> args;

    private Map<String, Object> context;


    public BusinessLogDefaultContextQuery() {
    }

    @Override
    public Map<String, Object> queryInContext(Map<String, Object> aContext) {
        context = new HashMap<String, Object>();
        if (null != aContext) {
            context.putAll(aContext);
        }
        context.put(contextKey, invoke(getMethodName(), getMethodParams()));
        return context;
    }

    private Object[] getMethodParams() {
        if (null == args || args.isEmpty()) {
            return null;
        }
        Object[] result = new Object[args.size()];
        for (int i = 0; i < getMethodParamTypes().size(); i++) {
            String type = getMethodParamTypes().get(i);
            result[i] = convertArg(args.get(i), type);
        }
        return result;
    }

    private List<String> getMethodParamTypes() {
        List<String> result = new ArrayList<String>();
        for (String paramName : methodSignature.substring(methodSignature.indexOf('(') + 1, methodSignature.indexOf(')')).split(",")) {
            result.add(paramName);
        }
        return result;
    }


    private String getMethodName() {
        return methodSignature.substring(0, methodSignature.indexOf('('));
    }

    private Method getMethod() {
        try {
            return Class.forName(beanClassName).getMethod(getMethodName(), MethodProcesser.getClassTypeOf(getMethodParamTypes()));
        } catch (NoSuchMethodException e) {
            new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            new RuntimeException(e);
        }
        return null;
    }

    private Object getBean() {
        if (MethodProcesser.isStaticMethod(getClassOfBean(), getMethodName(),
                MethodProcesser.getClassTypeOf(getMethodParamTypes()))) {
            try {
                return getClassOfBean().newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
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


    private Object convertArg(String arg, String type) {
        if (null == arg) {
            return arg;
        }
        if ("long".equals(type)) {
            return Long.parseLong(arg);
        } else if ("String".equals(type) || "java.lang.String".equals(type)) {
            return arg;
        } else {
            try {
                String key = arg.substring(arg.indexOf("{") +1, arg.lastIndexOf("}"));
                return Class.forName(type).cast(context.get(key));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }


    private Object invoke(Object methodObject, Object... params) {
        try {
            if (null == params) {
                return getMethod().invoke(methodObject);
            }
            return getMethod().invoke(methodObject, params);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
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
