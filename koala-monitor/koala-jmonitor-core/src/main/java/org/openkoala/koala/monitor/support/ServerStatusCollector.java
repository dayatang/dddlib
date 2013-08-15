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
package org.openkoala.koala.monitor.support;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.time.DateFormatUtils;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.Swap;
import org.jwebap.core.RuntimeContext;
import org.openkoala.koala.monitor.model.ServerStatusVo;
import org.openkoala.koala.monitor.model.ServerStatusVo.CpuInfoVo;
import org.openkoala.koala.monitor.model.ServerStatusVo.DiskInfoVo;

/**
 * 
 * 功能描述：服务器状态采集工具<br />
 *  
 * 创建日期：2013-6-19 上午9:17:40  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明
 */
public class ServerStatusCollector {
	
	//磁盘读写初始数据 用于计算读写速率
	private static Map<String, String> diskWritesAndReadsOnInit = new HashMap<String, String>();
	private static long initTime;
	static{
		initTime = System.currentTimeMillis();
		resetClasspath();
		Sigar sigar = null;
		try {
			
			sigar = new Sigar();
			FileSystem fslist[] = sigar.getFileSystemList();
			FileSystemUsage usage = null;
			for (int i = 0; i < fslist.length; i++) {
				FileSystem fs = fslist[i];
				if(fs.getType() != 2)continue;
				usage = sigar.getFileSystemUsage(fs.getDirName());
				diskWritesAndReadsOnInit.put(fs.getDevName(), usage.getDiskReadBytes() + "|"+usage.getDiskWriteBytes());
			}
		} catch (Exception e) {}finally{
			if(sigar != null)sigar.close();
		}
	}
	
	/**，
	 * 重新设置CLASSPATH,加入sigar，以支持dll,so等文件的加入与读取
	 */
	private static void resetClasspath(){
		String libPath = System.getProperty("java.library.path");
		String classpath = ServerStatusCollector.class.getResource("/").getPath();
		System.setProperty("java.library.path",classpath+File.separator+"sigar"+File.pathSeparator+libPath);
	}
	/**
	 * 获取服务器状态信息
	 * @return
	 */
	public static ServerStatusVo getServerAllStatus(){
		ServerStatusVo status = new ServerStatusVo();

    	Sigar sigar = null;
		try {
			
			getServerBaseInfo(status);
			
            sigar = new Sigar();
            getServerCpuInfo(sigar, status);
            getServerDiskInfo(sigar, status);
            getServerMemoryInfo(sigar, status);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(sigar == null)status.setSigarInitError(true);
			if(sigar != null)sigar.close();
		}

		return status;
	}
	
