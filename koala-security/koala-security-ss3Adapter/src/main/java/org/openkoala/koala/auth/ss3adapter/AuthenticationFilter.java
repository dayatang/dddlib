package org.openkoala.koala.auth.ss3adapter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 验证过滤器
 * @author zyb
 * @since 2013-04-17 10:37
 *
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private boolean postOnly = true;
	
	private List<HttpHandler> httpHandlers;

	public void setHttpHandlers(List<HttpHandler> httpHandlers) {
		this.httpHandlers = httpHandlers;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}
		
		handle(request, response);
		
        // UsernamePasswordAuthenticationToken实现Authentication校验  
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(  
        		obtainUsername(request), obtainPassword(request));  
  
        // 允许子类设置详细属性  
        setDetails(request, authRequest);  
  
        // 运行UserDetailsService的loadUserByUsername 再次封装Authentication  
        return this.getAuthenticationManager().authenticate(authRequest);  
	}

	private void handle(HttpServletRequest request, HttpServletResponse response) {
		for (HttpHandler each : httpHandlers) {
			each.handle(request, response);
		}
	}

	public boolean isPostOnly() {
		return postOnly;
	}

	@Override
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

}
