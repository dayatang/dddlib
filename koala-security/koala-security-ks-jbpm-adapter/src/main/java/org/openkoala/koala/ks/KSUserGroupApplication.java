package org.openkoala.koala.ks;

import javax.jws.WebService;

/**
 * KS权限系统提供的用户和角色关系的实现
 * @author lingen
 *
 */
@WebService
public interface KSUserGroupApplication {

	public String getGroupsByUser(String user);
	
	public String getUsersByGroup(String group);
}
