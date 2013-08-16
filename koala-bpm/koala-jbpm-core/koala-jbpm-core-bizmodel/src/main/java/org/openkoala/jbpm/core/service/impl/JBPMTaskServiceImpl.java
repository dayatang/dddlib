package org.openkoala.jbpm.core.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.drools.persistence.info.WorkItemInfo;
import org.openkoala.jbpm.core.KoalaAssignInfo;
import org.openkoala.jbpm.core.KoalaJbpmVariable;
import org.openkoala.jbpm.core.KoalaProcessInfo;
import org.openkoala.jbpm.core.ProcessInstanceExpandLog;
import org.openkoala.jbpm.core.service.JBPMTaskService;
import org.jbpm.persistence.processinstance.ProcessInstanceInfo;
import org.jbpm.process.audit.NodeInstanceLog;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.process.audit.VariableInstanceLog;
import org.jbpm.task.Content;
import org.jbpm.task.Status;
import org.jbpm.task.Task;
import org.jbpm.task.User;
import org.jbpm.task.query.TaskSummary;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dayatang.domain.QuerySettings;

@Named("jbpmTaskService")
@SuppressWarnings("unchecked")
@Transactional
public class JBPMTaskServiceImpl implements JBPMTaskService {

	@Inject
	@PersistenceContext(unitName="org.jbpm.persistence.local")
	EntityManager entityManager;
	
	public void removeTaskUser(long taskId,String user){
	    entityManager.createNativeQuery("delete from peopleassignments_potowners where TASK_ID='"+taskId+"' and ENTITY_ID='"+user+"' ").executeUpdate();
	}
	
	public void failedTask(Task task) {
		task.getTaskData().setStatus(Status.Failed);
		entityManager.merge(task);
	}
	
	public void repairTask(Task task){
		task.getTaskData().setStatus(Status.Reserved);
		entityManager.merge(task);
	}
	
	public ProcessInstanceExpandLog getInstanceExpandLog(long instanceLogId){
		final Query instanceExpandLogQuery = entityManager.createQuery("select log from ProcessInstanceExpandLog log where log.instanceLogId = :instanceLogId");
		instanceExpandLogQuery.setParameter("instanceLogId", instanceLogId);
		List<ProcessInstanceExpandLog> results = null;
		results = instanceExpandLogQuery.getResultList();
		if(results==null || results.size()==0){
			return null;
		}
		else{
			return (ProcessInstanceExpandLog)results.get(0);
		}
		
	}

	public List<TaskSummary> getErrorTasks() {
        final Query tasksAssignedAsPotentialOwner = entityManager.createNamedQuery("GetErrorTasks");
        tasksAssignedAsPotentialOwner.setParameter("language", "en-UK");
        return (List<TaskSummary>) tasksAssignedAsPotentialOwner.getResultList();
	}
	
	public List<WorkItemInfo> getWorkItemInfo(long processInstanceId){
		final Query workItemQuery = entityManager.createQuery("select w from WorkItemInfo w where w.processInstanceId = :processInstanceId");
		workItemQuery.setParameter("processInstanceId", processInstanceId);
		return (List<WorkItemInfo>)workItemQuery.getResultList();
	}
	
	public void exitedTask(long processInstanceId){
		final Query workItemQuery = entityManager.createQuery("select w from Task w where w.taskData.processInstanceId = :processInstanceId");
		workItemQuery.setParameter("processInstanceId", processInstanceId);
		List<Task> tasks = (List<Task>)workItemQuery.getResultList();
		for(Task task:tasks){
			if(task.getTaskData().getStatus().equals(Status.Failed) || task.getTaskData().getStatus().equals(Status.InProgress) || task.getTaskData().getStatus().equals(Status.Reserved)){
				task.getTaskData().setStatus(Status.Exited);
				entityManager.merge(task);
			}
		}
	}
	
	public List<KoalaJbpmVariable> queryJbpmVariable(){
		List<KoalaJbpmVariable> varsiableList = entityManager.createQuery("select k from KoalaJbpmVariable k").getResultList();
		return varsiableList;
	}

	public List<KoalaProcessInfo> findActiveProcess() {
		String sql = "from KoalaProcessInfo k where k.isActive is true";
		return entityManager.createQuery(sql).getResultList();
	}
	
	public List<KoalaAssignInfo> queryKoalaAssignInfo(String user,Date nowDate){
		final Query koalaProcessInfoQuery = entityManager.createQuery("select k from KoalaAssignInfo k where k.assignerTo = ? and k.beginTime<=? and ? <=k.endTime");
		koalaProcessInfoQuery.setParameter(1, user);
		koalaProcessInfoQuery.setParameter(2, nowDate);
		koalaProcessInfoQuery.setParameter(3, nowDate);
		return (List<KoalaAssignInfo>)koalaProcessInfoQuery.getResultList();
	}
	
