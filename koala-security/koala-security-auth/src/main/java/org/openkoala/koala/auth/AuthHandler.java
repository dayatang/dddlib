package org.openkoala.koala.auth;

/**
 * 认证处理器
 * @author zhuyuanbiao
 * @date 2013年12月23日 下午4:11:23
 *
 */
public interface AuthHandler {

	/**
	 * 认证
	 * @param useraccount
	 * @param password
	 * @return
	 */
	UserDetails authenticate(String useraccount, String password);
	
}
