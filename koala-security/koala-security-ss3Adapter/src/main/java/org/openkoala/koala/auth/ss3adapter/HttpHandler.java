package org.openkoala.koala.auth.ss3adapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HTTP处理器
 * @author zhuyuanbiao
 * @date 2014年1月7日 上午11:09:02
 *
 */
public interface HttpHandler {

	/**
	 * 处理HTTP请求
	 * @param request
	 * @param response
	 */
	void handle(HttpServletRequest request, HttpServletResponse response);
	
}
