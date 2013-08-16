package org.openkoala.jbpm.core.jbpm.action.jbpm;

import java.util.List;
import javax.inject.Inject;
import org.openkoala.jbpm.application.JBPMApplication;
import org.openkoala.jbpm.application.vo.HistoryLogVo;
import org.openkoala.jbpm.application.vo.KoalaProcessInfoVO;
import org.openkoala.jbpm.application.vo.ProcessInstanceVO;
import org.openkoala.jbpm.application.vo.ProcessVO;
import org.openkoala.jbpm.core.jbpm.action.BaseAction;

import com.dayatang.querychannel.support.Page;

public class JbpmProcessAction extends BaseAction{

	private static final long serialVersionUID = 6104107004303185898L;
	
	private String processName;
	
	private List<ProcessVO> processes;
	
	private List<String> packages;
	
	private Page<ProcessInstanceVO> processInstanceVos;
	
	@Inject
	private JBPMApplication jbpmApplication;
	
	private int page;
	
	private int pagesize;
	
	private List<HistoryLogVo> logs;
	
	private String processInstanceId;
	
	private String processId;
	
	private String versionNum;
	
	private String packageName;
	
	private List<KoalaProcessInfoVO> processInfos;
	
	public String processDetail(){
		logs = jbpmApplication.queryHistoryLog(Long.parseLong(processInstanceId));
		return "METHOD";
	}
	
	public String queryProcessVersion(){
		processInfos = jbpmApplication.getProcessVersionByProcessId(processId);
		dataMap.put("Rows", processInfos);
		dataMap.put("start", 0);
		dataMap.put("limit", 500);
		dataMap.put("Total", processInfos.size());
		return "JSON";
	}
	/**
	 * 分页查询所有正在运行的流程实例
	 * @return
	 */
	public String queryRunningProcess(){
		Page<ProcessInstanceVO> all = this.jbpmApplication.queryRunningProcessInstances(processId,versionNum,page, pagesize);
		dataMap.put("Rows", all.getResult());
		dataMap.put("start", page * pagesize - pagesize);
		dataMap.put("limit", pagesize);
		dataMap.put("Total", all.getTotalCount());
		return "JSON";
	}
	
	/**
	 * 分页查询所有已经完结的流程实例
	 * @return
	 */
	public String queryCompleteProcess(){ 
		Page<ProcessInstanceVO> all = this.jbpmApplication.queryCompleteProcessInstances(processId,versionNum,page, pagesize);
		dataMap.put("Rows", all.getResult());
		dataMap.put("start", page * pagesize - pagesize);
		dataMap.put("limit", pagesize);
		dataMap.put("Total", all.getTotalCount());
		return "JSON";
	}
	
	/**
	 * 查询到当前流程引擎中部署的包
	 * @return
	 */
	public String findPackages(){
		packages = jbpmApplication.getPakcages();
		dataMap.put("packages", packages);
		return "JSON";
	}
	
	/**
	 * 查询所有激活的流程版本
	 * @return
	 */
	public String queryActiveProcess(){
		//processes = jbpmApplication.getProcessByPackage(packageName)
		dataMap.put("Rows", processes);
		dataMap.put("start", 0);
		dataMap.put("limit", 1);
		dataMap.put("Total", processes.size());
		return "JSON";
	}
	
	
	public String queryProcessByPackage(){
		List<String> processes = jbpmApplication.getProcessByPackage(packageName);
		dataMap.put("processes", processes);
		return "JSON";
	}
	
	/**
	 * 查询某一流程的所有版本
	 * @return
	 */
	public String queryProcessByProcessName(){
		processes = jbpmApplication.getProcessesByProcessName(processName);
		dataMap.put("Rows", processes);
		dataMap.put("start", 0);
		dataMap.put("limit", 1);
		dataMap.put("Total", processes.size());
		return "JSON";
	}
	
	public List<String> getPackages() {
		return packages;
	}
	public void setPackages(List<String> packages) {
		this.packages = packages;
	}
	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public List<ProcessVO> getProcesses() {
		return processes;
	}

	public void setProcesses(List<ProcessVO> processes) {
		this.processes = processes;
	}

	public Page<ProcessInstanceVO> getProcessInstanceVos() {
		return processInstanceVos;
	}

	public void setProcessInstanceVos(Page<ProcessInstanceVO> processInstanceVos) {
		this.processInstanceVos = processInstanceVos;
	}

	public List<HistoryLogVo> getLogs() {
		return logs;
	}

	public void setLogs(List<HistoryLogVo> logs) {
		this.logs = logs;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public List<KoalaProcessInfoVO> getProcessInfos() {
		return processInfos;
	}

	public void setProcessInfos(List<KoalaProcessInfoVO> processInfos) {
		this.processInfos = processInfos;
	}

	public String getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(String versionNum) {
		this.versionNum = versionNum;
	}
	
	
	
}
