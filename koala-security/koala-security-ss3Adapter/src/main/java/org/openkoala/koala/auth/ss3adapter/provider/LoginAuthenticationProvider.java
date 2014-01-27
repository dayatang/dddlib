package org.openkoala.koala.auth.ss3adapter.provider;

import java.text.MessageFormat;

import javax.inject.Inject;

import org.openkoala.auth.application.UserApplication;
import org.openkoala.auth.application.vo.UserVO;
import org.openkoala.exception.extend.ApplicationException;
import org.openkoala.koala.auth.AuthHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 登录验证
 * 
 * @author zhuyuanbiao
 * @date 2013年12月23日 上午11:22:35
 * 
 */
public class LoginAuthenticationProvider implements AuthenticationProvider {
	
	private static Logger logger = LoggerFactory.getLogger(LoginAuthenticationProvider.class);

	private AuthHandler authHandler;
	
	@Inject
	private UserApplication userApplication;
	
	private UserDetailsService userDetailsService;
	
	private UserDetailsChecker checker = new DefaultPreAuthenticationChecks();
	
	public void setAuthHandler(AuthHandler authHandler) {
		this.authHandler = authHandler;
	}

	public void setUserApplication(UserApplication userApplication) {
		this.userApplication = userApplication;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		authHandler.authenticate(getUseraccount(authentication), getPassword(authentication));
		
		createUserIfNeed(authentication);
		
		checker.check(getUserDetails(authentication));
		
		modifyLastLoginTime(getUseraccount(authentication));
		
		return createSuccessAuthentication(authentication);
	}

	private void createUserIfNeed(Authentication authentication) {
		if (!isUseraccountExisted(authentication)) {
			UserVO userVO = new UserVO();
			userVO.setUserAccount(getUseraccount(authentication));
			userVO.setName(getUseraccount(authentication));
			userVO.setEmail(MessageFormat.format("{0}@null.com", getUseraccount(authentication)));
			try {
				userApplication.saveUser(userVO);
			} catch (ApplicationException e) {
				logger.error("保存用户失败：{}", e);
			}
		}
	}

	private boolean isUseraccountExisted(Authentication authentication) {
		return userApplication.findByUserAccount(getUseraccount(authentication)) != null;
	}

	private UserDetails getUserDetails(Authentication authentication) {
		return userDetailsService.loadUserByUsername(getUseraccount(authentication));
	}
	
	private void modifyLastLoginTime(String useraccount) {
		if (isUserExisted(useraccount)) {
			userApplication.modifyLastLoginTime(useraccount);
		}
	}

	private boolean isUserExisted(String useraccount) {
		UserVO result = userApplication.findByUserAccount(useraccount);
		if (result == null) {
			result = userApplication.findByEmail(useraccount);
		}
		return result == null ? false : true;
	}

	private String getUseraccount(Authentication authentication) {
		return authentication.getPrincipal().toString();
	}
	
	private String getPassword(Authentication authentication) {
		return authentication.getCredentials().toString();
	}

	private Authentication createSuccessAuthentication(Authentication authentication) {
		UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
				getUserDetails(authentication),
				authentication.getCredentials(), 
				getUserDetails(authentication).getAuthorities());
		
		result.setDetails(authentication.getDetails());
		return result;
	}

	@Override
	public boolean supports(Class<? extends Object> authentication) {
		return true;
	}
	
	 private class DefaultPreAuthenticationChecks implements UserDetailsChecker {
		 
	        public void check(UserDetails user) {
	            if (!user.isAccountNonLocked()) {
	                logger.debug("用户已经被锁住...");

	                throw new LockedException("用户已经被锁住...");
	            }

	            if (!user.isEnabled()) {
	                logger.debug("用户账号已经被禁用...");

	                throw new DisabledException("用户账号已经被禁用...");
	            }

	            if (!user.isAccountNonExpired()) {
	                logger.debug("用户账号已经过期...");

	                throw new AccountExpiredException("用户账号已经过期...");
	            }
	        }
	    }

}
