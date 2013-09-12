package org.openkoala.exception.support.struts2;

import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.openkoala.exception.base.utils.WebErrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.config.entities.ExceptionMappingConfig;
import com.opensymphony.xwork2.interceptor.ExceptionHolder;
import com.opensymphony.xwork2.interceptor.ExceptionMappingInterceptor;

/**
 * 
 * 类    名：NpsExceptionInterceptor.java<br />
 *   
 * 功能描述：业务异常拦截器	<br />
 *  
 * 创建日期：2012-11-22上午09:00:44  <br />   
 * 
 * 版本信息：v 1.0<br />
 * 
 * 版权信息：Copyright (c) 2011 Csair All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:jiangwei@openkoala.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明
 */
public class Struts2ExceptionInterceptor extends ExceptionMappingInterceptor {

	private static final long serialVersionUID = -3419510370644521904L;
	
	private static final Logger logger = LoggerFactory.getLogger(Struts2ExceptionInterceptor.class);

	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		 // 取得请求相关的ActionContext实例  
        ActionContext ctx = invocation.getInvocationContext();  
        String result = null;
        try {
            result = invocation.invoke(); 
            return result;
        } catch (Exception e) {
        	logger.error("Application exception:",e);
        	e.printStackTrace();
        	//
        	String formatException = WebErrUtils.formatException(e);
        	//判断是否ajax请求
        	String header = ServletActionContext.getRequest().getHeader("X-Requested-With");
            boolean isAjax = "XMLHttpRequest".equalsIgnoreCase(header);
            
            if(isAjax){//输出ajax错误提示
                return buildErrorMsg2Json(formatException);
            }else{//生成重定向错误页面提示
            	List<ExceptionMappingConfig> exceptionMappings = invocation.getProxy().getConfig().getExceptionMappings();
                String mappedResult = this.findResultFromExceptions(exceptionMappings, e);
                if (mappedResult != null) {
                    result = mappedResult;
                    publishException(invocation, new ExceptionHolder(e));
                } else {
                	result = WebErrUtils.RESULT_BUSS_EXCEPTION;
                }
				ctx.put(WebErrUtils.ERROR_KEY, formatException);
                return result;
            }
        }
	}
	
	
	 /**
     * <B>输出Json格式的异常信息到页面，适用于ajax请求</B><br />
     * create by:vakin 
     *             at:2013-1-12
     * @param String
     * @return
     */
    private String buildErrorMsg2Json(String errorMsg){
        String json = "{\"actionError\":\"{message}\"}";
        WebErrUtils.writeJSON(json.replace("{message}", errorMsg));
        return null;
    }

}
