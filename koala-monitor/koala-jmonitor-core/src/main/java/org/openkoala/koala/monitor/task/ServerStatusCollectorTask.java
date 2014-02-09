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
package org.openkoala.koala.monitor.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.time.DateUtils;
import org.openkoala.koala.monitor.common.KoalaDateUtils;
import org.openkoala.koala.monitor.def.TaskDef;
import org.openkoala.koala.monitor.model.ServerStatusVo;
import org.openkoala.koala.monitor.model.ServerStatusVo.CpuInfoVo;
import org.openkoala.koala.monitor.model.ServerStatusVo.DiskInfoVo;
import org.openkoala.koala.monitor.support.ServerStatusCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * 功能描述：服务器<br />
 * 
 * 创建日期：2013-8-6 下午2:14:47 <br />
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作 者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者 修改日期 文件版本 修改说明
 */
public class ServerStatusCollectorTask extends BaseMonitorTask {

	private static final Logger LOG = LoggerFactory
			.getLogger(ServerStatusCollectorTask.class);

	public final static String TASK_KEY = "SERVERSTATUS-COLLECT";

	public final static String CPU_INFO = "CPU";
	public final static String DISK_INFO = "DISK";
	public final static String MEM_INFO = "MEMORY";
	public final static String NET_INFO = "NETWORK";

	// 当前执行检测次数（记录是当前小时第几次检测）
	private int currentCheckCount = 0;

	private List<String> collectTypes = new ArrayList<String>();

	// 每小时内存使用汇总情况
	private Map<Integer,Double> memUsageEveryHour = null;

	// 每小时CPU使用汇总情况（多块）
	private Map<String, Map<Integer,Double>> cpuUsageEveryHour = null;

	// 磁盘使用汇总情况（多块）<磁盘名,<总容量，已使用量，读速率，写速率>>
	private Map<String, List<Double>> diskUsageEveryHour = null;
	
	private String nodataTip;

	public ServerStatusCollectorTask(TaskDef taskDef) {
		this.taskPeriod = (long) taskDef.getPeriod() * 60L * 1000L;// 分 -->毫秒
		delay = 1 * 1000;
		//
		String options = taskDef.getProperties().get("collectTypes");
		if (options != null){
			String[] optArray = options.split(";");
			for (String opt : optArray) {
				collectTypes.add(opt);
			}
		}
		memUsageEveryHour = new TreeMap<Integer, Double>();
		cpuUsageEveryHour = new HashMap<String, Map<Integer,Double>>();
		diskUsageEveryHour = new TreeMap<String, List<Double>>();

	}

	@Override
	public void stop() {

	}

	@Override
	public void refresh() {

	}

	@Override
	protected boolean initConfigOK() {
		return true;// collectTypes.size()>0;
	}

	@Override
	protected void doTask() {

		int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		//下一个时间段检测开始
		if(!memUsageEveryHour.containsKey(currentHour)){
			currentCheckCount = 1;
		}
		
		if (LOG.isDebugEnabled())LOG.debug("开始执行收集任务，Hour：{}", currentHour);
		// 最多只保存当前时间到过去24小时内的记录(移除第一条)
		if (memUsageEveryHour.size()>=24) {
			Integer first = currentHour + 1;
			memUsageEveryHour.remove(first);
			//
			for (Map<Integer,Double> cpumap : cpuUsageEveryHour.values()) {
				cpumap.remove(first);
			}
		}


		ServerStatusVo status = ServerStatusCollector.getServerAllStatus();
		if (status == null){
			checkTimer.cancel();
			nodataTip = "not sigar plugin support!";
			return;
		}
		// 内存 转换为百分百值
		double avgVal = (double)100 * status.getUsedMem() / status.getTotalMem();
		memUsageEveryHour.put(currentHour, calcAvgValue(memUsageEveryHour.get(currentHour), avgVal));

		// CPU
		List<CpuInfoVo> cpuInfos = status.getCpuInfos();
		for (CpuInfoVo cpu : cpuInfos) {
			String cpuId = cpu.getModel();
			Map<Integer,Double> tmpMap;
			if (!cpuUsageEveryHour.containsKey(cpuId)) {
				tmpMap = new TreeMap<Integer, Double>();
				cpuUsageEveryHour.put(cpuId, tmpMap);
			}
			
			tmpMap = cpuUsageEveryHour.get(cpuId);
			tmpMap.put(currentHour, calcAvgValue(tmpMap.get(currentHour), cpu.getUsedPercent()));
		}

		// 磁盘
		List<DiskInfoVo> diskInfos = status.getDiskInfos();
		for (DiskInfoVo disk : diskInfos) {
			if (!diskUsageEveryHour.containsKey(disk.getDevName())) {
				ArrayList<Double> tmpList = new ArrayList<Double>();
				diskUsageEveryHour.put(disk.getDevName(), tmpList);
			}
			diskUsageEveryHour.get(disk.getDevName()).add(
					(double) disk.getTotalSize());// 总量
			diskUsageEveryHour.get(disk.getDevName()).add(
					(double) disk.getUsedSize());// 已使用
			diskUsageEveryHour.get(disk.getDevName()).add(
					disk.getDiskReadRate());// 读速率
			diskUsageEveryHour.get(disk.getDevName()).add(
					disk.getDiskWriteRate());// 写速率
		}

		// 检测次数加1
		currentCheckCount++;

	}
	
