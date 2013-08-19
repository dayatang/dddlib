package org.jwebap.toolkit.bytecode;

import org.jwebap.toolkit.bytecode.asm.ASMInjectorStrategy;
import org.jwebap.toolkit.bytecode.asm.MethodInjectHandler;
import org.jwebap.toolkit.bytecode.asm.MethodInjectHandlerFactory;

/**
 * 类静态增强策略
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.5
 * @date  2008-3-7
 * @see ASMInjectorStrategy 基于ASM的注入策略
 * @see MethodInjectHandlerFactory 方法注入工厂
 * @see MethodInjectHandler 方法注入handle
 * 
 */
public interface InjectorStrategy {

    public Class inject(String className,MethodInjectHandlerFactory factory) throws InjectException;

    public Class inject(String className,MethodInjectHandler handler) throws InjectException;

    public Class[] injectPackage(String pckName,MethodInjectHandlerFactory factory) throws InjectException;

    public Class[] injectPackage(String pckName,MethodInjectHandler handler) throws InjectException;

}
