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

/**
 * 功能描述：<br />
 *  
 * 创建日期：2013-5-22 上午10:36:20  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public class CommondConst {

	/**
	 * 广播命令
	 */
	public static final String BROADCAST = "bcast";
	/**
	 * 注册服务命令
	 */
	public static final String REGISTER = "reg-service";
	/**
	 * 心跳检查命令
	 */
	public static final String HEART_BEAT = "heartbeat";
	/**
	 * 更新配置命令
	 */
	public static final String UPDATE_CONFIG = "update-config";
	/**
	 * 抓取数据命令标识
	 */
	public static final String FETCH = "fetch-data";
	
	/**
	 * 获取服务器信息命令
	 */
	public static final String GET_SERVER_INFO = "get-serverinfo";
	
	/**
	 * 获取监控汇总状态命令
	 */
	public static final String GET_GENERAL_STATUS = "get-generalstatus";
	
	/**
	 * 获取24小时服务器状态信息
	 */
	public static final String GET_SERVER_STATUS_24H = "GET_SERVER_STATUS_24H ";
	
	/**
	 * 获取数据库连接池状态命令
	 */
	public static final String GET_JDBC_POOL_STATUS = "get-jdbcpoolstatus";
	
	/**
	 * 客户端响应命令标识
	 */
	public static final String REPLY= "reply";
	/**
	 * 
	 */
	public static final String DEFAULT_DATA = "defaultdata";
	/**
	 * 监控节点客户端标识符
	 */
	public static final String CLIENT_ID = "client";
	/**
	 * 命令标识符
	 */
	public static final String CMD_ID = "cmdid";
	/**
	 * 错误标识符
	 */
	public static final String ERROR = "error";
	
}
