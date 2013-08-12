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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openkoala.koala.monitor.jwebap.NetTransObject;

/**
 * 功能描述：服务器状态<br />
 *  
 * 创建日期：2013-6-19 上午11:45:28  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public class ServerStatusVo extends NetTransObject{

	private static final long serialVersionUID = -2193532298090031743L;
	
	private String javaServer;
	private String deployPath;
	private String serverTime;
	private String serverName;
	private String serverOs;
	private String javaHome;
	private String javaTmpPath;
	private String javaVersion;
	private long jvmTotalMem;
	private long jvmFreeMem;
	
	private long totalMem;
	private long usedMem;
	private long freeMem;
	
	private long totalSwap;
	private long usedSwap;
	private long freeSwap;
	
	private String cpuUsage;//CPU使用率
	
	
	private List<CpuInfoVo> cpuInfos = new ArrayList<CpuInfoVo>();
	
	private List<DiskInfoVo> diskInfos = new ArrayList<DiskInfoVo>();
	
	private boolean sigarInitError;
	
	
	public String getJavaServer() {
		return javaServer;
	}

	public void setJavaServer(String javaServer) {
		this.javaServer = javaServer;
	}

	public String getDeployPath() {
		return deployPath;
	}

	public void setDeployPath(String deployPath) {
		this.deployPath = deployPath;
	}

	public String getServerTime() {
		return serverTime;
	}

	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerOs() {
		return serverOs;
	}

	public void setServerOs(String serverOs) {
		this.serverOs = serverOs;
	}

	public String getJavaHome() {
		return javaHome;
	}

	public void setJavaHome(String javaHome) {
		this.javaHome = javaHome;
	}

	public String getJavaVersion() {
		return javaVersion;
	}

	public void setJavaVersion(String javaVersion) {
		this.javaVersion = javaVersion;
	}

	public long getJvmTotalMem() {
		return jvmTotalMem;
	}

	public void setJvmTotalMem(long jvmTotalMem) {
		this.jvmTotalMem = jvmTotalMem;
	}

	public long getJvmFreeMem() {
		return jvmFreeMem;
	}

	public void setJvmFreeMem(long jvmFreeMem) {
		this.jvmFreeMem = jvmFreeMem;
	}


	public long getTotalMem() {
		return totalMem;
	}
	
	public String getJavaTmpPath() {
		return javaTmpPath;
	}

	public void setJavaTmpPath(String javaTmpPath) {
		this.javaTmpPath = javaTmpPath;
	}

	public void setTotalMem(long totalMem) {
		this.totalMem = totalMem;
	}

	public long getUsedMem() {
		return usedMem;
	}

	public void setUsedMem(long usedMem) {
		this.usedMem = usedMem;
	}

	public long getFreeMem() {
		return freeMem;
	}

	public void setFreeMem(long freeMem) {
		this.freeMem = freeMem;
	}

	public long getTotalSwap() {
		return totalSwap;
	}

	public void setTotalSwap(long totalSwap) {
		this.totalSwap = totalSwap;
	}

	public long getUsedSwap() {
		return usedSwap;
	}

	public void setUsedSwap(long usedSwap) {
		this.usedSwap = usedSwap;
	}

	public long getFreeSwap() {
		return freeSwap;
	}

	public void setFreeSwap(long freeSwap) {
		this.freeSwap = freeSwap;
	}

	public List<CpuInfoVo> getCpuInfos() {
		return cpuInfos;
	}

	public void setCpuInfos(List<CpuInfoVo> cpuInfos) {
		this.cpuInfos = cpuInfos;
	}

	public List<DiskInfoVo> getDiskInfos() {
		return diskInfos;
	}

	public void setDiskInfos(List<DiskInfoVo> diskInfos) {
		this.diskInfos = diskInfos;
	}

	public boolean isSigarInitError() {
		return sigarInitError;
	}

	public void setSigarInitError(boolean sigarInitError) {
		this.sigarInitError = sigarInitError;
	}
	

	public String getCpuUsage() {
		return cpuUsage;
	}

	public void setCpuUsage(String cpuUsage) {
		this.cpuUsage = cpuUsage;
	}
	
	/**
	 * 内存使用率
	 * @return
	 */
	public String getMemUsage(){
		return new BigDecimal(usedMem*100/totalMem).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue() + "%";
	}

	/**
	 * CPU信息
	 * 功能描述：<br />
	 *  
	 * 创建日期：2013-6-19 下午12:17:02  <br />   
	 * 
	 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
	 * 
	 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
	 * 
	 * 修改记录： <br />
	 * 修 改 者    修改日期     文件版本   修改说明
	 */
	public static class CpuInfoVo extends NetTransObject{
		private static final long serialVersionUID = 1L;
		
		private String id;
		private int totalMHz;
		private String vendor;
		private String model;
		private long cacheSize;
		private String idle;//空闲率
		private String used;//使用率（格式化的值）
		private double usedOrigVal;//使用率（原始的值）
		

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public int getTotalMHz() {
			return totalMHz;
		}

		public void setTotalMHz(int totalMHz) {
			this.totalMHz = totalMHz;
		}

		public String getVendor() {
			return vendor;
		}

		public void setVendor(String vendor) {
			this.vendor = vendor;
		}

		public String getModel() {
			return model;
		}

		public void setModel(String model) {
			this.model = model;
		}

		public long getCacheSize() {
			return cacheSize;
		}

		public void setCacheSize(long cacheSize) {
			this.cacheSize = cacheSize;
		}

		public String getUsed() {
			return used;
		}

		public void setUsed(String used) {
			this.used = used;
		}

		public String getIdle() {
			return idle;
		}

		public void setIdle(String idle) {
			this.idle = idle;
		}

		public double getUsedOrigVal() {
			return usedOrigVal;
		}

		public void setUsedOrigVal(double usedOrigVal) {
			this.usedOrigVal = usedOrigVal;
		}
		
	}
	
	/**
	 * 磁盘信息
	 * 功能描述：<br />
	 *  
	 * 创建日期：2013-6-19 下午12:17:02  <br />   
	 * 
	 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
	 * 
	 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
	 * 
	 * 修改记录： <br />
	 * 修 改 者    修改日期     文件版本   修改说明
	 */
	public static class DiskInfoVo extends NetTransObject{
		private static final long serialVersionUID = 1L;
		
		private String devName;//盘符名称
		private String dirName;//盘符路径
		private String sysTypeName;//文件系统类型，比如 FAT32、NTFS
		private String typeName;//文件系统类型名，比如本地硬盘、光驱、网络文件系统等
		private long totalSize;//总量
		private long availSize;//可用
		private long usedSize;//已用
		private String usePercent; //资源利用率
		private double diskReadRate; //读入
		private double diskWriteRate;//写出
		
		
		public String getDevName() {
			return devName;
		}
		public void setDevName(String devName) {
			this.devName = devName;
		}
		public String getDirName() {
			return dirName;
		}
		public void setDirName(String dirName) {
			this.dirName = dirName;
		}

		public String getSysTypeName() {
			return sysTypeName;
		}
		public void setSysTypeName(String sysTypeName) {
			this.sysTypeName = sysTypeName;
		}
		public String getTypeName() {
			return typeName;
		}
		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}
		public long getTotalSize() {
			return totalSize;
		}
		public void setTotalSize(long totalSize) {
			this.totalSize = totalSize;
		}

		public long getAvailSize() {
			return availSize;
		}
		public void setAvailSize(long availSize) {
			this.availSize = availSize;
		}
		public long getUsedSize() {
			return usedSize;
		}
		public void setUsedSize(long usedSize) {
			this.usedSize = usedSize;
		}
		public String getUsePercent() {
			return usePercent;
		}
		public void setUsePercent(String usePercent) {
			this.usePercent = usePercent;
		}
		public double getDiskReadRate() {
			return diskReadRate;
		}
		public void setDiskReadRate(double diskReadRate) {
			this.diskReadRate = diskReadRate;
		}
		public double getDiskWriteRate() {
			return diskWriteRate;
		}
		public void setDiskWriteRate(double diskWriteRate) {
			this.diskWriteRate = diskWriteRate;
		}
		
	}
}
