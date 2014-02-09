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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 功能描述：数据处理策略<br />
 *  
 * 创建日期：2013-6-5 下午3:15:12  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明
 */
public class DataPolicyDef implements Serializable{

	private static final long serialVersionUID = -642419276624131531L;

	/*数据处理方式：local,remote*/
	private String type;
	
	private Map<String,String> properties=new HashMap<String,String>();
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public void setProperty(String key, String value){
		getProperties().put(key, value);
	}
}
