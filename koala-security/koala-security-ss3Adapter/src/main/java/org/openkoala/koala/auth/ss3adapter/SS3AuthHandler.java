package org.openkoala.koala.auth.ss3adapter;

import org.openkoala.koala.auth.AuthHandler;
import org.openkoala.koala.auth.UserDetails;
import org.openkoala.koala.auth.password.PasswordEncoder;
import org.openkoala.koala.auth.vo.DefaultUserDetailsImpl;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Spring Security3.0认证处理器实现
 * @author zhuyuanbiao
 * @date 2013年12月23日 下午4:50:06
 *
 */
public class SS3AuthHandler implements AuthHandler {
	
	private PasswordEncoder passwordEncoder;
	
	private UserDetailsService userDetailsService;
	
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	public UserDetails authenticate(String useraccount, String password) {
		CustomUserDetails loadedUser = (CustomUserDetails) userDetailsService.loadUserByUsername(useraccount);
		
		if (loadedUser == null) {
			throw new UsernameNotFoundException("Username not found.");
		}
		
		if (!isPasswordValid(password, loadedUser.getPassword())) {
			throw new BadCredentialsException("Password is not correct.");
		}
		
		DefaultUserDetailsImpl result = new DefaultUserDetailsImpl();
		result.setUseraccount(loadedUser.getUsername());
		result.setRealName(loadedUser.getRealName());
		result.setSuper(loadedUser.isSuper());
		for (GrantedAuthority authority : loadedUser.getAuthorities()) {
			result.addAuthority(authority.getAuthority());
		}
		return result;
	}
	
	private boolean isPasswordValid(String password, String encodedPassword) {
		return encodedPassword.equals(passwordEncoder.encode(password));
	}

}
