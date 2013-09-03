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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openkoala.koala.commons.KoalaBeanUtils;
import org.openkoala.koala.monitor.common.Constant;
import org.openkoala.koala.monitor.common.KoalaDateUtils;
import org.openkoala.koala.monitor.domain.HttpDetails;
import org.openkoala.koala.monitor.domain.JdbcStatementDetails;
import org.openkoala.koala.monitor.domain.MethodDetails;
import org.openkoala.koala.monitor.domain.MonitorNode;
import org.openkoala.koala.monitor.domain.MonitorNode.MonitorComponent;
import org.openkoala.koala.monitor.model.CountVo;
import org.openkoala.koala.monitor.model.HttpDetailsVo;
import org.openkoala.koala.monitor.model.JdbcStatementDetailsVo;
import org.openkoala.koala.monitor.model.MainStatVo;
import org.openkoala.koala.monitor.model.MethodDetailsVo;
import org.openkoala.koala.monitor.service.MonitorDataService;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.transaction.annotation.Transactional;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

/**
 * 功能描述：<br />
 *  
 * 创建日期：2013-6-19 下午2:08:17  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */

@Named(value="monitorDataManageApplication")
@Transactional(value="km_transactionManager")
@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
@Stateless(name = "MonitorDataManageApplication")
@Remote
public class MonitorDataManageApplicationImpl implements
		MonitorDataManageApplication {
	
private QueryChannelService queryChannel;
	
	private QueryChannelService getQueryChannelService(){
		if(queryChannel==null){
			queryChannel = InstanceFactory.getInstance(QueryChannelService.class,"km_queryChannel");
		}
		return queryChannel;
	}
	
	
	private JdbcTemplate jdbcTemplate;
	
	private JdbcTemplate getJdbcTemplate(){
		if(jdbcTemplate==null){
			jdbcTemplate = InstanceFactory.getInstance(JdbcTemplate.class,"km_jdbcTemplate");
		}
		return jdbcTemplate;
	}
	

	private MonitorDataService monitorDataService;
	
	private MonitorDataService getMonitorDataService(){
		if(monitorDataService==null){
			monitorDataService = InstanceFactory.getInstance(MonitorDataService.class);
		}
		return monitorDataService;
	}

	
	@Override
	public final List<CountVo> getHttpMonitorCount(MainStatVo mainStatVo) {
		return getMonitorDataService().getHttpMonitorCount(mainStatVo.getPrincipal(),mainStatVo.getUnit(),KoalaDateUtils.parseDate(mainStatVo.getBeginTimeStr()));
	}
	
	/**
	 * 获取监控(JDBC)统计信息
	 */
	/*@Override
	public Map<String, List<CountVo>> getAllMainStatsForJdbc(String countType,
			MainStatVo mainStatVo) {
		return monitorDataService.getAllMainStatsForJdbc(jdbcTemplate, countType, mainStatVo);
	}*/
	
	@Override
	public final Page<HttpDetailsVo> pageGetHttpMonitorDetails(int currentPage, int pageSize, HttpDetailsVo httpDetailsVo) {
		String queryStr = "from HttpDetails where nodeId = ? and beginTime>=? and beginTime<? order by " 
				+ httpDetailsVo.getSortname() + " " + httpDetailsVo.getSortorder();
		Object[] params = new Object[]{httpDetailsVo.getSystem(), httpDetailsVo.getBeginTime(), 
				httpDetailsVo.getEndTime()};
//		Object[] params = new Object[]{httpDetailsVo.getSystem(), httpDetailsVo.getBeginTimeStr(), 
//				httpDetailsVo.getEndTimeStr()};
		Page<HttpDetails> pageEntity = getQueryChannelService().queryPagedResultByPageNo(queryStr, params, currentPage, pageSize);
		
		List<HttpDetailsVo> list = KoalaBeanUtils.getNewList(pageEntity.getResult(), HttpDetailsVo.class);
		
		//添加耗时字段
//		this.addTimeConsumeForHttp(list);
		
		return new Page<HttpDetailsVo>(currentPage, pageEntity.getTotalCount(), pageSize, list);
	}
	
	@Override
	public final Page<CountVo> pageGetMethodMonitorCount(int currentPage,
			int pageSize, MainStatVo mainStatVo) {
		String queryStr = "select method,count(*) from MethodDetails where nodeId = ? and beginTime>=? and beginTime<?  group by method order by count(*) desc ";
		Object[] params = new Object[]{mainStatVo.getPrincipal(), mainStatVo.getBeginTime(), 
				mainStatVo.getEndTime()};
		Page<Object[]> pageEntity = getQueryChannelService().queryPagedResultByPageNo(queryStr, params, currentPage, pageSize);
		
		List<CountVo> list = this.turnToCountVoList(pageEntity.getResult());
		
		return new Page<CountVo>(currentPage, pageEntity.getTotalCount(), pageSize, list);
	}
	
	@Override
	public final Page<CountVo> pageGetMethodMonitorAvgTimeConsume(int currentPage,
			int pageSize, MainStatVo mainStatVo) {
		String queryStr = " select method,avg(timeConsume) from MethodDetails where nodeId = ? and beginTime>=? and beginTime<? group by method order by avg(timeConsume) desc ";
		Object[] params = new Object[]{mainStatVo.getPrincipal(), mainStatVo.getBeginTime(), 
				mainStatVo.getEndTime()};
		Page<Object[]> pageEntity = getQueryChannelService().queryPagedResultByPageNo(queryStr, params, currentPage, pageSize);
		
		List<CountVo> list = this.turnToCountVoAvgTimeConsumeList(pageEntity.getResult());
		
		return new Page<CountVo>(currentPage, pageEntity.getTotalCount(), pageSize, list);
	}

	@Override
	public final Page<CountVo> pageGetMethodMonitorExceptionCount(int currentPage,
			int pageSize, MainStatVo mainStatVo) {
		String queryStr = "select method,count(successed) from MethodDetails where successed=0" +
				" and nodeId = ? and beginTime>=? and beginTime<? group by method order by count(successed) desc ";
		Object[] params = new Object[]{mainStatVo.getPrincipal(), mainStatVo.getBeginTime(), 
				mainStatVo.getEndTime()};
		Page<Object[]> pageEntity = getQueryChannelService().queryPagedResultByPageNo(queryStr, params, currentPage, pageSize);
		
		List<CountVo> list = this.turnToCountVoExceptionList(pageEntity.getResult());
		
		return new Page<CountVo>(currentPage, pageEntity.getTotalCount(), pageSize, list);
	}
	
	@Override
	public final Page<MethodDetailsVo> pageGetMethodMonitorDetails(int currentPage,
			int pageSize, MethodDetailsVo methodDetailsVo) {
		
		StringBuffer jpql = new StringBuffer("from MethodDetails m where 1=1");
		List<Object> params = new ArrayList<Object>();
		
		if(methodDetailsVo != null){
			if(StringUtils.isNotBlank(methodDetailsVo.getThreadKey())){
				jpql.append(" and m.threadKey = ?");
				params.add(methodDetailsVo.getThreadKey());
			}
			if(StringUtils.isNotBlank(methodDetailsVo.getSystem())){
				jpql.append(" and m.nodeId = ?");
				params.add(methodDetailsVo.getSystem());
			}
			if(StringUtils.isNotBlank(methodDetailsVo.getMethod())){
				jpql.append(" and m.method = ?");
				params.add(methodDetailsVo.getMethod());
			}
			
			if(methodDetailsVo.getBeginTime() != null){
				jpql.append(" and m.beginTime >= ?");
				params.add(methodDetailsVo.getBeginTime());
			}
			
			if(methodDetailsVo.getEndTime() != null){
				jpql.append(" and m.beginTime <= ?");
				params.add(methodDetailsVo.getEndTime());
			}
			
			if(StringUtils.isNotBlank(methodDetailsVo.getSortname())){
				jpql.append(" order by ").append(methodDetailsVo.getSortname());
			}
			if(StringUtils.isNotBlank(methodDetailsVo.getSortorder())){
				jpql.append(" ").append(methodDetailsVo.getSortorder());
			}
		}
		Page<MethodDetails> pageEntity = getQueryChannelService().queryPagedResultByPageNo(jpql.toString(), params.toArray(), currentPage, pageSize);
		
		List<MethodDetailsVo> list = KoalaBeanUtils.getNewList(pageEntity.getResult(), MethodDetailsVo.class);
		
		return new Page<MethodDetailsVo>(currentPage, pageEntity.getTotalCount(), pageSize, list);
	}

	
	@Override
	public final Page<JdbcStatementDetailsVo> getSqlsMonitorDetails(int currentPage, int pageSize, JdbcStatementDetailsVo jdbcStatementDetailsVo) {

		String queryStr = " select a from JdbcStatementDetails a where a.jdbcConn.id in (select b.id from JdbcConnDetails b " +
				" where b.threadKey=(select c.threadKey from MethodDetails c where c.id=?)) order by " +
				jdbcStatementDetailsVo.getSortname() + " " + jdbcStatementDetailsVo.getSortorder();
		
		Page<JdbcStatementDetails> pageEntity = getQueryChannelService().queryPagedResultByPageNo(
				queryStr, new Object[]{jdbcStatementDetailsVo.getMethodId()}, currentPage, pageSize);
		
		List<JdbcStatementDetailsVo> list = KoalaBeanUtils.getNewList(pageEntity.getResult(), JdbcStatementDetailsVo.class);
		return new Page<JdbcStatementDetailsVo>(currentPage, pageEntity.getTotalCount(), pageSize, list);
	}
	
	@Override
	public final List<String> getStackTracesDetails(String monitorType, String detailsId){
		String queryStr = null;
		if(Constant.MONITOR_TYPE_METHOD.equals(monitorType)){
			queryStr = " SELECT stackTracesDetails FROM MethodDetails where ID = ? ";
		}else{
			throw new RuntimeException("not surpport monitorType:"+monitorType);
		}

		String result = getQueryChannelService().querySingleResult(queryStr, new Object[]{Long.parseLong(detailsId)});
		return this.splitStringToList(result);
	}
	
	/**
	 * 把结果封装成需要的格式
	 * @param list
	 * @return
	 */
	private final List<CountVo> turnToCountVoList(List<Object[]> list){
		List<CountVo> countVos = new ArrayList<CountVo>();
		for(Object[] array : list){
			CountVo countVo = new CountVo();
			countVo.setMethod(array[0].toString());
			countVo.setMethodCount(Integer.parseInt(array[1].toString()));
			countVos.add(countVo);
		}
		return countVos;
	}
	
	/**
	 * 把结果封装成需要的格式
	 * @param list
	 * @return
	 */
	private final List<CountVo> turnToCountVoAvgTimeConsumeList(List<Object[]> list){
		List<CountVo> countVos = new ArrayList<CountVo>();
		for(Object[] array : list){
			CountVo countVo = new CountVo();
			countVo.setMethod(array[0].toString());
			countVo.setAvgTimeConsume(Math.round(Double.parseDouble(array[1].toString())));
			countVos.add(countVo);
		}
		return countVos;
	}
	
	/**
	 * 把结果封装成需要的格式
	 * @param list
	 * @return
	 */
	private final List<CountVo> turnToCountVoExceptionList(List<Object[]> list){
		List<CountVo> countVos = new ArrayList<CountVo>();
		for(Object[] array : list){
			CountVo countVo = new CountVo();
			countVo.setMethod(array[0].toString());
			countVo.setMethodExceptionCount(Integer.parseInt(array[1].toString()));
			countVos.add(countVo);
		}
		return countVos;
	}
	
	/**
	 * 将字符串(以换行符为分隔)解析成集合
	 * @param sqlsAll
	 * @return
	 */
	private final List<String> splitStringToList(String str){
		List<String> list = new ArrayList<String>();
		if( !StringUtils.isEmpty(str) ){
			String[] lines = str.split("\n");
			for(String line : lines){
				if( !StringUtils.isEmpty(line) ){
					list.add(line);
				}
			}
		}
		return list;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Map<Integer, Integer> getJdbcConnTimeStat(String nodeId,
			long timeoutLimit) {
		
		timeoutLimit = timeoutLimit * 1000;//to 毫秒
		if(timeoutLimit < 0){
			Set<MonitorComponent> conponents = MonitorNode.getAllNodesCache().get(nodeId).getConponents();
			for (MonitorComponent com : conponents) {
				if(com.getType().equals("JDBC")){
					timeoutLimit = Long.parseLong(com.getProperties().get("trace-timeout"));
					break;
				}
			}
		}
		final Map<Integer, Integer> result = new TreeMap<Integer, Integer>();
		Date now = new Date();
		Date before24h = DateUtils.addHours(now, -24);
		String sql = "select m.hour, count(*) from K_M_JDBC_CONN_DETAILS c left join k_m_main_stat m on c.THREAD_KEY = m.THREAD_KEY and m.fk_node_id=? and (m.begin_time between ? and ?) and TIME_CONSUME>? group by m.hour order by m.hour";
		Object[] params = new Object[]{nodeId,before24h,now,timeoutLimit};
		
		getJdbcTemplate().query(sql, params,new ResultSetExtractor<Object>() {
			@Override
			public Object extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				while(rs.next()){
					if(rs.getObject(1) != null){
						result.put(rs.getInt(1), rs.getInt(2));
					}
				}
				return null;
			}
		});
		
		int beginPoint = before24h.getHours() + 1;
		int endPoint = now.getHours();
		int count = 0;
		for (int i = beginPoint; i < 24; i++) {
			count = result.containsKey(i) ? result.get(i) : 0;
			result.put(i, count);
		}
		
		for (int i = 0; i <= endPoint; i++) {
			count = result.containsKey(i) ? result.get(i) : 0;
			result.put(i, count);
		}
		
		return result;
	}

}