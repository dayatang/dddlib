package org.openkoala.koala.auth.ss3adapter.provider;

import java.util.Collections;

import javax.inject.Inject;

import org.jasig.cas.client.validation.Assertion;
import org.openkoala.auth.application.UserApplication;
import org.openkoala.auth.application.vo.UserVO;
import org.openkoala.koala.auth.ss3adapter.CustomUserDetails;
import org.openkoala.koala.auth.ss3adapter.EmailNotFoundExction;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * CAS认证提供者
 * @author zhuyuanbiao
 * @date 2014年1月3日 上午11:36:34
 *
 */
public class CasUserProvider extends CasAuthenticationProvider {
	
	private static final String EMAIL_ATTRIBUTE = "email";

	private UserDetailsService userDetailsService;
	
	@Inject
	private UserApplication userApplication;
	
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
	
	public void setUserApplication(UserApplication userApplication) {
		this.userApplication = userApplication;
	}

	/**
	 * 当CAS验证通过之后，从本地数据库查找此用户是否已经存在，如果不存在，则创建此用户
	 */
	@Override
	protected UserDetails loadUserByAssertion(Assertion assertion) {
		CasAssertionAuthenticationToken token = new CasAssertionAuthenticationToken(assertion, "");
		CustomUserDetails loadedUser = (CustomUserDetails) userDetailsService.loadUserByUsername(token.getName());
		if (retrieveEmailAttribute(assertion) == null) {
			throw new EmailNotFoundExction("Email not found from cas server response.");
		}
		if (loadedUser == null) {
			createUser(assertion);
			loadedUser = createUserDetails();
		}
		return loadedUser;
	}

	/**
	 * 从CAS服务器中获取邮箱属性
	 * @param assertion
	 * @return
	 */
	private Object retrieveEmailAttribute(Assertion assertion) {
		return assertion.getPrincipal().getAttributes().get(EMAIL_ATTRIBUTE);
	}

	@SuppressWarnings("unchecked")
	private CustomUserDetails createUserDetails() {
		CustomUserDetails result = new CustomUserDetails();
		result.setAccountNonExpired(true);
		result.setAccountNonLocked(true);
		result.setEnabled(true);
		result.setAuthorities(Collections.EMPTY_LIST);
		result.setCredentialsNonExpired(true);
		return result;
	}
	
	private void createUser(Assertion assertion) {
		UserVO userVO = new UserVO();
		userVO.setUserAccount(assertion.getPrincipal().getName());
		userVO.setName(assertion.getPrincipal().getName());
		userVO.setEmail(retrieveEmailAttribute(assertion).toString());
		userVO.setValid(true);
		userApplication.saveUser(userVO);
	}

}
