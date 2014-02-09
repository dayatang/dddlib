package org.openkoala.koala.web.cxf.interceptor;


import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.apache.cxf.interceptor.AbstractOutDatabindingInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.openkoala.auth.application.domain.SecuritySettingsApplication;
import org.openkoala.koala.web.exception.KoalaJAXRSException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * WebService服务器端权限认证拦截器
 * 
 * @author zyb
 * @since 2013-5-27 下午2:05:48
 */
public class AuthorizationInterceptor extends AbstractOutDatabindingInterceptor {

	private AuthenticationManager authenticationManager;
	
	private String defaultUser;
	
	private String defaultPassword;
	
	public void setDefaultUser(String defaultUser) {
		this.defaultUser = defaultUser;
	}

	public void setDefaultPassword(String defaultPassword) {
		this.defaultPassword = defaultPassword;
	}

	@Inject
	private SecuritySettingsApplication securitySettingsApplication;

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public AuthorizationInterceptor() {
		// 指定拦截器的执行阶段
		super(Phase.PRE_INVOKE);
	}

	@SuppressWarnings("unchecked")
	public void handleMessage(Message message) throws Fault {
		if (securitySettingsApplication.findAll().isEmpty() || !securitySettingsApplication.findAll().get(0).isUsernamePasswordEnabled()) {
			Authentication authentication = authenticationManager.authenticate( //
					new UsernamePasswordAuthenticationToken(defaultUser, defaultPassword));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			return;
		}
		
		Map<String, List<String>> headers = (Map<String, List<String>>) message.get(Message.PROTOCOL_HEADERS);
		
		if (headers.get("username") == null || headers.get("password") == null) {
			throw new KoalaJAXRSException(null, "The HTTP header is missing.");
		}
		
		String username = headers.get("username").get(0);
		String password = headers.get("password").get(0);

		Authentication authentication = authenticationManager.authenticate( //
				new UsernamePasswordAuthenticationToken(username, password));

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}