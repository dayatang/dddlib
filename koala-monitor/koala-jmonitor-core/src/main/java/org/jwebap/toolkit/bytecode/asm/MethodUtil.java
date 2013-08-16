package org.jwebap.toolkit.bytecode.asm;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

import org.jwebap.asm.Type;

/**
 * 字节码注入帮助类
 * 
 * 注入的字节码会通过findMethod方法找到自己的原方法，和新产生的代理方法
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.5
 * @date  2008-3-5
 * @see ProxyMethodVisitor
 */
public class MethodUtil {

	public static  Method findMethod(String className, String methodName, String signature) throws SecurityException, NoSuchMethodException {
        
    	Method method=null;
    	ClassLoader loader =Thread.currentThread().getContextClassLoader();
    	try {
			Class clz=loader.loadClass(className);
			Class[] argsClz=parseTypeToClass(Type.getArgumentTypes(signature));
			method=clz.getDeclaredMethod(methodName,argsClz);
			method.setAccessible(true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    	
    	
    	return method;
    }
    
    private static Class[] parseTypeToClass(Type[] types) throws ClassNotFoundException{
    	Class[] clzs=new Class[types.length];
    	
    	for(int i=0;i<clzs.length;i++){
    		Type type=types[i];
    		clzs[i]=getClass(type.getClassName());
    	}
    	
    	return clzs;
    }
    
    public static Class  getClass(String clzName) throws ClassNotFoundException {
    	ClassLoader loader =Thread.currentThread().getContextClassLoader();
        if("void".equals(clzName)) {
        	return null;
        }else if ("boolean".equals(clzName)) {
        	return boolean.class;
        }else if ("char".equals(clzName)) {
        	return char.class;
        }else if ("byte".equals(clzName)) {
        	return byte.class;
        }else if ("short".equals(clzName)) {
        	return short.class;
        }else if ("int".equals(clzName)) {
        	return int.class;
        }else if ("float".equals(clzName)) {
        	return float.class;
        }else if ("long".equals(clzName)) {
        	return long.class;
        }else if ("double".equals(clzName)) {
        	return double.class;
        }else if (clzName.indexOf("[")>0) {
        	String className=clzName.substring(0,clzName.lastIndexOf("["));
        	Class c=Array.newInstance(getClass(className),0).getClass();
        	return c;
        }else{
            return loader.loadClass(clzName);
        }
      }

}
