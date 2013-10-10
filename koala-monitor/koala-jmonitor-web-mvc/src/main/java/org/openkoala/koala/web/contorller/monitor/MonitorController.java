package org.openkoala.koala.web.contorller.monitor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openkoala.koala.monitor.application.MonitorDataManageApplication;
import org.openkoala.koala.monitor.application.MonitorNodeManageApplication;
import org.openkoala.koala.monitor.common.Constant;
import org.openkoala.koala.monitor.common.KoalaDateUtils;
import org.openkoala.koala.monitor.model.CountVo;
import org.openkoala.koala.monitor.model.HttpDetailsVo;
import org.openkoala.koala.monitor.model.JdbcPoolStatusVo;
import org.openkoala.koala.monitor.model.JdbcStatementDetailsVo;
import org.openkoala.koala.monitor.model.MainStatVo;
import org.openkoala.koala.monitor.model.MethodDetailsVo;
import org.openkoala.koala.monitor.model.MonitorNodeVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayatang.querychannel.support.Page;

@Controller
@RequestMapping("/monitor/Monitor")
public class MonitorController {

	@Inject
	private MonitorNodeManageApplication monitorNodeManageApplication;

	@Inject
	private MonitorDataManageApplication monitorDataManageApplication;

	@ResponseBody
	@RequestMapping("/queryAllNodes")
	public final Map<String, Object> queryAllNodes() {
		Map<String, Object> dataMap = new HashMap<String,Object>();
		List<MonitorNodeVo> nodes = monitorNodeManageApplication.getAllNodes();
		dataMap.put("data", nodes);
		return dataMap;
	}

