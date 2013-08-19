package org.openkoala.koala.monitor.toolkit.asm;

public interface MethodInjectHandlerFactory {
	
	public MethodInjectHandler getMethodHandler(String className, String methodName, String signature);
   
}
