package org.openkoala.koala.auth.ss3adapter;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class AuthUserUtil {
	
	private AuthUserUtil() {
		
	}

	/**
	 * 获取当前登录用户名
	 * 
	 * @return
	 */
	public static String getLoginUserName() {
		if (getAuthentication() != null && getPrincipal() instanceof CustomUserDetails) {
			return ((CustomUserDetails) getPrincipal()).getUsername();
		}
		return null;
	}

	/**
	 * 获取登录用户信息
	 * @return
	 */
	private static Object getPrincipal() {
		return getAuthentication().getPrincipal();
	}

	/**
	 * 获取用户授权信息
	 * @return
	 */
	private static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 返回当前登录用户
	 * 
	 * @return
	 */
	public static CustomUserDetails getLoginUser() {
		if (getPrincipal() instanceof CustomUserDetails) {
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
