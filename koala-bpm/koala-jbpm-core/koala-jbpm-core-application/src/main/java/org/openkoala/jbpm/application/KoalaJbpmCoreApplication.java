package org.openkoala.jbpm.application;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.drools.runtime.process.ProcessInstance;
import org.openkoala.jbpm.application.vo.KoalaProcessInfoVO;
import org.jbpm.task.Task;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.ContentData;

public interface KoalaJbpmCoreApplication {

	public List<TaskSummary> findTaskSummary(String user);
	
	public List<TaskSummary> findTaskSummaryByGroup(String user,List<String> groupId);
	
	public Collection<org.drools.definition.process.Process> queryProcesses();
	
	public org.drools.definition.process.Process getProcess(String processId);
	
	
	public ProcessInstance getProcessInstance(long processInstanceId);
	
	public void abortProcessInstance(long processInstanceId);
	
	public void delegate(long taskId,String userId,String targetUserId);
	
	public Task getTask(long taskId);
	
	public void startTask(long taskId, String userId);
	
	public void completeTask(long taskId, String userId, ContentData outputData);
	
	public Map<String,Object> getGlobalVariable();
	
	public Map<String,Object> getPackageVariable(String packageName);
	
	public Map<String,Object> getProcessVariable(String processId);
	
	public ProcessInstance startProcess(String processName,Map<String, Object> params);
	

	public void initActiveProcess();

	public String getActiveProcess(String process);

	public void addProcessToCenter(KoalaProcessInfoVO processInfo,boolean isActive) throws Exception;

	public void initVariables();
	
	public void initialize() throws Exception;
	
}
