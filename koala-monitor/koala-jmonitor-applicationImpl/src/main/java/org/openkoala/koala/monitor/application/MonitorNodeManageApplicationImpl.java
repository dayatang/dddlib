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
package org.openkoala.koala.monitor.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.openkoala.exception.base.BaseException;
import org.openkoala.koala.commons.KoalaDateUtils;
import org.openkoala.koala.monitor.core.DataPolicyHandler;
import org.openkoala.koala.monitor.def.ComponentDef;
import org.openkoala.koala.monitor.domain.MonitorNode;
import org.openkoala.koala.monitor.domain.MonitorNode.MonitorComponent;
import org.openkoala.koala.monitor.model.GeneralMonitorStatusVo;
import org.openkoala.koala.monitor.model.JdbcPoolStatusVo;
import org.openkoala.koala.monitor.model.MonitorComponentVo;
import org.openkoala.koala.monitor.model.MonitorNodeVo;
import org.openkoala.koala.monitor.model.MonitorWarnInfoVo;
import org.openkoala.koala.monitor.model.ServerStatusVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

/**
 * 功能描述：<br />
 *  
 * 创建日期：2013-6-4 下午3:08:00  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
@Named(value="monitorNodeManageApplication")
@Transactional(value="km_transactionManager")
@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
@Stateless(name = "MonitorNodeManageApplication")
@Remote
public class MonitorNodeManageApplicationImpl implements
		MonitorNodeManageApplication {
	
	private static final Logger LOG = LoggerFactory.getLogger(MonitorNodeManageApplicationImpl.class);
	
	private DataPolicyHandler dataPolicyService;
	
	private DataPolicyHandler getDataPolicyHandler(){
		if(dataPolicyService==null){
			dataPolicyService = InstanceFactory.getInstance(DataPolicyHandler.class);
		}
		return dataPolicyService;
	}
	

	private QueryChannelService queryChannel;
	
	private QueryChannelService getQueryChannelService(){
		if(queryChannel==null){
			queryChannel = InstanceFactory.getInstance(QueryChannelService.class,"km_queryChannel");
		}
		return queryChannel;
	}

	@Override
	public List<MonitorNodeVo> getAllNodes() {
		List<MonitorNodeVo> result = new ArrayList<MonitorNodeVo>();
		try {
			Collection<MonitorNode> values = MonitorNode.getAllNodesCache().values();
			for (MonitorNode monitorNode : values) {
				MonitorNodeVo vo = new MonitorNodeVo();
				BeanUtils.copyProperties(vo, monitorNode);
				vo.setActive(true);
				result.add(vo);
			}
			
			List<MonitorNode> historyList = MonitorNode.getRepository().findAll(MonitorNode.class);
			if(historyList != null){
				for (MonitorNode monitorNode : historyList) {
					MonitorNodeVo vo = new MonitorNodeVo();
					vo.setNodeId(monitorNode.getNodeId());
					vo.setNodeName(monitorNode.getNodeName());
					if(!result.contains(vo)){
						vo.setActive(false);
						result.add(vo);
					}
				}
			}
			
		} catch (Exception e) {
			LOG.error("获取节点列表",e);
			throw new BaseException("", "获取节点列表错误");
		}
		
		return result;
	}

	
	
	@Override
	public void updateComponentConfig(String clientId,MonitorComponentVo compVo) {
		try {
			Set<MonitorComponent> conponents = MonitorNode.getAllNodesCache().get(clientId).getConponents();
			MonitorComponent comp = null;
			for (MonitorComponent c : conponents) {
				if(c.getType().equals(compVo.getType())){
					comp = c;
					break;
				}
			}
			if(comp == null)throw new RuntimeException("没有找到组件");
			//移除
			conponents.remove(comp);
			
			if(StringUtils.isNotBlank(compVo.getActive())){
				comp.setActive("true".equals(compVo.getActive()));
			}
			comp.setProperties(compVo.getProperties());
			conponents.add(comp);
			//重新添加
			ComponentDef def = new ComponentDef();
			BeanUtils.copyProperties(def,comp);
			getDataPolicyHandler().updateComponentConfig(clientId , def);
		} catch (Exception e) {
			LOG.error("更新监控组件配置错误",e);
			throw new BaseException("", "更新监控组件配置错误");
		}
		
	}
	
	
	@Override
	public ServerStatusVo getNodeServerStatus(String nodeId) {
		try {
			return getDataPolicyHandler().getServerStatusInfo(nodeId);
		} catch (Exception e) {
			LOG.error("获取服务器["+nodeId+"]状态错误",e);
			throw new BaseException("", "获取服务器状态错误");
		}
	}



	@Override
	public Page<MonitorWarnInfoVo> queryMonitorWarnInfos(
			MonitorWarnInfoVo search, int page, int pagesize) {
		
		return null;
	}



	@Override
	public GeneralMonitorStatusVo getGeneralMonitorStatus(String nodeId) {
		try {
			//今天开始时刻
			Date todayBegin = KoalaDateUtils.getDayBegin(new Date());
			GeneralMonitorStatusVo monitorStatus = getDataPolicyHandler().getGeneralMonitorStatus(nodeId);
			
			String jpql = "select AVG(h.timeConsume) from HttpDetails h where h.timeConsume>0 and h.beginTime>?";
			Object[] params = new Object[]{todayBegin};
			Double avg = getQueryChannelService().querySingleResult(jpql, params);
			if(avg != null){
				avg = new BigDecimal(avg/1000).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
				monitorStatus.setPageAvgResponseTime(avg);
				
			}
			jpql = "select AVG(h.timeConsume),h.uri from HttpDetails h where h.timeConsume>0 and h.beginTime>? group by h.uri order by AVG(h.timeConsume) desc";
			Object[] objs = getQueryChannelService().querySingleResult(jpql, params);
			if(objs !=null && objs.length>1){
				monitorStatus.setMaxAvgTimePage(objs[1].toString());
			}
			
			jpql = "select count(*) from MethodDetails m where m.beginTime>?";
			Long count = getQueryChannelService().querySingleResult(jpql, params);
			monitorStatus.setMethodCallCount(Integer.parseInt(count +""));
			
			jpql = "select count(*) from MethodDetails m where m.beginTime>? and m.successed=?";
			params = new Object[]{todayBegin,false};
			count = getQueryChannelService().querySingleResult(jpql, params);
			monitorStatus.setMethodExceptionCount(Integer.parseInt(count +""));
			
			jpql = "select count(m.method),m.method from MethodDetails m where m.beginTime>? group by m.method order by count(m.method) desc";
			params = new Object[]{todayBegin};
			objs = getQueryChannelService().querySingleResult(jpql, params);
			if(objs !=null && objs.length>1){
				monitorStatus.setMostCallMethod(objs[1].toString());
			}
			
			jpql = "select AVG(m.timeConsume),m.method from MethodDetails m where m.beginTime>? group by m.method order by AVG(m.timeConsume) desc";
			params = new Object[]{todayBegin};
			objs = getQueryChannelService().querySingleResult(jpql, params);
			if(objs !=null && objs.length>1){
				monitorStatus.setMaxAvgTimeMethod(objs[1].toString());
			}
			
			return monitorStatus;
		} catch (Exception e) {
			LOG.error("获取服务器["+nodeId+"]预警信息错误",e);
			throw new BaseException("", "获取服务器预警信息错误");
		}
		
	}



	@Override
	public MonitorNodeVo queryNode(String nodeId) {
		MonitorNodeVo result = new MonitorNodeVo();
		try {
			MonitorNode node = MonitorNode.getAllNodesCache().get(nodeId);
			BeanUtils.copyProperties(result, node);
			result.setConponents(null);
		} catch (Exception e) {
			
		}
		
		return result;
	}



	@Override
	public void updateNode(MonitorNodeVo node) {
		try {
			MonitorNode monitorNode = MonitorNode.getAllNodesCache().get(node.getNodeId());
			monitorNode.setMaxCacheSize(node.getMaxCacheSize());
			monitorNode.setCacheExpireTime(node.getCacheExpireTime());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	@Override
	public Map<String, JdbcPoolStatusVo> getJdbcPoolStatus(String nodeId) {
		if(!MonitorNode.getAllNodesCache().containsKey(nodeId)){
			throw new BaseException("NOT_CONNECTION", "无法连接监控节点");
		}
		Map<String, JdbcPoolStatusVo> status = getDataPolicyHandler().getJdbcPoolStatus(nodeId);
		return status;
	}
	
}
