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
package org.openkoala.koala.monitor.remote;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.openkoala.koala.monitor.jwebap.NetTransObject;

/**
 * 功能描述：同步数据命令<br />
 *  
 * 创建日期：2013-5-21 下午5:11:40  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public class Commond implements Serializable{

	private static final long serialVersionUID = 6748450171484878110L;
	
	private String commondText;
	
	private Map<String, String> headers;
	
	//数据传输形式：Json & MAP
	private String dataJson;//
	private Map<String, List<NetTransObject>> datas;
	
	/**
	 * @param commondText
	 */
	public Commond(String commondText) {
		super();
		this.commondText = commondText;
		addHeader(CommondConst.CMD_ID, UUID.randomUUID().toString().replaceAll("\\-", ""));
	}
	
	public String getDataJson() {
		return dataJson;
	}

	public void setDataJson(String dataJson) {
		this.dataJson = dataJson;
	}



	public String getCommondText() {
		return commondText;
	}

	public void setCommondText(String commondText) {
		this.commondText = commondText;
	}

	public Map<String, List<NetTransObject>> getDatas() {
		if(datas == null)datas = new HashMap<String, List<NetTransObject>>();
		return datas;
	}

	public void setDatas(Map<String, List<NetTransObject>> datas) {
		this.datas = datas;
	}
	
	
	public Map<String, String> getHeaders() {
		if(headers == null)headers = new HashMap<String, String>();
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	public String getCommondID(){
		return getHeaders().get(CommondConst.CMD_ID);
	}
	
	public String getError(){
		return getHeaders().get(CommondConst.ERROR);
	}
	
	public boolean isError(){
		return StringUtils.isNotBlank(getError());
	}

	public Commond clearData(){
		getDatas().clear();
		return this;
	}
	
	
	public Commond addData(String key,NetTransObject data){
		List<NetTransObject> list = getDatas().get(key);
		if(list == null){
			list = new ArrayList<NetTransObject>();
			getDatas().put(key, list);
		}
		list.add(data);
		return this;
	}
	
	public Commond addHeader(String headerName,String headerValue){
		getHeaders().put(headerName, headerValue);
		return this;
	}
	
	/**
	 * 获取唯一的数据对象（主要针对数据仅为一个对象的情况）
	 * @return
	 */
	public NetTransObject getSingleData(){
		Iterator<List<NetTransObject>> iterator = getDatas().values().iterator();
		if(iterator.hasNext())return iterator.next().get(0);
		return null;
	}
	
}
