package org.openkoala.businesslog.common;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.openkoala.businesslog.BusinessLogClassNotFoundException;
import org.openkoala.businesslog.BusinessLogQueryMethodException;
import org.openkoala.businesslog.QueryMethodConvertArgException;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: zjzhai
 * Date: 12/10/13
 * Time: 2:31 PM
 */
public class ContextQueryHelper {

    public static List<String> getMethodParamTypes(String methodSignature) {
        List<String> result = new ArrayList<String>();
        String params = methodSignature.substring(methodSignature.indexOf('(') + 1, methodSignature.indexOf(')'));
        if (null == params || "".equals(params.trim())) {
            return result;
        }
        for (String paramName : params.split(",")) {
            result.add(paramName);
        }
        return result;
    }

    public static String getMethodName(String methodSignature) {
        return methodSignature.substring(0, methodSignature.indexOf('('));
    }

    public static Method getMethodInstanceOf(String beanClassName, String methodName, Class[] paramsClasses) {
        Method result = null;

        try {
            if (null == paramsClasses) {
                result = Class.forName(beanClassName).getMethod(methodName);
            } else {
                result = Class.forName(beanClassName).getMethod(methodName, paramsClasses);
            }
            if (null == result) {
                throw new BusinessLogQueryMethodException(MessageFormat.format("Not found {0}\'s {1}", beanClassName, methodName));
            }
        } catch (NoSuchMethodException e) {
            throw new BusinessLogQueryMethodException(e);
        } catch (ClassNotFoundException e) {
            throw new BusinessLogQueryMethodException(e);
        }
        return result;
    }

    // TODO 待重构 未对多维数组进行支持
    public static Class[] getMethodParamClasses(List<String> methodParamTypes) {
        List<Class> classes = new ArrayList<Class>();
        for (String type : methodParamTypes) {
            if ("long".equals(type)) {
                classes.add(long.class);
                continue;
            } else if ("Long".equals(type) || "java.lang.Long".equals(type)) {
                classes.add(Long.class);
                continue;
            } else if ("String".equals(type) || "java.lang.String".equals(type)) {
                classes.add(String.class);
                continue;
            } else if ("double".equals(type)) {
                classes.add(double.class);
                continue;
            } else if ("Double".equals(type) || "java.lang.Double".equals(type)) {
                classes.add(Double.class);
                continue;

            } else if ("boolean".equals(type)) {
                classes.add(boolean.class);
                continue;

            } else if ("Boolean".equals(type) || "java.lang.Boolean".equals(type)) {
                classes.add(Boolean.class);
                continue;

            } else if ("char".equals(type)) {
                classes.add(char.class);
                continue;
            } else if ("Character".equals(type) || "java.lang.Character".equals(type)) {
                classes.add(Character.class);
                continue;
            } else if ("int".equals(type)) {
                classes.add(int.class);
                continue;
            } else if ("Integer".equals(type) || "java.lang.Integer".equals(type)) {
                classes.add(Integer.class);
                continue;
            } else if ("float".equals(type)) {
                classes.add(float.class);
                continue;
            } else if ("Float".equals(type) || "java.lang.Float".equals(type)) {
                classes.add(Float.class);
                continue;
            } else {
                try {
                    if (type.contains("[") && type.contains("]")) {
                        type = type.substring(0, type.indexOf("["));
                        classes.add(Array.newInstance(Class.forName(type), 0).getClass());
                    } else {
                        classes.add(Class.forName(type));
                    }
                } catch (ClassNotFoundException e) {
                    throw new BusinessLogClassNotFoundException(e);
                }
            }
        }
        return classes.toArray(new Class[classes.size()]);
    }


    public static Object contextQueryArgConvertStringToObject(String arg, Class aClass,
                                                              Map<String, Object> context) {
        if (null == arg) {
            return arg;
        }
        if (arg.contains("$")) {
            if (arg.contains(".")) {
                String key = arg.substring(arg.indexOf("{") + 1, arg.indexOf("."));
                Object bean = context.get(key);
                try {
                    return PropertyUtils.getNestedProperty(bean, arg.substring(arg.indexOf(".") + 1, arg.lastIndexOf("}")));
                } catch (IllegalAccessException e) {
                    throw new QueryMethodConvertArgException(e);
                } catch (InvocationTargetException e) {
                    throw new QueryMethodConvertArgException(e);
                } catch (NoSuchMethodException e) {
                    throw new QueryMethodConvertArgException(e);
                }
            } else {
                return context.get(arg.substring(arg.indexOf("{") + 1, arg.lastIndexOf("}")));
            }

        } else {
            if (aClass.equals(Long.class) || aClass.equals(long.class)) {
                return Long.parseLong(arg);
            } else if (aClass.equals(String.class)) {
                return arg;
            } else if (aClass.equals(Date.class)) {
                return Date.parse(arg);
            } else if (aClass.equals(float.class) || aClass.equals(Float.class)) {
                return Float.valueOf(arg);
            }
        }


        return null;
    }

}
