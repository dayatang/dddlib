package org.openkoala.koala.monitor.def;

import java.lang.reflect.Method;

/**
 * 
 * 功能描述：<br />
 *  
 * 创建日期：2013-6-26 上午9:19:00  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明
 */
public class MethodTrace extends StackTrace {

	private static final long serialVersionUID = 1L;

	private String packageName;
	private String method = null;

	private boolean successed;
	

	public MethodTrace(Trace stackTrace, Object proxy, Method method,
			Object[] args) {
		super(stackTrace);
		init(proxy, method, args);
		setStack(null);//子轨迹忽略堆栈
	}

	public MethodTrace(Object proxy, Method method, Object[] args,boolean traceStack) {
		init(proxy, method, args);
		if(!traceStack)setStack(null);
	}

	private void init(Object proxy, Method method, Object[] args) {
		packageName = proxy.getClass().getPackage().getName();
		setMethod(method2String(method));
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public boolean isSuccessed() {
		return successed;
	}

	public void setSuccessed(boolean successed) {
		this.successed = successed;
	}
	

	@Override
	public String getStackTracesDetails() {
		if(successed && traceException != null){
			String prefixPkg = null;
			try {
				prefixPkg = packageName.substring(0,packageName.indexOf(".", packageName.indexOf(".")+1));
			} catch (Exception e) {
				prefixPkg = packageName;
			}
			StringBuffer details = new StringBuffer();
			StackTraceElement[] traceElements = traceException.getStackTrace();
			for (StackTraceElement stackTrace : traceElements) {
				if(stackTrace.toString().contains("org.openkoala.koala.monitor")){
					continue;
				}
				if(stackTrace.toString().trim().startsWith(prefixPkg)){
					details.append(stackTrace.toString()).append("\n");
				}
			}
			return details.toString();
		}
		return super.getStackTracesDetails();
	}

	@SuppressWarnings("rawtypes")
	private static String method2String(Method m) {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(getTypeName(m.getDeclaringClass())).append(".");
			sb.append(m.getName()).append("(");
			Class[] params = m.getParameterTypes(); // avoid clone
			for (int j = 0; j < params.length; j++) {
				sb.append(getTypeName(params[j]));
				if (j < (params.length - 1)){
					sb.append(",");
				}
			}
			sb.append(")");
			return sb.toString().replaceAll("\\s+", "");
		} catch (Exception e) {
			return m.getName();
		}
	}

	@SuppressWarnings("rawtypes")
	private static String getTypeName(Class type) {
		if (type.isArray()) {
			try {
				Class cl = type;
				int dimensions = 0;
				while (cl.isArray()) {
					dimensions++;
					cl = cl.getComponentType();
				}
				StringBuffer sb = new StringBuffer();
				String name = cl.getName();
				name = name.substring(name.lastIndexOf('.') + 1);
				sb.append(name);
				for (int i = 0; i < dimensions; i++) {
					sb.append("[]");
				}
				return sb.toString();
			} catch (Exception e) { 
				return "unknow";
			}
		}
		String name = type.getName();
		return name.substring(name.lastIndexOf('.') + 1);
	}

}
