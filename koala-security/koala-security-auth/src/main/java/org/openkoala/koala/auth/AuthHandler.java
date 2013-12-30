package org.openkoala.koala.auth;

/**
 * 认证处理器
 * @author zhuyuanbiao
 * @date 2013年12月23日 下午4:11:23
 *
 */
public interface AuthHandler {

	/**
	 * 是否认证通过
	 * @param loginInfo
	 * @return
	 */
	boolean isAuth(AuthInfo loginInfo);
	
}
