package org.openkoala.koala.monitor.component.tracer.http;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openkoala.koala.monitor.core.RuntimeContext;
import org.openkoala.koala.monitor.core.TraceContainer;
import org.openkoala.koala.monitor.core.TraceLiftcycleManager;
import org.openkoala.koala.monitor.def.ComponentDef;
import org.openkoala.koala.monitor.def.HttpRequestTrace;

/**
 * 监控组件过滤器
 * @author 俞立德
 *
 * 2007-4-5
 */
public class DetectFilter implements Filter {

	private static Log log = LogFactory.getLog(DetectFilter.class);
	
	private static final List<String> DEFAULT_IGNORE_SUFFIX = Arrays.asList("css;js;jpg;ico;jpeg;bmp;gif;png;css;swf".split(";"));
	   
	private FilterConfig _filterConfig;
	
	private static Map<String, Object> contextParams = new HashMap<String, Object>();
	
	private static TraceLiftcycleManager getContainer(){
		return RuntimeContext.getContext().getContainer();
	}
	
	/**
	 * 初始化
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		_filterConfig = filterConfig;

	}

	public String getInitParameter(String name) {
		return _filterConfig.getInitParameter(name);
	}

	/**
	 * 判断是否忽略该条请求轨迹监控
	 * @param request
	 * @param response
	 * @param filterChain
	 * @return
	 */
	public boolean ignoreThisTrace(ServletRequest request, ServletResponse response, FilterChain filterChain) {
		try {
			
			if(!RuntimeContext.componentIsActive(HttpComponent.TRACE_TYPE))return true;
			
			HttpServletRequest httprequest = (HttpServletRequest) request;
			String path = httprequest.getRequestURI().trim();
			
			if(path.indexOf(".")>0){
				String suffix = path.substring(path.lastIndexOf(".")+1).toLowerCase();
				if(DEFAULT_IGNORE_SUFFIX.contains(suffix))return true;
			}

			ComponentDef def = RuntimeContext.getContext().getComponentDef(HttpComponent.TRACE_TYPE);
			String[] includeUrls = StringUtils.trimToEmpty(def.getProperty("includeUrls")).split(";");
			//如果包含网址不为空，则只处理包含在内的网址
			if(includeUrls.length>0 && StringUtils.isNotBlank(includeUrls[0])){
				for (String url : includeUrls) {
					if(url.indexOf("*")>=0){
						if(path.matches(url.replaceAll("\\*", ".*"))){
							return false;
						}
					}else{
						if(path.indexOf(url)>=0){
							return false;
						}
					}
				}
				return true;
			}
			//排除网址
			String excludes[] = StringUtils.trimToEmpty(def.getProperty("excludeUrls")).split(";");
			for (String excludeUrl : excludes) {
				if(excludeUrl.indexOf("*")>=0){
					if(path.matches(excludeUrl.replaceAll("\\*", ".*"))){
						return true;
					}
				}else{
					if(path.indexOf(excludeUrl)>=0){
						return true;
					}
				}
			}
		} catch (Throwable t) {
		}
		return false;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		if (getContainer() == null || ignoreThisTrace(request, response, filterChain)) {
			filterChain.doFilter(request, response);
			return;
		}
		HttpRequestTrace pageTrace = new HttpRequestTrace(req);
		//登录用户
		pageTrace.setPrincipal(getCurrentUser(req));
		try {
			try {
				getContainer().activateTrace(HttpComponent.TRACE_TYPE,pageTrace);
				
			} catch (Throwable e) {
				log.warn(e.getMessage());
			}
			filterChain.doFilter(request, response);
		} finally {
			try {
				getContainer().inactivateTrace(HttpComponent.TRACE_TYPE,pageTrace);
				TraceContainer.clearThreadKey();
				
			} catch (Throwable e) {
				log.warn(e.getMessage());
			}
		}
		return;
	}

	public void destroy() {
	}

	/**
	 * 获取当前登录用户名
	 * @param req
	 * @return
	 */
    private static String getCurrentUser(HttpServletRequest req){
    	String user = "unknow";
    	try {
    		String sessionKey = RuntimeContext.getContext().getComponentDef(HttpComponent.TRACE_TYPE).getProperty("login-user-sessionkey");
        	if(sessionKey.startsWith("SPRING_SECURITY_CONTEXT")){//Spring Security 
        		Object ssContext = req.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        		if(ssContext != null){
        			Method getAuthentitionMethod = null;
        			if(contextParams.containsKey("getAuthentitionMethod")){
        				getAuthentitionMethod = (Method) contextParams.get("getAuthentitionMethod");
        			}else{
        				getAuthentitionMethod = ssContext.getClass().getDeclaredMethod("getAuthentication");
        				contextParams.put("getAuthentitionMethod", getAuthentitionMethod);
        			}
        			
					Object authentication = getAuthentitionMethod.invoke(ssContext);
        			if(authentication != null){
        				Method getNameMethod = null;
        				if(contextParams.containsKey("getNameMethod")){
        					getNameMethod = (Method) contextParams.get("getNameMethod");
            			}else{
            				getNameMethod = authentication.getClass().getMethod("getName");
            				contextParams.put("getNameMethod", getNameMethod);
            			}
						Object name = getNameMethod.invoke(authentication);
        				if(name != null)user = name.toString();
        			}
        		}
        	}else{
        		user = req.getSession().getAttribute(sessionKey).toString();
        	}
		} catch (Exception e) {}
    	
    	return user;
    }

}
