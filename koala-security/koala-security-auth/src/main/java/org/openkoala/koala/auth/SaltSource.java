package org.openkoala.koala.auth;

/**
 * 盐值来源
 * @author zhuyuanbiao
 * @date Dec 27, 2013 10:57:20 AM
 *
 */
public interface SaltSource {

	/**
	 * 获取盐值
	 * @return
	 */
	Object getSalt();
	
}
