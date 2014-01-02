package org.openkoala.koala.auth.ss3adapter;

import java.util.ArrayList;
import java.util.Collection;

import org.openkoala.koala.auth.AuthHandler;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 登录验证
 * 
 * @author zhuyuanbiao
 * @date 2013年12月23日 上午11:22:35
 * 
 */
public class LoginAuthenticationProvider implements AuthenticationProvider {

	private AuthHandler authHandler;

	public void setAuthHandler(AuthHandler authHandler) {
		this.authHandler = authHandler;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		org.openkoala.koala.auth.UserDetails loadedUser = authHandler.authenticate(
				authentication.getPrincipal().toString(), 
				authentication.getCredentials().toString());
		
		if (loadedUser == null) {
			throw new AuthenticationFailureException("Authentication failed.");
		}
		
		return createSuccessAuthentication(authentication, getUserDetails(loadedUser), loadedUser);
	}

	private CustomUserDetails getUserDetails(org.openkoala.koala.auth.UserDetails loadedUser) {
		CustomUserDetails userDetails = new CustomUserDetails();
		userDetails.setRealName(loadedUser.getRealName());
		userDetails.setAuthorities(getAuthorities(loadedUser));
		userDetails.setUsername(loadedUser.getUseraccount());
		userDetails.setPassword(loadedUser.getPassword());
		userDetails.setEnabled(loadedUser.isEnabled());
		userDetails.setSuper(loadedUser.isSuper());
		return userDetails;
	}
	
	private Authentication createSuccessAuthentication(Authentication authentication, 
			UserDetails userDetails, org.openkoala.koala.auth.UserDetails loadedUser) {
		UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userDetails,
				authentication.getCredentials(), getAuthorities(loadedUser));
		result.setDetails(authentication.getDetails());
		return result;
	}

	private Collection<GrantedAuthority> getAuthorities(org.openkoala.koala.auth.UserDetails loadedUser) {
		Collection<GrantedAuthority> results = new ArrayList<GrantedAuthority>();
		for (final String authority : loadedUser.getAuthorities()) {
			results.add(new GrantedAuthority() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 4687464060621929687L;

				@Override
				public String getAuthority() {
					return authority;
				}
			});
		}
		return results;
	}

	@Override
	public boolean supports(Class<? extends Object> authentication) {
		return true;
	}

}