	public KoalaProcessInfo getKoalaProcessInfo(Map<String,Object> params) {
		QuerySettings<KoalaProcessInfo> qSetting = QuerySettings.create(KoalaProcessInfo.class);
		Set<Map.Entry<String, Object>> entrySet = params.entrySet();
		for(Map.Entry<String, Object> entry:entrySet){
			qSetting.eq(entry.getKey(),entry.getValue());
		}
		return KoalaProcessInfo.findKoalaProcessInfo(qSetting);
	}
	
	public void addProcessInfo(KoalaProcessInfo info){
		entityManager.persist(info);
		
	}
	
	public List<KoalaProcessInfo> getDBResource() {
		final Query processQuery = entityManager.createQuery("select w from KoalaProcessInfo w");
		return processQuery.getResultList();
	}
	
	public void removeAssignUser(User user){
		entityManager.remove(entityManager.merge(user));
	}
	public void removeWorkItemInfo(WorkItemInfo info){
		entityManager.remove(info);
	}
	
	public void saveWorkItem(WorkItemInfo info){
		entityManager.persist(info);
	}
	
	public ProcessInstanceInfo getProcessInstanceInfo(long processInstanceId){
		return entityManager.find(ProcessInstanceInfo.class, processInstanceId);
	}

	public void updateProcessInstanceInfo(ProcessInstanceInfo info){
		entityManager.merge(info);
	}
	
	public Content getContent(long contentId){
		return entityManager.find(Content.class, contentId);
	}
	
	 @SuppressWarnings("unchecked")
	    public List<ProcessInstanceLog> findProcessInstances() {

	        List<ProcessInstanceLog> result = entityManager.createQuery("FROM ProcessInstanceLog").getResultList();

	        return result;
	    }

	    @SuppressWarnings("unchecked")
	    public  List<ProcessInstanceLog> findProcessInstances(String processId) {

	        List<ProcessInstanceLog> result = entityManager
	            .createQuery("FROM ProcessInstanceLog p WHERE p.processId like :processId")
	                .setParameter("processId", processId+"@%")
	                .getResultList();

	        return result;
	    }

	    @SuppressWarnings("unchecked")
	    public  List<ProcessInstanceLog> findActiveProcessInstances(String processId) {
	
	        List<ProcessInstanceLog> result = entityManager
	            .createQuery("FROM ProcessInstanceLog p WHERE p.processId = :processId AND p.end is null")
	                .setParameter("processId", processId).getResultList();

	        return result;
	    }

	    public  ProcessInstanceLog findProcessInstance(long processInstanceId) {

	        try {
	        	return (ProcessInstanceLog) entityManager
	            .createQuery("FROM ProcessInstanceLog p WHERE p.processInstanceId = :processInstanceId")
	                .setParameter("processInstanceId", processInstanceId).getSingleResult();
	        } catch (NoResultException e) {
	        	return null;
	        }
	    }
	    
	    @SuppressWarnings("unchecked")
	    public  List<NodeInstanceLog> findNodeInstances(long processInstanceId) {

	        List<NodeInstanceLog> result =entityManager
	            .createQuery("FROM NodeInstanceLog n WHERE n.processInstanceId = :processInstanceId ORDER BY date,id")
	                .setParameter("processInstanceId", processInstanceId).getResultList();

	        return result;
	    }

	    @SuppressWarnings("unchecked")
	    public  List<NodeInstanceLog> findNodeInstances(long processInstanceId, String nodeId) {

	        List<NodeInstanceLog> result =entityManager
	            .createQuery("FROM NodeInstanceLog n WHERE n.processInstanceId = :processInstanceId AND n.nodeId = :nodeId ORDER BY date,id")
	                .setParameter("processInstanceId", processInstanceId)
	                .setParameter("nodeId", nodeId).getResultList();

	        return result;
	    }

	    @SuppressWarnings("unchecked")
	    public  List<VariableInstanceLog> findVariableInstances(long processInstanceId) {

	        List<VariableInstanceLog> result =entityManager
	            .createQuery("FROM VariableInstanceLog v WHERE v.processInstanceId = :processInstanceId ORDER BY date")
	                .setParameter("processInstanceId", processInstanceId).getResultList();

	        return result;
	    }

	    @SuppressWarnings("unchecked")
	    public  List<VariableInstanceLog> findVariableInstances(long processInstanceId, String variableId) {

	        List<VariableInstanceLog> result = entityManager
	            .createQuery("FROM VariableInstanceLog v WHERE v.processInstanceId = :processInstanceId AND v.variableId = :variableId ORDER BY date")
	                .setParameter("processInstanceId", processInstanceId)
	                .setParameter("variableId", variableId).getResultList();
	        return result;
	    }
}
