package org.openkoala.koala.auth.impl.ejb;

import java.util.List;
import java.util.Map;

import org.openkoala.koala.auth.UserDetails;

/**
 * 获取鉴权信息接口
 * @author zyb
 * 2013-7-2 上午11:56:18
 *
 */
public interface SecurityManager {
	
	/**
	 * 根据用户账号获取用户信息
	 * @param userAccount
	 * @return
	 */
	UserDetails getUser(String userAccount);
	
	/**
	 * 获取所有资源的角色授权
	 * @return
	 */
	Map<String, List<String>> getAllReourceAndRoles();
	
	/**
	 * 根据用户账号获取用户所拥有的角色
	 * @param userAccount
	 * @return
	 */
	List<String> getUserRoles(String userAccount);
	
}