	/**
	 * 返回method统计列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/httpMonitorCount")
	public final Map<String, Object> httpMonitorCount(HttpServletRequest request) {
		Map<String, Object> dataMap = new HashMap<String,Object>();
		// 把页面参数封装成MainStatVo
		MainStatVo mainStatVo = this.packagingMainStatVoForHttpCount(request);

		List<CountVo> countVoList = monitorDataManageApplication
				.getHttpMonitorCount(mainStatVo);
		dataMap.put("Rows", countVoList);
		dataMap.put("queryTime", mainStatVo.getBeginTimeStr());
		return dataMap;
	}

	/**
	 * 返回method统计列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/methodMonitorCount")
	public final Map<String, Object> methodMonitorCount(HttpServletRequest request,Integer page,Integer pagesize) {
		Map<String, Object> dataMap = new HashMap<String,Object>();
		Page<CountVo> result = null;
		MainStatVo mainStatVo = this.packagingMainStatVoForMethodCount(request);

		// 方法统计类型
		String methodCountType = request.getParameter("methodCountType");
		// 默认统计方法调用次数
		if (StringUtils.isEmpty(methodCountType)
				|| Constant.COUNT_METHOD_COUNT.equals(methodCountType)) {
			result = monitorDataManageApplication.pageGetMethodMonitorCount(
					page, pagesize, mainStatVo);
		} else if (Constant.COUNT_METHOD_AVG_TIME_CONSUME
				.equals(methodCountType)) {
			result = monitorDataManageApplication
					.pageGetMethodMonitorAvgTimeConsume(page,
							pagesize, mainStatVo);
		} else if (Constant.COUNT_METHOD_EXCEPTIONS.equals(methodCountType)) {
			result = monitorDataManageApplication
					.pageGetMethodMonitorExceptionCount(page,
							pagesize, mainStatVo);
		}

		dataMap.put("Rows", result.getResult());
		dataMap.put("start", page * pagesize - pagesize);
		dataMap.put("limit", pagesize);
		dataMap.put("Total", result.getTotalCount());
		dataMap.put("timeStart", mainStatVo.getBeginTimeStr());
		dataMap.put("timeEnd", mainStatVo.getEndTimeStr());
		return dataMap;
	}

	/**
	 * 返回http监控节点明细列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/httpMonitorDetail")
	public final Map<String, Object> httpMonitorDetail(HttpServletRequest request,int page,int pagesize) {
		Map<String, Object> dataMap = new HashMap<String,Object>();
		Page<HttpDetailsVo> result = monitorDataManageApplication
				.pageGetHttpMonitorDetails(page,pagesize,
						this.packagingHttpDetailsVo(request));
		dataMap.put("Rows", result.getResult());
		dataMap.put("start", page * pagesize - pagesize);
		dataMap.put("limit", pagesize);
		dataMap.put("Total", result.getTotalCount());
		return dataMap;
	}

	/**
	 * 返回method监控节点明细列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/methodMonitorDetail")
	public final Map<String, Object> methodMonitorDetail(HttpServletRequest request,int page,int pagesize) {
		Map<String, Object> dataMap = new HashMap<String,Object>();
		Page<MethodDetailsVo> result = monitorDataManageApplication
				.pageGetMethodMonitorDetails(page,pagesize,
						this.packagingMethodDetailsVo(request));
		dataMap.put("Rows", result.getResult());
		dataMap.put("start", page * pagesize - pagesize);
		dataMap.put("limit", pagesize);
		dataMap.put("Total", result.getTotalCount());
		return dataMap;
	}

	/**
	 * 返回连接池监控节点明细列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/poolMonitorDetail")
	public final Map<String, Object> poolMonitorDetail(HttpServletRequest request) {
		Map<String, Object> dataMap = new HashMap<String,Object>();
		Map<String, JdbcPoolStatusVo> map = monitorNodeManageApplication
				.getJdbcPoolStatus(request.getParameter("nodeId"));
		dataMap.put("pools", map);
		return dataMap;
	}

	/**
	 * 返回sqls明细列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/sqlsMonitorDetail")
	public final Map<String, Object> sqlsMonitorDetail(HttpServletRequest request,int page,int pagesize) {
		Map<String, Object> dataMap = new HashMap<String,Object>();
		Page<JdbcStatementDetailsVo> result = monitorDataManageApplication
				.getSqlsMonitorDetails(page, pagesize,
						this.packagingJdbcStatementDetailsVo(request));
		dataMap.put("Rows", result.getResult());
		dataMap.put("start", page * pagesize - pagesize);
		dataMap.put("limit", pagesize);
		dataMap.put("Total", result.getTotalCount());
		return dataMap;
	}

	/**
	 * 返回堆栈信息
	 * 
	 * @return
	 */
	@RequestMapping("/stackTracesDetail")
	public final String stackTracesDetail(HttpServletRequest request) {
		List<String> stackTraces = monitorDataManageApplication
				.getStackTracesDetails(request.getParameter("monitorType"),
						request.getParameter("detailsId"));
		request.setAttribute("stackTraces", stackTraces);
		return "monitor/Monitor_stackTracesDetail";
	}

	/**
	 * JDBC连接耗时情况
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/jdbcTimeStat")
	public Map<String, Object> jdbcTimeStat(HttpServletRequest request,String nodeId) {
		Map<String, Object> dataMap = new HashMap<String,Object>();
		Map<Integer, Integer> stat = monitorDataManageApplication
				.getJdbcConnTimeStat(nodeId, Long.parseLong(request.getParameter("limit")));
		Set<Integer> keySet = stat.keySet();

		StringBuffer x = new StringBuffer();
		StringBuffer y = new StringBuffer();
		for (Integer key : keySet) {
			x.append(key).append(",");
			y.append(stat.get(key)).append(",");
		}
		x.deleteCharAt(x.length() - 1);
		y.deleteCharAt(y.length() - 1);
		dataMap.put("data", new String[] { x.toString(), y.toString() });
		return dataMap;
	}

	/**
	 * 封装http统计页面的查询条件
	 * 
	 * @param request
	 * @return
	 */
	private final MainStatVo packagingMainStatVoForHttpCount(
			HttpServletRequest request) {

		MainStatVo mainStatVo = new MainStatVo();

		String system = request.getParameter("system");
		mainStatVo.setPrincipal(system);
		String unit = request.getParameter("unit");
		mainStatVo.setUnit(unit);

		String queryTime = request.getParameter("queryTime");
		// 默认查询时间为当天
		if (StringUtils.isEmpty(queryTime)) {
			queryTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
		}
		mainStatVo.setBeginTimeStr(queryTime);
		return mainStatVo;
	}

