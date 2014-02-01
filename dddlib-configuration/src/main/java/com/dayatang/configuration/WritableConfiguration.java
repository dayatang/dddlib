package com.dayatang.configuration;


/**
 * 用于读写全局性配置信息的接口
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public interface WritableConfiguration extends Configuration {
	/**
	 * 持久化配置信息。
	 */
	void save();

}