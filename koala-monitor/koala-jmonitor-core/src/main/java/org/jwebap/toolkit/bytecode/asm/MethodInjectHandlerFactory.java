package org.jwebap.toolkit.bytecode.asm;

public interface MethodInjectHandlerFactory {
	
	public MethodInjectHandler getMethodHandler(String className, String methodName, String signature);
   
}
