package org.openkoala.businesslog.common;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * User: zjzhai
 * Date: 12/8/13
 * Time: 9:16 PM
 */
public class MethodProcesser {

    private final static String LONG = "long";
    private final static String STRING = "String";
    private final static String INT = "int";


    public static Class[] getClassTypeOf(List<String> args) {
        List<Class> classes = new ArrayList<Class>();
        for (String arg : args) {
            if (LONG.equals(arg)) {
                classes.add(long.class);
                continue;
            } else if (STRING.equals(arg) || "java.lang.String".equals(arg)) {
                classes.add(String.class);
            } else {
                try {

                    classes.add(Class.forName(arg));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return classes.toArray(new Class[classes.size()]);
    }

    public static boolean isStaticMethod(Class clasz, String methodName, Class[] params) {
        try {
            return Modifier.isStatic(
                    clasz.getMethod(methodName, params).getModifiers());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
