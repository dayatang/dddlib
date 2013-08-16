package org.openkoala.koala.web.jaxrs.handler;

import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.cxf.jaxrs.ext.RequestHandler;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.message.Message;
import org.openkoala.auth.application.domain.SecuritySettingsApplication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * JAX-RS权限处理器
 * @author zyb
 * @since 2013-6-27 下午12:32:36
 */
public class AuthorizationRequestHandler implements RequestHandler {
	
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

	@Override
	public Response handleRequest(Message message, ClassResourceInfo resourceClass) {
		if (!isCheckUsernameAndPassword()) {
			return authentication(defaultUser, defaultPassword);
		}
		
		if (isHeaderExist(message)) {
			return buildErrorResponse(Status.BAD_REQUEST, "The HTTP header is missing");
		}
		
		return authentication(getUsername(message).get(0), getPassword(message).get(0));
	
	}

	/**
	 * 权限验证
	 * @param username
	 * @param password
	 * @return
	 */
	private Response authentication(String username, String password) {
		try {
			Authentication authentication = authenticationManager.authenticate( //
					new UsernamePasswordAuthenticationToken(username, password));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (AuthenticationException e) {
			return buildErrorResponse(Status.UNAUTHORIZED, e.getMessage());
		}
		return null;
	}

	/**
	 * 构建Response
	 * @return
	 */
	private Response buildErrorResponse(Status status, String message) {
		return Response.status(status).entity(new KoalaJAXRSFault("", message))
				.type(MediaType.APPLICATION_XML).build();
	}

	/**
	 * HTTP头部信息是否存在
	 * @param message
	 * @return
	 */
	private boolean isHeaderExist(Message message) {
		return getUsername(message) == null || getPassword(message) == null;
	}

	/**
	 * 获取密码
	 * @param message
	 * @return
	 */
	private List<String> getPassword(Message message) {
		return getHttpHeaders(message).get("password");
	}

	/**
	 * 获取用户名
	 * @param message
	 * @return
	 */
	private List<String> getUsername(Message message) {
		return getHttpHeaders(message).get("username");
	}

	/**
	 * 获取HTTP头部信息
	 * @param message
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, List<String>> getHttpHeaders(Message message) {
		Map<String, List<String>> headers = (Map<String, List<String>>) message.get(Message.PROTOCOL_HEADERS);
		return headers;
	}

	/**
	 * 是否检查用户名和密码
	 * @return
	 */
	private boolean isCheckUsernameAndPassword() {
		return !(securitySettingsApplication.findAll().isEmpty() || 
			!securitySettingsApplication.findAll().get(0).isUsernamePasswordEnabled());
	}

}
