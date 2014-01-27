package org.openkoala.jbpm.application;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.drools.runtime.process.ProcessInstance;
import org.jbpm.process.audit.JPAProcessInstanceDbLog;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.process.audit.VariableInstanceLog;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;
import org.jbpm.task.Content;
import org.jbpm.task.Task;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.ContentData;

/**
 * JBPM 5.4 原有的API功能封装
 * @author lingen
 *
 */
public interface KoalaBPMApiApplication {

	public ProcessInstance startProcess(String processName,
			Map<String, Object> params);
	
	public List<TaskSummary> findTaskSummary(String user);

	public List<TaskSummary> findTaskSummaryByGroup(String user,
			List<String> groups);

	public Collection<org.drools.definition.process.Process> queryProcesses();

	public org.drools.definition.process.Process getProcess(String processId) ;

	public ProcessInstanceLog getProcessInstanceLog(long processId);

	public List<ProcessInstanceLog> findActiveProcessInstances(
			String processIdActual);

	public List<ProcessInstanceLog> findProcessInstances(String processIdActual);

	public List<VariableInstanceLog> findVariableInstances(long processId);

	public List<VariableInstanceLog> findVariableInstances(long processId,
			String variableId);

	public ProcessInstance getProcessInstance(long processInstanceId);

	public void abortProcessInstance(long processInstanceId);

	public void delegate(long taskId, String userId, String targetUserId);

	public Task getTask(long taskId);
	
	public Content getContent(long contentId);

	public void startTask(long taskId, String userId);

	public void completeTask(long taskId, String userId, ContentData outputData);
	
	public void clearTaskByProcessInstanceId(long processInstanceId);
	
}