	/**
	 * 获取服务器基本信息
	 * @return
	 */
	public static void getServerBaseInfo(ServerStatusVo status){
		status.setServerTime(DateFormatUtils.format(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss"));
		status.setServerName(System.getenv().get("COMPUTERNAME"));
		status.setJavaServer(RuntimeContext.getContext().getServerName());
		status.setDeployPath(RuntimeContext.getContext().getDeployPath());
		
		Runtime rt = Runtime.getRuntime();
		status.setJvmTotalMem(rt.totalMemory()/(1024*1024));
		status.setJvmFreeMem(rt.freeMemory()/(1024*1024));
		
		Properties props = System.getProperties();
		status.setServerOs(props.getProperty("os.name") + " " + props.getProperty("os.arch") + " " + props.getProperty("os.version"));
		status.setJavaHome(props.getProperty("java.home"));
		status.setJavaVersion(props.getProperty("java.version"));
		status.setJavaTmpPath(props.getProperty("java.io.tmpdir"));

	}
	
	public static void getServerCpuInfo(Sigar sigar,ServerStatusVo status){
		try {
			CpuInfo infos[] = sigar.getCpuInfoList();
			CpuPerc cpuList[] = sigar.getCpuPercList();
			double totalUse = 0L;
	        for (int i = 0; i < infos.length; i++) {
	        	CpuPerc perc = cpuList[i];
				CpuInfoVo cpuInfo = new CpuInfoVo();
				cpuInfo.setId(infos[i].hashCode() + "");
				cpuInfo.setCacheSize(infos[i].getCacheSize());
				cpuInfo.setModel(infos[i].getModel());
				cpuInfo.setUsed(CpuPerc.format(perc.getCombined()));
				cpuInfo.setUsedOrigVal(perc.getCombined());
				cpuInfo.setIdle(CpuPerc.format(perc.getIdle()));
				cpuInfo.setTotalMHz(infos[i].getMhz());
				cpuInfo.setVendor(infos[i].getVendor());
				status.getCpuInfos().add(cpuInfo);
				totalUse += perc.getCombined();
			}
	        status.setCpuUsage(CpuPerc.format(totalUse/status.getCpuInfos().size()));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
    public static void getServerMemoryInfo(Sigar sigar,ServerStatusVo status){
		try {
			Mem mem = sigar.getMem();
			status.setTotalMem(mem.getTotal()/(1024*1024));
			status.setUsedMem(mem.getUsed()/(1024*1024));
			status.setFreeMem(mem.getFree()/(1024*1024));
			//交换区
			Swap swap = sigar.getSwap();
			status.setTotalSwap(swap.getTotal()/(1024*1024));
			status.setUsedSwap(swap.getUsed()/(1024*1024));
			status.setFreeSwap(swap.getFree()/(1024*1024));
		} catch (Exception e) {

		}
	}
    
    
    public static void getServerDiskInfo(Sigar sigar,ServerStatusVo status){
		try {
			FileSystem fslist[] = sigar.getFileSystemList();
			FileSystemUsage usage = null;
			for (int i = 0; i < fslist.length; i++) {
				FileSystem fs = fslist[i];
				switch (fs.getType()) {
				case 0: // TYPE_UNKNOWN ：未知
				case 1: // TYPE_NONE
				case 3:// TYPE_NETWORK ：网络
				case 4:// TYPE_RAM_DISK ：闪存
				case 5:// TYPE_CDROM ：光驱
				case 6:// TYPE_SWAP ：页面交换
					break;
				case 2: // TYPE_LOCAL_DISK : 本地硬盘
					DiskInfoVo disk = new DiskInfoVo();
					disk.setDevName(fs.getDevName());
					disk.setDirName(fs.getDirName());
					usage = sigar.getFileSystemUsage(fs.getDirName());
					disk.setTotalSize(usage.getTotal()/(1024*1024));
					//disk.setFreeSize(usage.getFree()/(1024*1024));
					disk.setAvailSize(usage.getAvail()/(1024*1024));
					disk.setUsedSize(usage.getUsed()/(1024*1024));
					disk.setUsePercent(usage.getUsePercent() * 100D + "%");
					disk.setTypeName(fs.getTypeName());
					disk.setSysTypeName(fs.getSysTypeName());
					
					String val = diskWritesAndReadsOnInit.get(fs.getDevName());
					if(val != null){
						long timePeriod = (System.currentTimeMillis() - initTime)/1000;
						long origRead = Long.parseLong(val.split("\\|")[0]);
						long origWrite = Long.parseLong(val.split("\\|")[1]);
						disk.setDiskReadRate((usage.getDiskReadBytes() - origRead)/timePeriod);
						disk.setDiskWriteRate((usage.getDiskWriteBytes() - origWrite)/timePeriod);
					}
					
					status.getDiskInfos().add(disk);
					
				}
			}
		} catch (Exception e) {

		}
	}
    
    public static void getNetInfo(Sigar sigar,ServerStatusVo status){
    	try {
    		 String ifNames[] = sigar.getNetInterfaceList();
    	        for (int i = 0; i < ifNames.length; i++) {
    	            String name = ifNames[i];
    	            NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
    	            if ((ifconfig.getFlags() & 1L) <= 0L) {
    	                System.out.println("!IFF_UP...skipping getNetInterfaceStat");
    	                continue;
    	            }
    	            NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
    	            System.out.println(name + "接收的总包裹数:" + ifstat.getRxPackets());// 接收的总包裹数
    	            System.out.println(name + "发送的总包裹数:" + ifstat.getTxPackets());// 发送的总包裹数
    	            System.out.println(name + "接收到的总字节数:" + ifstat.getRxBytes());// 接收到的总字节数
    	            System.out.println(name + "发送的总字节数:" + ifstat.getTxBytes());// 发送的总字节数
    	            System.out.println(name + "接收到的错误包数:" + ifstat.getRxErrors());// 接收到的错误包数
    	            System.out.println(name + "发送数据包时的错误数:" + ifstat.getTxErrors());// 发送数据包时的错误数
    	            System.out.println(name + "接收时丢弃的包数:" + ifstat.getRxDropped());// 接收时丢弃的包数
    	            System.out.println(name + "发送时丢弃的包数:" + ifstat.getTxDropped());// 发送时丢弃的包数
    	        }
		} catch (Exception e) {
			
		}
    }
    
    public static void main(String[] args) {
    	resetClasspath();
			ServerStatusVo status = new ServerStatusVo();
	    	Sigar sigar = null;
			try {
	            sigar = new Sigar();
	            getServerCpuInfo(sigar, status);
	            
	            System.out.println(status);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