	private Double calcAvgValue(Double origVal,Double newVal){
		if(currentCheckCount>1){
			newVal = (origVal*(currentCheckCount - 1) + newVal)/currentCheckCount;
		}
		return new BigDecimal(newVal).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 获取服务器状态信息
	 * 
	 * @return
	 */
	public Map<String, Object> getServerStatusInfos() {
		String yesterday = KoalaDateUtils.format(DateUtils.addDays(new Date(), -1),"yyyy-MM-dd");
		String today = KoalaDateUtils.format(new Date(),"yyyy-MM-dd");
		
		int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		Map<String, Object> map = new HashMap<String, Object>();
		// MEM
		Map<String,Double> memDatas = formatEveryHourDadas(memUsageEveryHour, yesterday, today, currentHour);
		map.put(MEM_INFO, memDatas);

		// CPU
		Map<String, Map<String,Double>> cpuDatas = new HashMap<String, Map<String,Double>>();
		Set<String> keys = cpuUsageEveryHour.keySet();
		//按每一种型号
		for (String model : keys) {
			Map<String, Double> modelDatas = formatEveryHourDadas(cpuUsageEveryHour.get(model), yesterday, today, currentHour);
			cpuDatas.put(model, modelDatas);
		}
		map.put(CPU_INFO, cpuDatas);

		// DISK
		Map<String, Map<String, Double>> diskDatas = new TreeMap<String, Map<String, Double>>();
		keys = diskUsageEveryHour.keySet();
		Map<String, Double> singgleDiskData;
		List<Double> dataList;
		for (String key : keys) {
			singgleDiskData = new HashMap<String, Double>();
			dataList = diskUsageEveryHour.get(key);
			singgleDiskData.put("total", dataList.get(0));
			singgleDiskData.put("used", dataList.get(1));
			if(dataList.size()>2){
				singgleDiskData.put("readRate", dataList.get(2));
				singgleDiskData.put("writeRate", dataList.get(3));
			}
			diskDatas.put(key, singgleDiskData);
		}
		map.put(DISK_INFO, diskDatas);

		return map;
	}

	/**
	 * 格式化每小时数据
	 * @param origDatas  原始数据
	 * @param yesterday  昨天日期字符串
	 * @param today      今天日期字符串
	 * @param currentHour 当前小时
	 * @return
	 */
	private Map<String, Double> formatEveryHourDadas(Map<Integer, Double> origDatas,String yesterday,String today,int currentHour){
		Map<String, Double> formatDatas = new TreeMap<String, Double>();
		//今天凌晨前数据
		for ( Integer i = currentHour+1; i < 24; i++) {
			//无数据则填充0
			formatDatas.put(yesterday +" "+ i, origDatas.containsKey(i) ? origDatas.get(i):new Double(0));
		}
		//今天数据
		for ( Integer i = 0; i <=currentHour; i++) {
			//无数据则填充0
			formatDatas.put(today +" "+ i, origDatas.containsKey(i) ? origDatas.get(i):new Double(0));
		}
		return formatDatas;
	}

	/**
	 * 获取服务器信息JSON格式
	 * 
	 * @return
	 */
	public String getDatas() {
		if(nodataTip != null){
			return "{'error':'"+nodataTip+"'}";
		}
		if(memUsageEveryHour == null || memUsageEveryHour.isEmpty()){
			return "{'error':'nodata'}";
		}
		return JSON.toJSONString(getServerStatusInfos());
	}

}
