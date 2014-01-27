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

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.apache.mina.core.session.IoSession;
import org.openkoala.koala.monitor.core.DataPolicyHandler;
import org.openkoala.koala.monitor.datasync.base.ClientRequestListener;
import org.openkoala.koala.monitor.datasync.server.DatasyncServer;
import org.openkoala.koala.monitor.def.ComponentDef;
import org.openkoala.koala.monitor.def.NodeDef;
import org.openkoala.koala.monitor.def.Trace;
import org.openkoala.koala.monitor.domain.MonitorNode;
import org.openkoala.koala.monitor.domain.MonitorNode.MonitorComponent;
import org.openkoala.koala.monitor.extend.BaseSchedulerBean;
import org.openkoala.koala.monitor.model.GeneralMonitorStatusVo;
import org.openkoala.koala.monitor.model.JdbcPoolStatusVo;
import org.openkoala.koala.monitor.model.ServerStatusVo;
import org.openkoala.koala.monitor.remote.Commond;
import org.openkoala.koala.monitor.remote.CommondConst;
import org.openkoala.koala.monitor.service.MonitorDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

/**
 * 功能描述：远程数据策略mina服务端处理类<br />
 * 
 * 创建日期：2013-5-21 下午3:57:11 <br />
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作 者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者 修改日期 文件版本 修改说明
 */
public class RemoteDataPolicyServerHandler extends BaseSchedulerBean implements DataPolicyHandler{

	private static final Logger LOG = LoggerFactory.getLogger(RemoteDataPolicyServerHandler.class);

	
	@Inject
	private MonitorDataService monitorDataService;
	
	private DatasyncServer minaServer;
	
	//同步等待应答结果
	private static Map<String, Commond> syncWaitReplyResults = new ConcurrentHashMap<String, Commond>();
	//等待响应超时时间
	private static final long WAIT_REPLY_TIMEOUT = 60 * 1000;
	
	
	private int getPort(){
		try {
			Properties prop = new Properties();
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("props/mina-server.properties");
			prop.load(inputStream);
			return Integer.parseInt(prop.get("server.port") + "");
		} catch (Exception e) {
			LOG.warn("解析端口配置文件失败[{}]，默认使用端口：9000",e.getMessage());
			return 9000;
		}
		
	}

	private void  startupMinaServer(){
    	minaServer = new DatasyncServer(getPort(), new ClientRequestListener() {
			
			@Override
			public void afterClientRequest(IoSession session, Commond message) {
				processClientRequestData(session, message);
			}
			
			@Override
			public void afterClientClosed(IoSession session) {
				String clientKey = (String) session.getAttribute("clientKey");
				if (clientKey != null){
					final MonitorNode node = new MonitorNode();
					node.setNodeId(clientKey);
					
					transactionTemplate.execute(new TransactionCallback<Object>() {
						@Override
						public Object doInTransaction(TransactionStatus status) {
							node.inactive();
							return null;
						}
					});
				}
			}
		});
    	
    	try {
			minaServer.startup();
		} catch (Exception e) {
			minaServer = null;
			LOG.error("监控同步服务MINA服务端未能正常启动",e);
		}
    }	
	


	/**
	 * 处理客户端请求数据
	 * @param session
	 * @param cmd
	 */
	private void processClientRequestData(IoSession session, Commond cmd) {
		String clientKey = null;
		Map<String, String> headers = cmd.getHeaders();
		clientKey = headers.get(CommondConst.CLIENT_ID);
		if(clientKey == null)clientKey = (String) session.getAttribute("clientKey");
		
		if(LOG.isDebugEnabled())LOG.debug("收到来自客户端[" + clientKey +"]请求指令["+cmd.getCommondText()+"]消息指令["+cmd.getCommondID()+"]");
		
		if (CommondConst.REPLY.equals(cmd.getCommondText())) {
			//放入等待结果
			syncWaitReplyResults.put(cmd.getCommondID(), cmd);
		}else if (CommondConst.REGISTER.equals(cmd.getCommondText())) {
			registerMonitorNode(session, cmd, clientKey);
		}else if (CommondConst.HEART_BEAT.equals(cmd.getCommondText())) {
			minaServer.getSessions().put(clientKey, session);
			session.setAttribute("clientKey", clientKey);
			//更新心跳时间
			MonitorNode.getAllNodesCache().get(clientKey).setLatestSessionTime(new Date());
			if(LOG.isDebugEnabled())LOG.debug("监控节点[{}]心跳检查成功", clientKey);
		}
	}


