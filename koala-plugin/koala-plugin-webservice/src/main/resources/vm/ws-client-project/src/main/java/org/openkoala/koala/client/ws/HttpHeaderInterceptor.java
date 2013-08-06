package org.openkoala.koala.client.ws;

import java.util.List;
import java.util.Map;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.AbstractOutDatabindingInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

/**
 * WebService客户端拦截器（用于添加HTTP头部信息）
 * 
 * @author zyb
 * @since 2013-5-27 下午2:24:08
 * 
 */
public class HttpHeaderInterceptor extends AbstractOutDatabindingInterceptor {
	
	private List<String> username;
	
	private List<String> password;
	
	public void setUsername(List<String> username) {
		this.username = username;
	}

	public void setPassword(List<String> password) {
		this.password = password;
	}

	public HttpHeaderInterceptor() {
		super(Phase.PRE_PROTOCOL);
	}

	@SuppressWarnings("unchecked")
	public void handleMessage(Message message) throws Fault {
		Map<String, List<String>> headers = (Map<String, List<String>>) message.get(Message.PROTOCOL_HEADERS);
		headers.put("username", username);
		headers.put("password", password);		
	}

}
