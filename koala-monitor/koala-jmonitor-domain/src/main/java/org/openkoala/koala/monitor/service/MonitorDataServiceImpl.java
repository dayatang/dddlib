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
package org.openkoala.koala.monitor.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openkoala.koala.monitor.common.Constant;
import org.openkoala.koala.monitor.common.KoalaDateUtils;
import org.openkoala.koala.monitor.def.CombineMethodTrace;
import org.openkoala.koala.monitor.def.HttpRequestTrace;
import org.openkoala.koala.monitor.def.JdbcConnTrace;
import org.openkoala.koala.monitor.def.Trace;
import org.openkoala.koala.monitor.domain.HttpDetails;
import org.openkoala.koala.monitor.domain.JdbcConnDetails;
import org.openkoala.koala.monitor.domain.JdbcStatementDetails;
import org.openkoala.koala.monitor.domain.MainStat;
import org.openkoala.koala.monitor.domain.MethodDetails;
import org.openkoala.koala.monitor.model.CountVo;
import org.openkoala.koala.monitor.model.HttpDetailsVo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * 功能描述：<br />
 *  
 * 创建日期：2013-6-8 上午9:35:32  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
@Transactional(value="km_transactionManager")
public class MonitorDataServiceImpl implements MonitorDataService {
	
	@Inject
	@Qualifier(value="km_jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public final void saveMonitorData(String clientId,List<Trace> traces) {
		//按线程轨迹发生时间排序
		Collections.sort(traces);
		
		//第一条记录为JDBC的不记录
		if("JDBC".equalsIgnoreCase(traces.get(0).getTraceType())){
			return;
		}
		
		MainStat main = new MainStat(clientId,new Date(traces.get(0).getCreatedTime()));
		main.setTraceId(traces.get(0).getThreadId());
		long endTime = traces.get(0).getCreatedTime();
		for (Trace trace : traces) {
			try {
				if(trace.getInactiveTime() > endTime){
					endTime = trace.getInactiveTime();
				}
				if(trace instanceof HttpRequestTrace){
					HttpDetails httpdetails = new HttpDetails();
					BeanUtils.copyProperties(httpdetails, trace);
					httpdetails.setNodeId(clientId);
					httpdetails.save();
				}else if(trace instanceof CombineMethodTrace){
					MethodDetails md = new MethodDetails();
					BeanUtils.copyProperties(md,trace);
					md.setNodeId(clientId);
					md.save();
				}else if(trace instanceof JdbcConnTrace){
					JdbcConnDetails conn = new JdbcConnDetails();
					BeanUtils.copyProperties(conn,trace);
					Trace[] statements = trace.getChildTraces();
					Trace[] rsTraces = null;
					for (Trace stmt : statements) {
						rsTraces = stmt.getChildTraces();
						if(rsTraces == null)continue;
						for (Trace rs : rsTraces) {
							JdbcStatementDetails sql = new JdbcStatementDetails();
							sql.setJdbcConn(conn);
							sql.setBeginTime(rs.getBeginTime());
							sql.setClosed(!stmt.isUnrecovered());
							sql.setSql(rs.getContent());
							sql.setTimeConsume(rs.getTimeConsume());
							conn.getStatements().add(sql);
						}
					}
					conn.setNodeId(clientId);
					conn.save();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		main.setEndTime(new Date(endTime));
		main.save();
	}


	@Override
	public final List<CountVo> getHttpMonitorCount(String nodeId, String statUnit, Date beginTime) {
		List<CountVo> list = new ArrayList<CountVo>();
		String sql = null;
		Date beginDt = KoalaDateUtils.getDayBegin(beginTime);
		Date endDt = null;
		if(Constant.UNIT_HOUR.equals(statUnit)){
			sql = "select m.hour, count(*) from k_m_http_details h left join k_m_main_stat m on h.TRACE_ID = m.TRACE_ID and m.fk_node_id=? and (m.begin_time between ? and ?) group by m.hour order by m.hour";
			endDt = KoalaDateUtils.getDayEnd(beginDt);
		}else if(Constant.UNIT_DAY.equals(statUnit)){
			sql = "select m.day, count(*) from k_m_http_details h left join k_m_main_stat m on h.TRACE_ID = m.TRACE_ID and m.fk_node_id=? and (m.begin_time between ? and ?) group by m.day order by m.day";
			endDt = KoalaDateUtils.getLastDateOfMonth(beginDt);
		}else{
			throw new RuntimeException("参数错误");
		}
		
		final Map<Integer, String> tmpMap = new HashMap<Integer, String>();
		Object[] objects = new Object[]{nodeId,beginDt,endDt};
		jdbcTemplate.query(sql, objects,new ResultSetExtractor<Object>() {
			@Override
			public Object extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				while(rs.next()){
					if(rs.getObject(1) != null){
						tmpMap.put(rs.getInt(1), rs.getObject(2).toString());
					}
				}
				return null;
			}
		});

		int index = Constant.UNIT_HOUR.equals(statUnit) ? 0 : 1;
		for (;;) {
			if(beginDt.compareTo(endDt)>0)break;
			CountVo countVo = new CountVo();
			if(tmpMap.containsKey(index)){
				countVo.setHttpCount(Integer.parseInt(tmpMap.get(index)));
			}else{
				countVo.setHttpCount(0);
			}
			list.add(countVo);
			if(Constant.UNIT_HOUR.equals(statUnit)){
				countVo.setDateStr(KoalaDateUtils.format(beginDt,"yyyy-MM-dd HH"));
				beginDt = DateUtils.addHours(beginDt, 1);
			}else if(Constant.UNIT_DAY.equals(statUnit)){
				countVo.setDateStr(KoalaDateUtils.format(beginDt,"yyyy-MM-dd"));
				beginDt = DateUtils.addDays(beginDt, 1);
			}
			index++;
		}
		return list;
	}
	
	@Override
	public final List<HttpDetailsVo> pageGetHttpMonitorDetails(int start, int pageSize, HttpDetailsVo httpDetailsVo) {
		String sql = " select uri,ip,parameters,begin_time,end_time from k_m_http_details where trace_id in(select m.trace_id from k_m_main_stat m where m.fk_node_id=? and m.begin_time>=? and m.begin_time<?) ";
		return jdbcTemplate.query(sql, 
				new Object[]{httpDetailsVo.getSystem(), httpDetailsVo.getBeginTime(), httpDetailsVo.getEndTime()}, 
				new RowMapper<HttpDetailsVo>(){
			@Override
			public HttpDetailsVo mapRow(ResultSet rs, int index)
					throws SQLException {
				HttpDetailsVo httpDetailsVo = new HttpDetailsVo();
				httpDetailsVo.setUri(rs.getString("uri"));
				httpDetailsVo.setIp(rs.getString("ip"));
				httpDetailsVo.setParameters(rs.getString("parameters"));
				httpDetailsVo.setBeginTime(rs.getDate("begin_time"));
				httpDetailsVo.setEndTime(rs.getDate("end_time"));
				return httpDetailsVo;
			}
		});
	}

}
