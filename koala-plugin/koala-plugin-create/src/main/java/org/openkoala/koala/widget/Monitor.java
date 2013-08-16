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
package org.openkoala.koala.widget;

import java.io.Serializable;

import org.openkoala.koala.annotation.ObjectFunctionCreate;

/**
 * 功能描述：<br />
 *  
 * 创建日期：2013-7-13 下午3:30:15  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */

@ObjectFunctionCreate
public class Monitor implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 安装方式
	 * all：集成式安装（安装监控和监控管理两部分）
	 * client：分布式安装（仅安装监控，监控管理部分由远程服务端完成）
	 */
	private String installType = "all";

	public String getInstallType() {
		return installType;
	}

	public void setInstallType(String installType) {
		this.installType = installType;
	}
	
}
