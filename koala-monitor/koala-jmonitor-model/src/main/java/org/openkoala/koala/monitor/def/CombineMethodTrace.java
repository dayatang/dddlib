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
package org.openkoala.koala.monitor.def;


/**
 * 功能描述：合并的方法trace（由于方法包含堆栈信息，故再合并一次）<br />
 *  
 * 创建日期：2013-7-11 上午9:12:30  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public class CombineMethodTrace extends Trace {

	private static final long serialVersionUID = 9075997420795698632L;
    
	private String method;

	private boolean successed;
	
	private String stackTracesDetails;
	
	public CombineMethodTrace(){}

	public void combineTraceInfo(MethodTrace origTrace) {
		this.ignore = origTrace.isIgnore();
		this.createdTime = origTrace.createdTime;
		this.inActiveTime = origTrace.inActiveTime;
		this.threadId = origTrace.threadId;
		this.traceId = origTrace.traceId;
		this.method = origTrace.getMethod();
		this.successed = origTrace.isSuccessed();
		this.stackTracesDetails = origTrace.getStackTracesDetails();
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

	public String getStackTracesDetails() {
		return stackTracesDetails;
	}

	public void setStackTracesDetails(String stackTracesDetails) {
		this.stackTracesDetails = stackTracesDetails;
	}
}
