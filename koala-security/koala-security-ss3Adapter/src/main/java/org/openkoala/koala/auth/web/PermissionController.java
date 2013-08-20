package org.openkoala.koala.auth.web;

import javax.inject.Named;

//import org.openkoala.framework.i18n.I18NManager;
import org.openkoala.koala.auth.ss3adapter.AuthUserUtil;
import org.openkoala.koala.auth.ss3adapter.CustomUserDetails;
import org.openkoala.koala.auth.ss3adapter.SecurityMetadataSource;

import com.dayatang.domain.InstanceFactory;

@Named("owner")
public class PermissionController {

	public PermissionController() {

	}

	public String getMessage(String name) {
		return "";
	}

	/**
	 * 获取SecurityMetadataSource实例 
	 * @return
	 */
	public SecurityMetadataSource getSecuritySource() {
		return InstanceFactory.getInstance(SecurityMetadataSource.class, "securityMetadataSource");
	}

	/**
	 * 根据一个资源标识判断用户是否有权限
	 * @param identifier
	 * @return
	 */
	public boolean hasPrivilege(String identifier) {
		if (AuthUserUtil.getLoginUser().isSuper()) {
			return true;
		}
		return getSecuritySource().getResAuthByUseraccount(AuthUserUtil.getLoginUserName(), identifier);
	}

	/**
	 * 获取登录用户
	 * @return
	 */
	public CustomUserDetails getLoginUser() {
		return AuthUserUtil.getLoginUser();
	}

	/**
	 * 获取登录用户名
	 * @return
	 */
	public String getLoginUsername() {
		return AuthUserUtil.getLoginUserName();
	}

}
