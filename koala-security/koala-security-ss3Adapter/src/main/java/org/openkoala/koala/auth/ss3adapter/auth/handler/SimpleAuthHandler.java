package org.openkoala.koala.auth.ss3adapter.auth.handler;

import org.openkoala.koala.auth.AuthHandler;
import org.openkoala.koala.auth.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 简单验证处理器
 * @author zhuyuanbiao
 * @date 2014年1月22日 下午7:28:10
 *
 */
public class SimpleAuthHandler implements AuthHandler {

	@Override
	public UserDetails authenticate(String useraccount, String password) {
		// 此处实现真正的验证逻辑
		if (!useraccount.equals(password)) {
			throw new UsernameNotFoundException("Username not found.");
		}
		return null;
	}

}
