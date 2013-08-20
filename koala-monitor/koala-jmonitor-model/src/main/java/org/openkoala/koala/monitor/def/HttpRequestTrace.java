package org.openkoala.koala.monitor.def;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class HttpRequestTrace extends Trace   {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2694088563379875257L;

	private String ip = null;
	
	private String principal;//

	/** 请求URI */
	private String uri = null;
	
	private String sessionId;
	
	private String referer;//来路


	/** 请求的参数列表 */
	private String parameters;

	public HttpRequestTrace(Trace stackTrace, HttpServletRequest request) {
		super(stackTrace);
		init(request);
	}

	public HttpRequestTrace(HttpServletRequest request) {
		init(request);
	}

	@SuppressWarnings("rawtypes")
	private void init(HttpServletRequest request) {
		// uri
		setUri(request.getRequestURI());
		sessionId = request.getRequestedSessionId();
		setIp(request.getRemoteHost());
		this.referer = request.getHeader("Referer");
		String host = request.getHeader("Host");
		//本站来路不记录
		if(referer != null && referer.contains(host)){
			this.referer = null;
		}
		StringBuffer sb = new StringBuffer();
		// queryString
		if (request.getQueryString() != null) {
			sb.append(request.getQueryString()).append("&");
		}
		
		Map map = request.getParameterMap();
		Iterator it = map.keySet().iterator();
		while(it.hasNext()){
			Object key = it.next();
			//一些无意义的字段不记录
			if ("timestamp".equals(key))continue;
			sb.append(key.toString()).append("=");
			Object[] vals = (Object[]) map.get(key);
			for (int i = 0; i < vals.length; i++) {
				sb.append(vals[i].toString().length()>50 ? vals[i].toString().substring(0,48).concat("...") : vals[i].toString());
				if(i < vals.length - 1)sb.append(",");
			}
			sb.append("&");
		}
		if(sb.length()>0){
			sb.deleteCharAt(sb.length() - 1);
		}
		
		// ip
		// 参数
		setParameters(sb.toString());

	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getParameters() {
		return parameters;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}



	/**
	 * 
	 * 功能描述：活动用户<br />
	 *  
	 * 创建日期：2013-6-26 上午11:23:19  <br />   
	 * 
	 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
	 * 
	 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
	 * 
	 * 修改记录： <br />
	 * 修 改 者    修改日期     文件版本   修改说明
	 */
	public static class ActiveUser implements Serializable{

		private static final long serialVersionUID = -4324913707082449121L;
		private String ip;
		private String principal;
		private Date beginTime;//开始活动时间
		private Date activeTime;//最后活动时间
		private String referer;//来路
		private int count = 1;//请求次数
		
		
		
		/**
		 * 
		 */
		public ActiveUser() {}
		/**
		 * @param ip
		 * @param userName
		 * @param inTime
		 */
		public ActiveUser(String ip, String principal, Date inTime,String referer) {
			super();
			this.ip = ip;
			this.principal = principal;
			this.beginTime = inTime;
			this.activeTime = inTime;
			this.referer = referer;
		}
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
		
		public Date getBeginTime() {
			return beginTime;
		}
		public void setBeginTime(Date beginTime) {
			this.beginTime = beginTime;
		}
		public String getPrincipal() {
			return principal;
		}
		public void setPrincipal(String principal) {
			this.principal = principal;
		}
		public String getReferer() {
			return referer;
		}
		public void setReferer(String referer) {
			this.referer = referer;
		}
	
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
		
		
		public Date getActiveTime() {
			return activeTime;
		}
		public void setActiveTime(Date activeTime) {
			this.activeTime = activeTime;
		}
		public void addCount(){
			count++;
		}
		
		/**
		 * 判断是否正常请求<br>
		 * 规则：请求超过50次，每次请求间隔时间小5秒视为不正常
		 * @return
		 */
		public boolean isAbNormal(){
			if(referer == null || "".equals(referer.trim()))return false; 
			int period = (int) ((activeTime.getTime() - beginTime.getTime())/1000);
			return count>50 && count/period  <5;
		}
		
	}
}
