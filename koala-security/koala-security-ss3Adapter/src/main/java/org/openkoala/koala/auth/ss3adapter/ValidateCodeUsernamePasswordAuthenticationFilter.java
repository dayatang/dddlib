package org.openkoala.koala.auth.ss3adapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.octo.captcha.module.servlet.image.SimpleImageCaptchaServlet;

/**
 * 验证码与用户登录验证
 * @author zyb
 * @since 2013-04-17 10:37
 *
 */
public class ValidateCodeUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private boolean postOnly = true;
	private boolean allowEmptyValidateCode = false;
	private String sessionvalidateCodeField = DEFAULT_SESSION_VALIDATE_CODE_FIELD;
	private String validateCodeParameter = DEFAULT_VALIDATE_CODE_PARAMETER;
	public static final String SPRING_SECURITY_LAST_USERNAME_KEY = "SPRING_SECURITY_LAST_USERNAME";
	// session中保存的验证码
	public static final String DEFAULT_SESSION_VALIDATE_CODE_FIELD = "rand";
	// 输入的验证码
	public static final String DEFAULT_VALIDATE_CODE_PARAMETER = "code";
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}
		
		checkValidateCode(request);
  
        // 获取Username和Password  
        String username = obtainUsername(request);  
        String password = obtainPassword(request);  
  
        // UsernamePasswordAuthenticationToken实现Authentication校验  
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(  
                username, password);  
  
        // 允许子类设置详细属性  
        setDetails(request, authRequest);  
  
        // 运行UserDetailsService的loadUserByUsername 再次封装Authentication  
        return this.getAuthenticationManager().authenticate(authRequest);  
	}

	/**
	 * 比较session中的验证码和用户输入的验证码是否相等
	 * @param request
	 */
	protected void checkValidateCode(HttpServletRequest request) {
		
		 String userCaptchaResponse = request.getParameter("jcaptcha");  
	        boolean captchaPassed = SimpleImageCaptchaServlet.validateResponse(request,  
	                userCaptchaResponse); 
	    if (captchaPassed==false) {
			throw new AuthenticationServiceException("Wrong ValidateCode！");
		}
	}

	protected String obtainSessionValidateCode(HttpServletRequest request) {
		Object obj = request.getSession().getAttribute(sessionvalidateCodeField);
		return null == obj ? "" : obj.toString();
	}

	public boolean isPostOnly() {
		return postOnly;
	}

	@Override
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	public String getValidateCodeName() {
		return sessionvalidateCodeField;
	}

	public void setValidateCodeName(String validateCodeName) {
		this.sessionvalidateCodeField = validateCodeName;
	}

	public boolean isAllowEmptyValidateCode() {
		return allowEmptyValidateCode;
	}

	public void setAllowEmptyValidateCode(boolean allowEmptyValidateCode) {
		this.allowEmptyValidateCode = allowEmptyValidateCode;
	}

}
