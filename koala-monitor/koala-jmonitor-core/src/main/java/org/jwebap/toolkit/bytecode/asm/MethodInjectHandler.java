package org.jwebap.toolkit.bytecode.asm;

import java.lang.reflect.Method;

/**
 * 采用around的方式拦截方法，该方式为默认拦截方式也是最通用的方式
 * @author leadyu
 */
public interface MethodInjectHandler {
	Object invoke(Object target, Method method, Method methodProxy, Object[] args)throws Throwable;

}
