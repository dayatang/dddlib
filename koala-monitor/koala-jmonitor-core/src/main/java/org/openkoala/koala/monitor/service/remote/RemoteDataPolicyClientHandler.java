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
package org.openkoala.koala.monitor.service.remote;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.openkoala.koala.monitor.component.task.ServiceConnectionCheckTask;
import org.openkoala.koala.monitor.core.RuntimeContext;
import org.openkoala.koala.monitor.datasync.base.ServerCommondListener;
import org.openkoala.koala.monitor.datasync.client.DatasyncClient;
import org.openkoala.koala.monitor.jwebap.ComponentDef;
import org.openkoala.koala.monitor.jwebap.HttpRequestTrace.ActiveUser;
import org.openkoala.koala.monitor.jwebap.NetTransObject;
import org.openkoala.koala.monitor.jwebap.Trace;
import org.openkoala.koala.monitor.model.GeneralMonitorStatusVo;
import org.openkoala.koala.monitor.model.JdbcPoolStatusVo;
import org.openkoala.koala.monitor.model.ServerStatusVo;
import org.openkoala.koala.monitor.remote.Commond;
import org.openkoala.koala.monitor.remote.CommondConst;
import org.openkoala.koala.monitor.support.JdbcPoolStatusCollector;
import org.openkoala.koala.monitor.support.ServerStatusCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能描述：远程数据策略mina客户端处理类<br />
 * 
 * 创建日期：2013-5-30 上午10:17:26 <br />
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作 者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者 修改日期 文件版本 修改说明
 */
public class RemoteDataPolicyClientHandler{

	private static final Logger LOG = LoggerFactory.getLogger(RemoteDataPolicyClientHandler.class);
	
	// 同步数据客户端
	private DatasyncClient minaClient;
	// 心跳检测定时任务
	private Timer syscService_heartbeat_timer;
	
	protected RuntimeContext context;
	
	/**
	 * @param context
	 */
	public RemoteDataPolicyClientHandler() {}
	