	/**
	 * 注册
	 * @param session
	 * @param cmd
	 * @param clientKey
	 */
	private void registerMonitorNode(IoSession session, Commond cmd,String clientKey){

		Map<String, List<Serializable>> datas = cmd.getDatas();
		NodeDef clientNodeDef = (NodeDef) datas.get(CommondConst.DEFAULT_DATA).get(0);
		if(MonitorNode.getAllNodesCache().containsKey(clientNodeDef.getId())){
			LOG.warn("系统标识符[{}]已经存在",clientNodeDef.getId());
			// 通知客户端
			Commond reply = new Commond(CommondConst.REPLY);
			reply.addHeader(CommondConst.ERROR, "远程注册失败,系统ID["+clientNodeDef.getId()+"]重复");
			session.write(reply);
			return;
		}
		final MonitorNode node = new MonitorNode();
		node.setNodeId(clientNodeDef.getId());
		node.setNodeName(clientNodeDef.getName());
		node.setNodeUri(clientNodeDef.getUri());
		node.setLatestSessionTime(new Date());
		
		List<ComponentDef> components = clientNodeDef.getComponents();
		for (ComponentDef compDef : components) {
			node.getConponents().add(new MonitorComponent(compDef));
		}
		
		transactionTemplate.execute(new TransactionCallback<Object>() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
				node.active();
				return null;
			}
		});
		minaServer.getSessions().put(clientKey, session);
		//MonitorNodeVo
		session.setAttribute("clientKey", clientKey);
		if(LOG.isDebugEnabled())LOG.debug("监控节点[{}]心跳检查成功", clientKey);
	
	}
	
	private void stopMinaServer() {
		try {
			for (IoSession session : minaServer.getSessions().values()) {
				if (session.isConnected()) {
					session.close(true);
				}
			}
		} catch (Exception e) {
			
		}
		minaServer.getSessions().clear();
	}



	@Override
	@PostConstruct
	public void onStart() {
		super.onStart();
		startupMinaServer();
	}

	@Override
	@PreDestroy
	public void onStop() {
		super.onStop();
		stopMinaServer();
	}

	@Override
	public void doJob() throws Exception {
		if(minaServer == null){
			LOG.warn("MINA 服务端未正常运行");
			return;
		}
		if(minaServer.getSessions().size() == 0){
			LOG.warn("==当前无监控节点===");
			return;
		}
		Commond commond = new Commond(CommondConst.FETCH);
		for (String clientId : minaServer.getSessions().keySet()) {
			if(LOG.isDebugEnabled())LOG.debug("向节点[{}]发送监控数据同步指令[{}]",clientId,commond.getCommondText());
			try {
				Commond result = sendCommonWaitReply(clientId, commond);
				processMonitorDatas(clientId, result);
			} catch (Exception e) {
				LOG.error("同步节点["+clientId+"]监控数据发生错误",e);
			}
			
			MonitorNode.refreshNodeLastActiveTime(clientId);
			if(LOG.isDebugEnabled())LOG.debug("完成节点[{}]监控数据同步,指令[{}]",clientId,commond.getCommondText());
		}
	}
	
	
	/**
	 * 处理监控数据
	 * @param clientId
	 * @param cmdResult
	 */
	private void processMonitorDatas(String clientId ,Commond cmdResult){

		int count = 0;
		Map<String, List<Serializable>> datas = cmdResult.getDatas();
		Iterator<List<Serializable>> its = datas.values().iterator();
		List<Trace> tempTrace = new ArrayList<Trace>();
		while(its.hasNext()){
			tempTrace.clear();
			List<Serializable> next = its.next();
			for (Serializable o : next) {
				tempTrace.add((Trace)o);
				count++;
			}
			monitorDataService.saveMonitorData(clientId, tempTrace);
		}
		if(LOG.isDebugEnabled())LOG.debug("完成保存监控数据记录数："+count);
	
	}



	@Override
	public void updateComponentConfig(String clientId,ComponentDef comp) {
		Commond commond = new Commond(CommondConst.UPDATE_CONFIG);
		commond.addData(CommondConst.DEFAULT_DATA, comp);
		sendCommonWaitReply(clientId, commond);
	}



	@Override
	public ServerStatusVo getServerStatusInfo(String clientId) {
		Commond commond = new Commond(CommondConst.GET_SERVER_INFO);
		Commond result = sendCommonWaitReply(clientId, commond);
		return (ServerStatusVo)result.getSingleData();
	}
	
	@Override
	public GeneralMonitorStatusVo getGeneralMonitorStatus(String nodeId) {
		Commond commond = new Commond(CommondConst.GET_GENERAL_STATUS);
		Commond result = sendCommonWaitReply(nodeId, commond);
		return (GeneralMonitorStatusVo)result.getSingleData();
	}

	@Override
	public Map<String, JdbcPoolStatusVo> getJdbcPoolStatus(String nodeId) {
		Commond commond = new Commond(CommondConst.GET_JDBC_POOL_STATUS);
		Commond result = sendCommonWaitReply(nodeId, commond);
		
		Map<String, JdbcPoolStatusVo> map = new HashMap<String, JdbcPoolStatusVo>();
		Map<String, List<Serializable>> datas = result.getDatas();
		for (String key : datas.keySet()) {
			map.put(key, (JdbcPoolStatusVo)datas.get(key).get(0));
		}
		return map;
	} 
	
	
	/**
	 * 发送命令并等待结果
	 * @param clientId
	 * @param commond
	 * @return
	 */
	private Commond sendCommonWaitReply(String clientId,Commond commond){
		if(minaServer == null){
			LOG.warn("MINA 服务端未正常运行");
			throw new RuntimeException("MINA 服务端未正常运行");
		}
		minaServer.sendData(clientId, commond);
		String commondID = commond.getCommondID();
		Commond result = null;
		long begin = System.currentTimeMillis();
		while(result == null){
			result = syncWaitReplyResults.get(commondID);
			if(result != null){
				syncWaitReplyResults.remove(commondID);
				
				if(result.isError()){
					throw new RuntimeException(result.getError());
				}
				return result;
			}
			if(System.currentTimeMillis() - begin > WAIT_REPLY_TIMEOUT){
				throw new RuntimeException("等待响应超时");
			}
		}
		throw new RuntimeException("获取响应数据失败");
	}

	public void setMonitorDataService(MonitorDataService monitorDataService) {
		this.monitorDataService = monitorDataService;
	}

	public void setMinaServer(DatasyncServer minaServer) {
		this.minaServer = minaServer;
	}
}
