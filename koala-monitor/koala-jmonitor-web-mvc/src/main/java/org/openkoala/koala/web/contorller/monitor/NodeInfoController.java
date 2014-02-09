package org.openkoala.koala.web.contorller.monitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.openkoala.koala.monitor.application.MonitorNodeManageApplication;
import org.openkoala.koala.monitor.application.ServiceMonitorApplication;
import org.openkoala.koala.monitor.model.GeneralMonitorStatusVo;
import org.openkoala.koala.monitor.model.MonitorComponentVo;
import org.openkoala.koala.monitor.model.MonitorNodeVo;
import org.openkoala.koala.monitor.model.MonitorWarnInfoVo;
import org.openkoala.koala.monitor.model.ScheduleConfVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayatang.querychannel.support.Page;

@Controller
@RequestMapping("/monitor/NodeInfo")
public class NodeInfoController {

	 
	@Inject
	private MonitorNodeManageApplication monitorNodeManageApplication;
	
	@Inject
	private ServiceMonitorApplication serviceMonitorApplication;
	
	/**
	 * 显示当前监控节点
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/queryAllNodes")
    public Map<String, Object> queryAllNodes(){
    	Map<String, Object> dataMap = new HashMap<String,Object>();
    	List<MonitorNodeVo> nodes = monitorNodeManageApplication.getAllNodes();
        dataMap.put("Rows", nodes);
    	return dataMap;
    }
    
	@ResponseBody
	@RequestMapping("/httpMonitorCount")
    public Map<String, Object> queryNode(String nodeId){
    	Map<String, Object> dataMap = new HashMap<String,Object>();
    	MonitorNodeVo node = monitorNodeManageApplication.queryNode(nodeId);
    	dataMap.put("data", node);
    	return dataMap;
    }
    
	@ResponseBody
	@RequestMapping("/updateNode")
    public Map<String, Object> updateNode(HttpServletRequest request,String nodeId){
    	Map<String, Object> dataMap = new HashMap<String,Object>();
    	MonitorNodeVo node = new MonitorNodeVo();
    	node.setNodeId(nodeId);
    	node.setCacheExpireTime(Integer.parseInt(request.getParameter("expireTime")));
    	node.setMaxCacheSize(Integer.parseInt(request.getParameter("maxCacheSize")));
    	node.setSyncDataInterval(Integer.parseInt(request.getParameter("interval")));
    	
    	monitorNodeManageApplication.updateNode(node);
    	return dataMap;
    }
    

    /**
     * 更新节点
     * @return
     */
	@ResponseBody
	@RequestMapping("/updateMonitorConfig")
    public Map<String, Object> updateMonitorConfig(HttpServletRequest request,String nodeId){
    	Map<String, Object> dataMap = new HashMap<String,Object>();
    	MonitorComponentVo comp = new MonitorComponentVo();
    	comp.setActive(request.getParameter("active"));
    	comp.setType(request.getParameter("compType"));
    	String props = request.getParameter("props");
    	if(StringUtils.isNotBlank(props)){
    		String[] propArr = props.split("@@@");
    		for (String prop : propArr) {
				if(prop.contains("->")){
					String[] datas = prop.split("->");
					if(datas.length==2)comp.getProperties().put(datas[0], datas[1]);
				}
			}
    	}
    	dataMap.put("result", true);
    	try{
    		monitorNodeManageApplication.updateComponentConfig(nodeId, comp);
    	}catch(Exception e){
    		dataMap.put("result", false);
    	}
    	return dataMap;
    }
    
    /**
     * 汇总监控信息
     * @return
     * @throws Exception 
     */
	@ResponseBody
	@RequestMapping("/serverSummryInfo")
    public Map<String, Object> serverSummryInfo(HttpServletRequest request,String nodeId) throws Exception{
    	if(StringUtils.isBlank(nodeId)){
    		throw new RuntimeException("参数不能为空");
    	}
    	GeneralMonitorStatusVo monitorStatus = monitorNodeManageApplication.getGeneralMonitorStatus(nodeId);
    	Map<String, Object> dataMap;
    	dataMap = monitorStatus.formatAsMap();
    	if(dataMap == null)dataMap = new HashMap<String,Object>();
    	return dataMap;
    }
    
    
    /**
     * 查询所有预警信息
     * @return
     */
	@ResponseBody
	@RequestMapping("/queryWarnInfos")
    public Map<String, Object> queryWarnInfos(int page,int pagesize){
    	Map<String, Object> dataMap = new HashMap<String,Object>();
    	MonitorWarnInfoVo search = new MonitorWarnInfoVo();
    	Page<MonitorWarnInfoVo> pageInfo = monitorNodeManageApplication.queryMonitorWarnInfos(search, page, pagesize);
    	//Page<AccountVO> all = accountApplication.pageQueryAccount(accountVO, page, pagesize);
		dataMap.put("Rows", pageInfo.getResult());
		dataMap.put("start", page * pagesize - pagesize);
		dataMap.put("limit", pagesize);
		dataMap.put("Total", pageInfo.getTotalCount());
    	return dataMap;
    }

	@ResponseBody
	@RequestMapping("/getScheduleConf")
    public Map<String, Object> getScheduleConf(){
    	Map<String, Object> dataMap = new HashMap<String,Object>();
    	ScheduleConfVo conf = serviceMonitorApplication.queryScheduler("dataPolicyTrigger");
    	dataMap.put("data", conf);
    	return dataMap;
    }
    
	@ResponseBody
	@RequestMapping("/updateScheduleConf")
    public Map<String, Object> updateScheduleConf(HttpServletRequest request){
    	Map<String, Object> dataMap = new HashMap<String,Object>();
    	ScheduleConfVo conf = new ScheduleConfVo();
    	conf.setTriggerName("dataPolicyTrigger");
    	conf.setActiveAsString(request.getParameter("active"));
    	conf.setInterval(Integer.parseInt(request.getParameter("interval")));
    	dataMap.put("result", true);
    	try{
    		serviceMonitorApplication.updateScheduleConf(conf);
    	}catch(Exception e){
        	dataMap.put("result", false);
    	}
    	return dataMap;
    }
    
}
