package org.openkoala.exception.surpport.springmvc;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openkoala.exception.base.utils.WebErrUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * SpringMVC异常解析器
 * 
 * @author zyb
 * @since 2013-6-5 下午4:03:26
 */
public class ExceptionResolver extends SimpleMappingExceptionResolver {

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		String exceptionMsg = WebErrUtils.formatException(ex);
		String viewName = determineViewName(ex, request);
		if (viewName != null) {
			viewName = viewName.substring(0, viewName.indexOf(".jsp"));
			if (!isAsynRequest(request)) {
				Integer statusCode = determineStatusCode(request, viewName);
				if (statusCode != null) {
					applyStatusCodeIfPossible(request, response, statusCode);
				}
				ex.printStackTrace();
				Map<String, String> result = new HashMap<String, String>();
				result.put(WebErrUtils.ERROR_KEY, exceptionMsg);
				return new ModelAndView(viewName, result);
			} else {
				writeJSON(response, "actionError", exceptionMsg);
				ex.printStackTrace();
				return null;
			}
		}
		return null;
	}

	/**
	 * 向页面输出JSON数据
	 * @param response	response对象
	 * @param key		JSON中的键
	 * @param value		JSON中的值
	 */
	private void writeJSON(HttpServletResponse response, String key, String value) {
		Writer writer = null;
		try {
			response.setContentType("text/x-json;charset=UTF-8");
			writer = response.getWriter();
			writer.write(String.format("{\"%s\":\"%s\"}", key , value));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 是否是异步请求
	 * 
	 * @param request
	 * @return
	 */
	private boolean isAsynRequest(HttpServletRequest request) {
		return (request.getHeader("accept").indexOf("application/json") != -1 || (request
				.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With")
				.indexOf("XMLHttpRequest") != -1));
	}

}
