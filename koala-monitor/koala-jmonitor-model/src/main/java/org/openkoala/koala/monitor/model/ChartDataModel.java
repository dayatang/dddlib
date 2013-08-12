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
package org.openkoala.koala.monitor.model;

import java.util.List;

import org.openkoala.koala.monitor.jwebap.NetTransObject;

/**
 * 功能描述:通用统计图数据模型<br />
 *  
 * 创建日期：2013-8-6 下午4:14:04  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public class ChartDataModel extends NetTransObject {

	private static final long serialVersionUID = 7371655060092334447L;
	
	private String showName;
	//时间轴值
	List<Double> timelines = null;
	private double total;//总量
	private double used;//已使用
	
	/**
	 * @param showName
	 * @param timelines
	 * @param total
	 * @param used
	 */
	public ChartDataModel(String showName, List<Double> timelines, double total,
			double used) {
		super();
		this.showName = showName;
		this.timelines = timelines;
		this.total = total;
		this.used = used;
	}
	public String getShowName() {
		return showName;
	}
	public void setShowName(String showName) {
		this.showName = showName;
	}
	public List<Double> getTimelines() {
		return timelines;
	}
	public void setTimelines(List<Double> timelines) {
		this.timelines = timelines;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public double getUsed() {
		return used;
	}
	public void setUsed(double used) {
		this.used = used;
	}
}
