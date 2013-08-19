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
package org.openkoala.koala.monitor.component.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openkoala.koala.monitor.jwebap.TaskDef;
import org.openkoala.koala.monitor.model.ChartDataModel;
import org.openkoala.koala.monitor.model.ServerStatusVo;
import org.openkoala.koala.monitor.model.ServerStatusVo.CpuInfoVo;
import org.openkoala.koala.monitor.model.ServerStatusVo.DiskInfoVo;
import org.openkoala.koala.monitor.support.ServerStatusCollector;

import com.alibaba.fastjson.JSON;

/**
 * 功能描述：<br />
 *  
 * 创建日期：2013-8-6 下午2:14:47  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public class ServerStatusCollectorTask extends BaseMonitorTask {

	public final static String TASK_KEY = "SERVERSTATUS-COLLECT"; 
	
	public final static String CPU_INFO = "CPU";
	public final static String DISK_INFO = "DISK";
	public final static String MEM_INFO = "MEMORY";
	public final static String NET_INFO = "NETWORK";
	
	private static int checkCountEveryHour = 0;//每小时检测次数
	
	private static List<String> collectTypes = new ArrayList<String>();
	
	//每小时内存使用汇总情况
	private static List<Double> memUsageEveryHour = null;
	
	//每小时CPU使用汇总情况（多块）
	private static Map<String, List<Double>> cpuUsageEveryHour = null;
	
	//磁盘使用汇总情况（多块）<磁盘名,<总容量，已使用量，读速率，写速率>>
	private static Map<String, List<Double>> diskUsageEveryHour = null;
	
	public ServerStatusCollectorTask(TaskDef taskDef) {
		String options = taskDef.getProperties().get("collectTypes");
		this.taskPeriod = taskDef.getPeriod() * 1000 * 1000;//分 -->毫秒
		if(options == null)return;
		String[] optArray = options.split(";");
		for (String opt : optArray) {
			collectTypes.add(opt);
		}
		
		checkCountEveryHour = 60/taskDef.getPeriod();
	}



	@Override
	public void stop() {
		
	}

	@Override
	public void refresh() {
		
	}
	

	@Override
	protected boolean initConfigOK() {
		return collectTypes.size()>0;
	}

	@Override
	protected void doTask() {
		int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		//0点或者第一次运行 初始化
		if(currentHour == 0 || memUsageEveryHour == null){
			initDatas();
		}
		
		ServerStatusVo status = ServerStatusCollector.getServerAllStatus();
		if(status.isSigarInitError())throw new RuntimeException("You must put Sigar' lib in you classpath");
		//内存
		int index = currentHour;
		double newVal = (double)status.getUsedMem()/status.getTotalMem() + memUsageEveryHour.get(index);
		memUsageEveryHour.set(index, newVal);
		
		//CPU
		List<CpuInfoVo> cpuInfos = status.getCpuInfos();
		for (CpuInfoVo cpu : cpuInfos) {
			String cpuId =cpu.getModel() + "|"+cpu.getId();
			if(!cpuUsageEveryHour.containsKey(cpuId)){
				ArrayList<Double> tmpList = new ArrayList<Double>();
				for (Double i = 0d; i < 24; i++) {
					tmpList.add(i);
				}
				cpuUsageEveryHour.put(cpuId, tmpList);
			}
			
			newVal = cpu.getUsedOrigVal() + cpuUsageEveryHour.get(cpuId).get(index);
			cpuUsageEveryHour.get(cpuId).set(index, newVal);
		}
		
		//磁盘
		List<DiskInfoVo> diskInfos = status.getDiskInfos();
		for (DiskInfoVo disk : diskInfos) {
			if(!diskUsageEveryHour.containsKey(disk.getDevName())){
				ArrayList<Double> tmpList = new ArrayList<Double>();
				diskUsageEveryHour.put(disk.getDevName(), tmpList);
			}
			diskUsageEveryHour.get(disk.getDevName()).add((double) disk.getTotalSize());//总量
			diskUsageEveryHour.get(disk.getDevName()).add((double) disk.getUsedSize());//已使用
			diskUsageEveryHour.get(disk.getDevName()).add(disk.getDiskReadRate());//读速率
			diskUsageEveryHour.get(disk.getDevName()).add(disk.getDiskWriteRate());//写速率
		}
		
	}
	
	/**
	 * 获取服务器状态信息
	 * @return
	 */
	public static Map<String, List<ChartDataModel>> getServerStatusInfos(){
		int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		List<ChartDataModel> tmpList = null;
		ChartDataModel model = null;
		Map<String, List<ChartDataModel>> map = new HashMap<String, List<ChartDataModel>>();
		tmpList = new ArrayList<ChartDataModel>();
		model = new ChartDataModel("内存",new ArrayList<Double>(),0,0);
		//MEM
		for (int i = 0; i < currentHour; i++) {
			model.getTimelines().add(new BigDecimal(memUsageEveryHour.get(i)/checkCountEveryHour).setScale(1).doubleValue());
		}
		tmpList.add(model);
		map.put(MEM_INFO, tmpList);
		
		
		//CPU
		tmpList = new ArrayList<ChartDataModel>();
		Set<String> keys = cpuUsageEveryHour.keySet();
		for (String key : keys) {
			String name = key.split("\\|")[0];
			model = new ChartDataModel(name,new ArrayList<Double>(),0,0);
			for (int i = 0; i < currentHour; i++) {
				model.getTimelines().add(new BigDecimal(cpuUsageEveryHour.get(key).get(i)/checkCountEveryHour).setScale(1).doubleValue());
			}
			tmpList.add(model);
		}
		map.put(CPU_INFO, tmpList);
		
		//DISK
		tmpList = new ArrayList<ChartDataModel>();
		keys = diskUsageEveryHour.keySet();
		for (String key : keys) {
			String name = key;//+ "(Read:" + diskUsageEveryHour.get(key).get(2) + ",Write:" + diskUsageEveryHour.get(key).get(3) + ")";
			model = new ChartDataModel(name,null,diskUsageEveryHour.get(key).get(0),diskUsageEveryHour.get(key).get(1));
			tmpList.add(model);
		}
		map.put(DISK_INFO, tmpList);
		
		return map;
	}
	
	/**
	 * 获取服务器信息JSON格式
	 * @return
	 */
	public static String getServerStatusInfosAsJson(){
		return JSON.toJSONString(getServerStatusInfos());
	}
	
	private static void initDatas(){
		memUsageEveryHour = new ArrayList<Double>();
		for (Double i = 0d; i < 24; i++) {
			memUsageEveryHour.add(i);
		}
		
		cpuUsageEveryHour = new HashMap<String, List<Double>>();
		diskUsageEveryHour = new HashMap<String, List<Double>>();
	}
	
	public static void main(String[] args) {
		System.out.println(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));

        double xx = 23.45666+ 22.5;
        System.out.println((double)111111);
	}
}
