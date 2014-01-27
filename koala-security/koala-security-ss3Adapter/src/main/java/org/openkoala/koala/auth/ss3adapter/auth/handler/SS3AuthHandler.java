package org.openkoala.koala.auth.ss3adapter.auth.handler;

import javax.inject.Inject;

import org.openkoala.auth.application.UserApplication;
import org.openkoala.auth.application.vo.UserVO;
import org.openkoala.koala.auth.AuthHandler;
import org.openkoala.koala.auth.UserDetails;
import org.openkoala.koala.auth.password.PasswordEncoder;
import org.openkoala.koala.auth.vo.DefaultUserDetailsImpl;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Spring Security3.0认证处理器实现
 * @author zhuyuanbiao
 * @date 2013年12月23日 下午4:50:06
 *
 */
public class SS3AuthHandler implements AuthHandler {
	
	private PasswordEncoder passwordEncoder;
	
	@Inject
	private UserApplication userApplication;
	
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public void setUserApplication(UserApplication userApplication) {
		this.userApplication = userApplication;
	}

	@Override
	public UserDetails authenticate(String useraccount, String password) {
		UserVO loadedUser = userApplication.findByUserAccount(useraccount);
		
		if (loadedUser == null) {
			throw new UsernameNotFoundException("Username not found.");
		}
		
		if (!isPasswordValid(password, loadedUser.getUserPassword())) {
			throw new BadCredentialsException("Password is not correct.");
		}
		
		DefaultUserDetailsImpl result = new DefaultUserDetailsImpl();
		result.setUseraccount(loadedUser.getUserAccount());
		result.setRealName(loadedUser.getName());
		result.setPassword(loadedUser.getUserPassword());
		result.setEmail(loadedUser.getEmail());
		
		return result;
	}
	
	private boolean isPasswordValid(String password, String encodedPassword) {
		return encodedPassword.equals(passwordEncoder.encode(password));
	}

}