	public void startup(RuntimeContext context) {
		this.context = context;
		Map<String, String> props = context.getNodeDef().getDataPolicy().getProperties();
		String host = props.get("host");
		final String clientId = context.getNodeDef().getId();
		Integer port = Integer.parseInt(props.get("port"));
		Integer heartbeat = Integer.parseInt(props.get("heartbeat"));
		Integer timeout = Integer.parseInt(props.get("timeout"));
		minaClient = new DatasyncClient(host, port, timeout*1000,new ServerCommondListener() {
			@Override
			public void execute(Commond command) {
				processServerCommand(command);
			}
		});
		
		boolean startFail = false;
		try {
			minaClient.startup();
		} catch (Exception e) {
			LOG.warn("Connection to Server fail! cause:[{}->{}]",e.toString(),e.getMessage());
			startFail = true;
		}
		
		//注册
		if(!startFail)registerCurrentNode(clientId);
		
		//心跳检测
		syscService_heartbeat_timer = new Timer();
		syscService_heartbeat_timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(minaClient.isConnected()){
					Commond commond = new Commond(CommondConst.HEART_BEAT);
					commond.addHeader(CommondConst.CLIENT_ID, clientId);
					boolean result = sendDataToServer(commond);
					if(result && LOG.isDebugEnabled())LOG.debug("发送心跳检测OK");
				}else{
					//注册
					registerCurrentNode(clientId);
				}
			}
		}, 60*1000, heartbeat*1000);
		
		LOG.info("===连接Server端心跳检查服务启动成功====");

	}

	/**
	 * 注册当前节点
	 * @param context
	 * @param clientId
	 */
	private void registerCurrentNode(final String clientId) {
		Commond commond = new Commond(CommondConst.REGISTER);
		commond.addHeader(CommondConst.CLIENT_ID, clientId);
		commond.addData(CommondConst.DEFAULT_DATA, context.getNodeDef());
		boolean result = sendDataToServer(commond);
		if(result && LOG.isDebugEnabled())LOG.debug("监控数据同步服务启动并远程注册成功");
	}

	
	/**
	 * 发送数据到服务端
	 * @param data
	 */
	private synchronized boolean sendDataToServer(Object data){
		if(!minaClient.isConnected()){
			boolean connect = minaClient.resetConnect();
			if(!connect){
				LOG.warn("重新连接监控服务器失败");
				return false;
			}
		}
		minaClient.sendData(data);
		return minaClient.isConnected();
	}
	
	/**
	 * 处理服务端命令
	 * @param commandText
	 */
	private void processServerCommand(Commond command){
		
		if(command.isError()){
			LOG.warn(command.getError());
			return;
		}
		String clientId = context.getNodeDef().getId();
		String commandText = command.getCommondText();
		try {
			if(LOG.isDebugEnabled())LOG.debug("接收到服务端指令[" + commandText+"]指令ID["+command.getCommondID()+"]");
			if(CommondConst.FETCH.equals(commandText)){
				
				Map<String, List<Trace>> allCacheTrace = RuntimeContext.getContext().getDataCache().getAllCacheTrace();
				
				Commond cmd = new Commond(CommondConst.REPLY);
				cmd.addHeader(CommondConst.CLIENT_ID, clientId);
				cmd.addHeader(CommondConst.CMD_ID, command.getCommondID());
				
				if(allCacheTrace.size()>0){
					List<String> traceKeys = new ArrayList<String>(allCacheTrace.keySet());
					List<Trace> tempTraces;
		            for (String traceKey : traceKeys) {
		            	tempTraces = allCacheTrace.get(traceKey);
		            	for (Trace trace : tempTraces) {
		            		cmd.addData(traceKey, trace);
						}
					}
				}
				//send 
	            sendDataToServer(cmd);
	            
			}else if(CommondConst.UPDATE_CONFIG.equals(commandText)){
				ComponentDef comp = (ComponentDef) command.getSingleData();
				RuntimeContext.getContext().getJwebapDefManager().updateComponentConfig(comp);
				replyServerCommond(clientId,command.getCommondID(), null, null);
				
			}else if(CommondConst.GET_SERVER_INFO.equals(commandText)){//获取服务器信息
				ServerStatusVo status = ServerStatusCollector.getServerAllStatus();
				replyServerCommond(clientId,command.getCommondID(), status, null);
			}else if(CommondConst.GET_GENERAL_STATUS.equals(commandText)){//
				replyServerCommond(clientId,command.getCommondID(), getGeneralMonitorStatus(), null);
			}else if(CommondConst.GET_JDBC_POOL_STATUS.equals(commandText)){//
				Map<String, JdbcPoolStatusVo> status = JdbcPoolStatusCollector.getInstance().currentAllDataSourceStatus();
				Commond commond = new Commond(CommondConst.REPLY);
				commond.addHeader(CommondConst.CMD_ID, command.getCommondID());
				commond.addHeader(CommondConst.CLIENT_ID, clientId);
				for (String key : status.keySet()) {
					commond.addData(key, status.get(key));
				}
				sendDataToServer(commond);
			}
			if(LOG.isDebugEnabled())LOG.debug("完成响应指令:"+commandText);
		} catch (Exception e) {
			replyServerCommond(clientId,command.getCommondID(), null, e);
			LOG.warn("响应指令:"+commandText+"发生错误");
			if(LOG.isDebugEnabled())LOG.debug("错误信息",e);
		}
	}
	
	
	/**
	 * 响应服务端命令
	 * @param clientId
	 * @param commondId
	 * @param data
	 * @param e
	 */
	private void replyServerCommond(String clientId,String commondId,NetTransObject data,Exception e){
		Commond commond = new Commond(CommondConst.REPLY);
		commond.addHeader(CommondConst.CMD_ID, commondId);
		commond.addHeader(CommondConst.CLIENT_ID, clientId);
		if(data !=null)commond.addData(CommondConst.DEFAULT_DATA, data);
		if(e !=null)commond.addHeader(CommondConst.ERROR,"发生异常["+e.getClass().getName()+"]异常信息：["+e.getMessage()+"]");
		sendDataToServer(commond);
	}
	
	private GeneralMonitorStatusVo getGeneralMonitorStatus() {
		GeneralMonitorStatusVo vo = new GeneralMonitorStatusVo();
		//在线人数
		Map<String, ActiveUser> users = RuntimeContext.getContext().getDataCache().getActiveSessions();
		//恶意访问IP
		Iterator<ActiveUser> it = users.values().iterator();
		while(it.hasNext()){
			ActiveUser user = it.next();
			if(user.isAbNormal())vo.getAbnormalIps().add(user.getIp());
		}
		vo.setActiveCount(users.size());
		//服务器状态
		vo.setServerStatus(ServerStatusCollector.getServerAllStatus());
		//第三方服务
		vo.setAbnormalServices(ServiceConnectionCheckTask.getBadSevices());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		vo.setServiceCheckTime(dateFormat.format(ServiceConnectionCheckTask.getLastCheckTime()));
		return vo;
	}
}
