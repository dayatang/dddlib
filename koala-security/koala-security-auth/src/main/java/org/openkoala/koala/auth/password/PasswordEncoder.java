package org.openkoala.koala.auth.password;

/**
 * 密码加密接口
 * @author Ken
 * @date 2013年12月23日 上午11:17:46
 *
 */
public interface PasswordEncoder {

	/**
	 * 加密密码
	 * @param password
	 * @return
	 */
	String encode(String password);
	
}
