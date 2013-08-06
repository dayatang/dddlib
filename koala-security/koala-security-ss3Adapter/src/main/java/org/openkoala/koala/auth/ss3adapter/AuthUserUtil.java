package org.openkoala.koala.auth.ss3adapter;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUserUtil {

	/**
	 * 获取当前登录用户名
	 * 
	 * @return
	 */
	public static String getLoginUserName() {
		if (getAuthentication() == null) {
			return null;
		}
		if (getPrincipal() != null && getPrincipal() instanceof CustomUserDetails) {
			return ((CustomUserDetails) getPrincipal()).getUsername();
		}
		return null;
	}

	private static Object getPrincipal() {
		return getAuthentication().getPrincipal();
	}

	private static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 返回当前登录用户
	 * 
	 * @return
	 */
	public static CustomUserDetails getLoginUser() {
		if (getPrincipal() != null && getPrincipal() instanceof CustomUserDetails) {
			return (CustomUserDetails) getPrincipal();
		}
		return null;
	}

	public static List<String> getRolesByCurrentUser() {
		return null;
	}

	public static List<String> getRolesByUser(String user) {
		return null;
	}

	public static List<String> getResByRole(String role) {
		return null;
	}

	public static List<String> getResByRoles(String[] roles) {
		return null;
	}
}
