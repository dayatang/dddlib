
package org.jboss.brms.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 请求编码过滤器，过滤所有请求，将请求参数字符串转换为指定编码，避免乱码出现
 * 使用方法：
 * 
 * 在web.xml中配置此Filter过滤所有路径，使用时无需任何额外代码，直接取参数即可
 * 支持Post方法和Get方法得请求。
 *
 */
@SuppressWarnings("rawtypes")
public class RequestEncodingFilter implements Filter {

    protected FilterConfig config;
    protected String encoding;
    protected boolean ignore;
    protected boolean filterGetMethod;

    public RequestEncodingFilter() {
        config = null;
        encoding = null; // 编码，默认为UTF-8
        ignore = false; // 可选，默认为不忽略
        filterGetMethod = false; // 是否处理Get方法
    }

    public void destroy() {
        config = null;
        encoding = null;
    }
    public void init(FilterConfig filterConfig) throws ServletException {
        config = filterConfig;
		Enumeration names = config.getInitParameterNames();
        while(names.hasMoreElements()) {
            String name = names.nextElement().toString();
            if (name.trim().toLowerCase().equals("encoding")) {
                encoding = filterConfig.getInitParameter(name);
                if (encoding == null || encoding.trim().length() == 0) {
                    encoding = "utf-8";
                }
            }
            
            if (name.trim().toLowerCase().equals("ignore")) {
                String s = config.getInitParameter(name);
                if (s == null) s = "";
                s = s.trim().toLowerCase();
                ignore = (s.equals("true") || s.equals("yes") || s.equals("ignore"));
            }
            
            if (name.trim().toLowerCase().equals("filtergetmethod")) {
                String s = config.getInitParameter(name);
                if (s == null) s = "";
                s = s.trim().toLowerCase();
                filterGetMethod = (s.equals("true") || s.equals("yes"));
            }
        }
    }

    public void doFilter(ServletRequest req, ServletResponse resp,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;

        if (!ignore) {
            request.setCharacterEncoding(encoding);

            // 对于GET方法需要对QueryString重新编码
            if (filterGetMethod 
                    && "GET".equalsIgnoreCase(request.getMethod())
                    && (request.getQueryString() != null)) {
                req = new RequestEncodingWrapper(request, encoding);
            }
        }

        chain.doFilter(req, resp);
    }
}

