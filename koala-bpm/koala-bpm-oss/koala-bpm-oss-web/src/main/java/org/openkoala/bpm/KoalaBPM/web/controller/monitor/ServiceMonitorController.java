package org.openkoala.bpm.KoalaBPM.web.controller.monitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.openkoala.koala.monitor.application.ServiceMonitorApplication;
import org.openkoala.koala.monitor.model.ScheduleConfVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/monitor/ServiceMonitor")
public class ServiceMonitorController {

	@Inject
	private ServiceMonitorApplication serviceMonitorApplication;
	
	@ResponseBody
	@RequestMapping("/queryAllSchedulers")
	public Map<String, Object> queryAllSchedulers(){
		Map<String, Object> dataMap = new HashMap<String,Object>();
		List<ScheduleConfVo> schedulers = serviceMonitorApplication.queryAllSchedulers();
		dataMap.put("data", schedulers);
		return dataMap;
	}
	
	@ResponseBody
	@RequestMapping("/updateScheduleConf")
	public Map<String, Object> updateScheduleConf(HttpServletRequest request){
		Map<String, Object> dataMap = new HashMap<String,Object>();
		try {
			ScheduleConfVo vo = new ScheduleConfVo();
			String triggerName = request.getParameter("triggerName");
			vo.setTriggerName(triggerName);
			String confExpr = request.getParameter("confExpr");
			String currentStat = request.getParameter("currentStat");
			if(StringUtils.isNotBlank(confExpr)){
				vo.setCronExpr(confExpr);
				serviceMonitorApplication.updateScheduleConf(vo);
			}else if(StringUtils.isNotBlank(currentStat)){
				if(currentStat.equals("true")){
					vo.setActive(false);
				}else{
					vo.setActive(true);
				}
				serviceMonitorApplication.updateScheduleConf(vo);
			}
			dataMap.put("success", true);
		} catch (Exception e) {
			dataMap.put("success", e.getMessage());
		}
		return dataMap;
	}
}
