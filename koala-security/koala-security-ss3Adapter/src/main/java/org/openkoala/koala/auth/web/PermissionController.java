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
		return "";// I18NManager.getMessage(name);
	}

	public SecurityMetadataSource getSecuritySource() {
		return InstanceFactory.getInstance(SecurityMetadataSource.class, "securityMetadataSource");
	}

	public boolean hasPrivilege(String resUrl) {
		if (AuthUserUtil.getLoginUser().isSuper()) {
			return true;
		}
		return getSecuritySource().getResAuthByUseraccount(AuthUserUtil.getLoginUserName(), resUrl);
	}

	public CustomUserDetails getLoginUser() {
		return AuthUserUtil.getLoginUser();
	}

	public String getLoginUsername() {
		return AuthUserUtil.getLoginUserName();
	}

}
