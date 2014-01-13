package org.openkoala.koala.auth.ss3adapter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * 验证过滤器
 * @author zyb
 * @since 2013-04-17 10:37
 *
 */
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	
	private boolean postOnly = true;
	
	private static final String SPRING_SECURITY_FORM_USERNAME_KEY = "j_username";
	
	private static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "j_password";
	
	@SuppressWarnings("unchecked")
	private List<HttpHandler> httpHandlers = Collections.EMPTY_LIST;
	
	private AuthenticationFailureHandler failureHandler;
	
	public boolean isPostOnly() {
		return postOnly;
	}

	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	public void setHttpHandlers(List<HttpHandler> httpHandlers) {
		this.httpHandlers = httpHandlers;
	}
	
	public void setFailureHandler(AuthenticationFailureHandler failureHandler) {
		this.failureHandler = failureHandler;
	}

	protected AuthenticationFilter() {
		super("/j_spring_security_check");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}
		
		handle(request, response);
		
        UsernamePasswordAuthenticationToken authRequest = 
        		new UsernamePasswordAuthenticationToken(  
        		obtainUsername(request), 
        		obtainPassword(request));  
  
        // 允许子类设置详细属性  
        setDetails(request, authRequest);  
  
        return this.getAuthenticationManager().authenticate(authRequest);  
	}
	
	public void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();

        if (logger.isDebugEnabled()) {
            logger.debug("Authentication request failed: " + failed.toString());
            logger.debug("Updated SecurityContextHolder to contain null Authentication");
            logger.debug("Delegating to authentication failure handler " + failureHandler);
        }

        failureHandler.onAuthenticationFailure(request, response, failed);
    }
	
	protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

	private Object obtainPassword(HttpServletRequest request) {
		return request.getParameter(SPRING_SECURITY_FORM_PASSWORD_KEY);
	}

	private Object obtainUsername(HttpServletRequest request) {
		return request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY);
	}

	private void handle(HttpServletRequest request, HttpServletResponse response) {
		if (!httpHandlers.isEmpty()) {
			for (HttpHandler each : httpHandlers) {
				each.handle(request, response);
			}
		}
	}

}
