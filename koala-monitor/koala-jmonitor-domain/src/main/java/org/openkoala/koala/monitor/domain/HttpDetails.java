/*
 * Copyright (c) Koala 2012-2014 All Rights Reserved
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openkoala.koala.monitor.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 功能描述：<br />
 * 
 * 创建日期：2013-6-7 上午9:56:29 <br />
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作 者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者 修改日期 文件版本 修改说明
 */
@Entity
@Table(name = "K_M_HTTP_DETAILS")
public class HttpDetails extends BaseMonitorDetails {

	private static final long serialVersionUID = 4866520755447864208L;

	@Column(name = "IP")
	private String ip;

	@Column(name = "URI")
	private String uri;
	
	@Column(name = "REFERER")
	private String referer;

	@Column(name = "PRINCIPAL")
	private String principal;//

	@Lob
	@Column(name = "PARAMETERS")
	private String parameters;

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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	@Override
	public String toString() {
		return "HttpDetails [ip=" + ip + ", uri=" + uri + ", principal="
				+ principal + ", parameters=" + parameters + ", traceKey="
				+ traceKey + ", beginTime=" + beginTime  + "]";
	}

	

}
