package org.openkoala.koala.auth.ss3adapter;

import org.openkoala.koala.auth.AuthHandler;
import org.openkoala.koala.auth.AuthInfo;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 登录验证
 * 
 * @author zhuyuanbiao
 * @date 2013年12月23日 上午11:22:35
 * 
 */
public class LoginAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	private UserDetailsService userDetailsService;
	
	private AuthHandler authHandler;

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

		if (authentication.getCredentials() == null) {
			logger.debug("Authentication failed: no credentials provided");
			throw new BadCredentialsException(messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}

		AuthInfo loginInfo = getUserInfo(userDetails.getPassword(), authentication.getCredentials().toString());
		
		if (!authHandler.isAuth(loginInfo)) {
			logger.debug("Authentication failed.");
			throw new AuthenticationFailureException("Authentication failed.");
		}

	}

	private AuthInfo getUserInfo(String encodedPassword, String password) {
		AuthInfo loginInfo = new AuthInfo();
		loginInfo.setEncodedPassword(encodedPassword);
		loginInfo.setPassword(password);
		return loginInfo;
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		UserDetails loadedUser;

		try {
			loadedUser = userDetailsService.loadUserByUsername(username);
		} catch (DataAccessException repositoryProblem) {
			throw new AuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
		} catch (UsernameNotFoundException notFound) {
			throw notFound;
		}

		if (loadedUser == null) {
			throw new AuthenticationServiceException(
					"UserDetailsService returned null, which is an interface contract violation");
		}

		return loadedUser;
	}

	public void setAuthHandler(AuthHandler authHandler) {
		this.authHandler = authHandler;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

}