	/**
	 * 封装method统计页面的查询条件
	 * 
	 * @param request
	 * @return
	 */
	private MainStatVo packagingMainStatVoForMethodCount(
			HttpServletRequest request) {

		String timeStart = request.getParameter("timeStart");
		String timeEnd = request.getParameter("timeEnd");
		// 默认查询时间为当天0点 至 当前时间
		if (StringUtils.isEmpty(timeStart) && StringUtils.isEmpty(timeEnd)) {
			Date now = new Date();
			timeStart = KoalaDateUtils.format2YYYY_MM_DD(now);
			timeEnd = KoalaDateUtils.format(now);
		}

		MainStatVo mainStatVo = new MainStatVo();

		mainStatVo.setPrincipal(request.getParameter("system"));
		mainStatVo.setBeginTime(KoalaDateUtils.parseDate(timeStart));
		mainStatVo.setEndTime(KoalaDateUtils.parseDate(timeEnd));
		mainStatVo.setBeginTimeStr(timeStart);
		mainStatVo.setEndTimeStr(timeEnd);

		return mainStatVo;
	}

	/**
	 * 把http明细查询参数封装成HttpDetailsVo
	 * 
	 * @param request
	 * @return
	 */
	private HttpDetailsVo packagingHttpDetailsVo(HttpServletRequest request) {
		HttpDetailsVo httpDetailsVo = new HttpDetailsVo();
		httpDetailsVo.setSystem(request.getParameter("system"));
		// 查询范围限制在该小时,大于等于开始时间，小于结束时间
		String requestDate = request.getParameter("requestDate");
		String unit = request.getParameter("unit");
		Date beginDt = KoalaDateUtils.parseDate(requestDate);
		Date endDt = null;
		if ("hour".equals(unit)) {
			endDt = DateUtils.addHours(beginDt, 1);
		} else if ("day".equals(unit)) {
			endDt = DateUtils.addDays(beginDt, 1);
		}
		httpDetailsVo.setBeginTime(beginDt);
		httpDetailsVo.setEndTime(endDt);
		httpDetailsVo.setSortname(request.getParameter("sortname"));
		httpDetailsVo.setSortorder(request.getParameter("sortorder"));
		return httpDetailsVo;
	}

	/**
	 * 把method明细查询参数封装成MethodDetailsVo
	 * 
	 * @param request
	 * @return
	 */
	private MethodDetailsVo packagingMethodDetailsVo(HttpServletRequest request) {
		MethodDetailsVo methodDetailsVo = new MethodDetailsVo();
		methodDetailsVo.setSystem(request.getParameter("nodeId"));
		methodDetailsVo.setThreadKey(request.getParameter("traceKey"));

		String timeStart = request.getParameter("timeStart");
		String timeEnd = request.getParameter("timeEnd");
		if(StringUtils.isNotBlank(timeStart)){
			methodDetailsVo.setBeginTime(KoalaDateUtils.parseDate(timeStart));
		}
		if(StringUtils.isNotBlank(timeEnd)){
			methodDetailsVo.setEndTime(KoalaDateUtils.parseDate(timeEnd));
		}
		methodDetailsVo.setSortname(request.getParameter("sortname"));
		methodDetailsVo.setSortorder(request.getParameter("sortorder"));

		methodDetailsVo.setMethod(request.getParameter("method"));

		return methodDetailsVo;
	}

	/**
	 * 把method的sql明细查询参数封装成JdbcStatementDetailsVo
	 * 
	 * @param request
	 * @return
	 */
	
	private JdbcStatementDetailsVo packagingJdbcStatementDetailsVo(
			HttpServletRequest request) {
		JdbcStatementDetailsVo sqlDetailsVo = new JdbcStatementDetailsVo();
		sqlDetailsVo.setMethodId(Long.parseLong(request
				.getParameter("methodId")));
		sqlDetailsVo.setSortname(request.getParameter("sortname"));
		sqlDetailsVo.setSortorder(request.getParameter("sortorder"));
		return sqlDetailsVo;
	}

}
