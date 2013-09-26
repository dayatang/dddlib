package org.jwebap.core;

/**
 * 上下文接口
 * <p>
 * 实现一个上下文层级树，这在一些场景是非常有用的，比如：
 * 1)在Component初始化时所应用的上下文属于全局上下文的一个子。
 * 2)视图层可以通过Component的RuntimeContext获取视图所需的组件，而应用级别可以通过全局的上下文获取更多的信息
 * </p>
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.5
 * @date 2007-12-4
 */
public interface Context {

	public Context getParent();
}
