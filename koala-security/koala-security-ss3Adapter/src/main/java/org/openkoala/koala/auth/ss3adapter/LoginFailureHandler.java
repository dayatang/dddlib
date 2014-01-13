package org.openkoala.koala.auth.ss3adapter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * 登录失败处理
 * @author zhuyuanbiao
 * @date 2014年1月13日 上午9:43:47
 *
 */
public class LoginFailureHandler implements AuthenticationFailureHandler {
	
	private String userAccountNotFoundUrl;
	
	private String passwordNotCorrectUrl;
	
	private String validateCodeNotCorrectUrl;

	public String getUserAccountNotFoundUrl() {
		return userAccountNotFoundUrl;
	}

	public void setUserAccountNotFoundUrl(String userAccountNotFoundUrl) {
		this.userAccountNotFoundUrl = userAccountNotFoundUrl;
	}

	public String getPasswordNotCorrectUrl() {
		return passwordNotCorrectUrl;
	}

	public void setPasswordNotCorrectUrl(String passwordNotCorrectUrl) {
		this.passwordNotCorrectUrl = passwordNotCorrectUrl;
	}

	public String getValidateCodeNotCorrectUrl() {
		return validateCodeNotCorrectUrl;
	}

	public void setValidateCodeNotCorrectUrl(String validateCodeNotCorrectUrl) {
		this.validateCodeNotCorrectUrl = validateCodeNotCorrectUrl;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		if (exception instanceof UsernameNotFoundException) {
			request.getRequestDispatcher(userAccountNotFoundUrl).forward(request, response);
			return;
		}
		
		if (exception instanceof BadCredentialsException) {
			request.getRequestDispatcher(passwordNotCorrectUrl).forward(request, response);
			return;
		}
		
		if (exception instanceof BadValidateCodeException) {
			request.getRequestDispatcher(validateCodeNotCorrectUrl).forward(request, response);
		}
		
	}

}
