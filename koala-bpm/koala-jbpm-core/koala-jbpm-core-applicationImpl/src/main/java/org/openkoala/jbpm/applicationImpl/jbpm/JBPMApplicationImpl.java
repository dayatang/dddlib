package org.openkoala.jbpm.applicationImpl.jbpm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jws.WebMethod;

import org.drools.definition.process.Node;
import org.drools.persistence.info.WorkItemInfo;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.NodeInstance;
import org.drools.runtime.process.ProcessInstance;
import org.openkoala.exception.extend.KoalaException;
import org.openkoala.jbpm.application.JBPMApplication;
import org.openkoala.jbpm.application.KoalaJbpmCoreApplication;
import org.openkoala.jbpm.application.core.JoinAssignApplication;
import org.openkoala.jbpm.application.vo.HistoryLogVo;
import org.openkoala.jbpm.application.vo.JBPMNode;
import org.openkoala.jbpm.application.vo.JoinAssignVO;
import org.openkoala.jbpm.application.vo.KoalaProcessInfoVO;
import org.openkoala.jbpm.application.vo.MessageLogVO;
import org.openkoala.jbpm.application.vo.ProcessInstanceVO;
import org.openkoala.jbpm.application.vo.ProcessVO;
import org.openkoala.jbpm.application.vo.TaskVO;
import org.openkoala.jbpm.applicationImpl.util.JbpmSupport;
import org.openkoala.jbpm.core.HistoryLog;
import org.openkoala.jbpm.core.JoinAssign;
import org.openkoala.jbpm.core.KoalaAssignDetail;
import org.openkoala.jbpm.core.KoalaAssignInfo;
import org.openkoala.jbpm.core.KoalaJbpmVariable;
import org.openkoala.jbpm.core.KoalaProcessInfo;
import org.openkoala.jbpm.core.MessageLog;
import org.openkoala.jbpm.core.ProcessInstanceExpandLog;
import org.openkoala.jbpm.core.jbpm.applicationImpl.util.WSUtil;
import org.openkoala.jbpm.core.service.JBPMTaskService;
import org.openkoala.jbpm.infra.ImageUtil;
import org.openkoala.jbpm.infra.XmlParseUtil;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.ruleflow.core.RuleFlowProcess;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;
import org.jbpm.task.AccessType;
import org.jbpm.task.Content;
import org.jbpm.task.Status;
import org.jbpm.task.Task;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.ContentData;
import org.jbpm.workflow.core.node.HumanTaskNode;
import org.jbpm.workflow.instance.node.HumanTaskNodeInstance;
import org.mvel2.MVEL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

/**
 * 流程的实现
 * 
 * @author lingen
 * 
 */
@Named("jbpmApplication")
@Transactional(value="transactionManager")
@SuppressWarnings({ "unchecked", "unused" })
@org.apache.cxf.interceptor.InInterceptors (interceptors = {"org.apache.cxf.transport.common.gzip.GZIPInInterceptor" })
public class JBPMApplicationImpl implements JBPMApplication {

	private static final Logger logger = LoggerFactory
			.getLogger(JBPMApplicationImpl.class);
	
	@Value("${koala.userGroupWsUrl}")
	private String userGroupWsUrl;
	
	@Value("${koala.userGroupMethod}")
	private String userGroupMethod; 

	@Inject
	private QueryChannelService queryChannel;
	
	@Inject
	private JoinAssignApplication joinAssignApplication;

	@Inject
	private JBPMTaskService jbpmTaskService;

	private JbpmSupport jbpmSupport;

	public JbpmSupport getJbpmSupport() {
		if (jbpmSupport == null) {
			jbpmSupport = InstanceFactory.getInstance(JbpmSupport.class);
		}
		return jbpmSupport;
	}


	/**
	 * 返回指定流程名称的图片
	 */
	public byte[] getProcessImage(String processId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("processId", processId);
		KoalaProcessInfo processInfo = jbpmTaskService
				.getKoalaProcessInfo(params);
		return ImageUtil.getProcessPictureByte(processId, processInfo.getPng(),
				new ArrayList<Integer>());
	}

	/**
	 */
	public byte[] getPorcessImageStream(long instanceId) {
		RuleFlowProcessInstance in = (RuleFlowProcessInstance) getJbpmSupport()
				.getProcessInstance(instanceId);
		List<Integer> nodes = new ArrayList<Integer>();
		String processId = null;
		if (in != null) {
			Collection<org.drools.runtime.process.NodeInstance> actives = in
					.getNodeInstances();
			processId = in.getProcessId();
			for (NodeInstance active : actives) {
				nodes.add((Integer) active.getNode().getMetaData().get("x"));
				nodes.add((Integer) active.getNode().getMetaData().get("y"));
			}
		} else {
			ProcessInstanceLog log =jbpmTaskService.findProcessInstance(
					instanceId);
			processId = log.getProcessId();
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("processId", processId);
		KoalaProcessInfo processInfo = jbpmTaskService
				.getKoalaProcessInfo(params);
		return ImageUtil.getProcessPictureByte(processId, processInfo.getPng(),
				nodes);
	}
	
	
	public List<TaskVO> queryTodoList(String user) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<TaskVO> todos = new ArrayList<TaskVO>();	
		
		List<TaskSummary> tasks = null;
		if(!"".equals(userGroupWsUrl) && !"".equals(userGroupMethod)){
			List<String> groups = null;
			try {
				groups = WSUtil.invokeUserGroup(user,userGroupWsUrl,userGroupMethod);
			} catch (Exception e) {
				e.printStackTrace();
			}
			tasks = getJbpmSupport().findTaskSummaryByGroup(user, groups);
		}else{
			tasks = getJbpmSupport().findTaskSummary(user);
		}
		for (TaskSummary task : tasks) {
			long processId = task.getProcessInstanceId();
			RuleFlowProcessInstance in = null;
			try {
				ProcessInstance instance = getJbpmSupport().getProcessInstance(
						processId);
				if (instance == null)
					continue;
				in = (RuleFlowProcessInstance) instance;
			} catch (Exception e) {
				continue;
			}
			ProcessInstanceLog log = jbpmTaskService
					.findProcessInstance(processId);
			TaskVO todo = new TaskVO();
			Map<String, Object> processParams = in.getVariables();
			String processData = XmlParseUtil.paramsToXml(processParams);
			todo.setActualOwner(task.getActualOwner().getId());
			todo.setActualName(task.getName());
			todo.setProcessInstanceId(processId);
			todo.setProcessId(in.getProcessId());
			try {
				todo.setProcessName(in.getProcessName());
			} catch (Exception e) {
				todo.setProcessName(in.getProcessId());
			}
			todo.setTaskId(task.getId());
			todo.setProcessData(processData);
			//todo.setCreateDate(df.format(log.getStart()));
			if(in.getVariable("_process_creater")!=null){
				todo.setCreater((String)in.getVariable("_process_creater"));
			}
			todos.add(todo);
		}
		
		// 以下是查询委托待办
		List<KoalaAssignInfo> koalaAssignInfoList = jbpmTaskService
				.queryKoalaAssignInfo(user, new Date());
		if (koalaAssignInfoList != null && !koalaAssignInfoList.isEmpty()) {
			Set<String> agetnUserList = new HashSet<String>();
			//委托待办任务
			List<TaskSummary> agentTasks = new ArrayList<TaskSummary>();
			for (KoalaAssignInfo koalaAssignInfo : koalaAssignInfoList) {
				agetnUserList.add(koalaAssignInfo.getAssigner());
				//如果koalaAssignInfo中指定的流程为空，则表明所有流程都委托，否指定某个流程进行待办
				if(koalaAssignInfo.getJbpmNames()==null || koalaAssignInfo.getJbpmNames().size()==0){
					
					tasks = null;
					if(!"".equals(userGroupWsUrl) && !"".equals(userGroupMethod)){
						List<String> groups = null;
						try {
							groups = WSUtil.invokeUserGroup(user,userGroupWsUrl,userGroupMethod);
						} catch (Exception e) {
							e.printStackTrace();
						}
						tasks = getJbpmSupport().findTaskSummaryByGroup(user, groups);
					}else{
						tasks = getJbpmSupport().findTaskSummary(user);
					}
					
					agentTasks.addAll(tasks);
				}else{
					//如果指定了迁移的流程，则只委托指定的流程
					List<TaskSummary> assignTasks = null;
					if(!"".equals(userGroupWsUrl) && !"".equals(userGroupMethod)){
						List<String> groups = null;
						try {
							groups = WSUtil.invokeUserGroup(user,userGroupWsUrl,userGroupMethod);
						} catch (Exception e) {
							e.printStackTrace();
						}
						assignTasks = getJbpmSupport().findTaskSummaryByGroup(user, groups);
					}else{
						assignTasks = getJbpmSupport().findTaskSummary(user);
					}
					
					List<String> allowProcess = new ArrayList<String>();
					for(KoalaAssignDetail detail:koalaAssignInfo.getJbpmNames()){
						allowProcess.add(detail.getProcessId());
					}
					for(TaskSummary task:assignTasks){
						String processId = task.getProcessId();
						if(processId.contains("@"))processId = processId.substring(0,processId.indexOf("@"));
						if(allowProcess.contains(processId)){
							agentTasks.add(task);
						}
					}
				}
			}

			for (TaskSummary task : agentTasks) {
				long processId = task.getProcessInstanceId();
				RuleFlowProcessInstance in = null;
				try {
					ProcessInstance instance = getJbpmSupport()
							.getProcessInstance(processId);
					if (instance == null)
						continue;
					in = (RuleFlowProcessInstance) instance;
					// WorkItem workItem = ((WorkItemManager)
					// in.getKnowledgeRuntime().getWorkItemManager()).getWorkItem(contentId);
					// Object obj = in.getNodeInstances(workItem.getId());
					// System.out.println(workItem.getParameters());
				} catch (Exception e) {
					continue;
				}
				ProcessInstanceLog log = jbpmTaskService.findProcessInstance(
						processId);
				TaskVO todo = new TaskVO();
				Map<String, Object> processParams = in.getVariables();
				String processData = XmlParseUtil.paramsToXml(processParams);
				todo.setActualOwner(task.getActualOwner().getId());
				todo.setActualName(task.getName());
				todo.setProcessInstanceId(processId);
				todo.setProcessId(in.getProcessId());
				try {
					todo.setProcessName(in.getProcessName());
				} catch (Exception e) {
					todo.setProcessName(in.getProcessId());
				}
				todo.setTaskId(task.getId());
				todo.setProcessData(processData);
				todo.setCreateDate(df.format(log.getStart()));
				todo.setAgents("是");
				// todo.setCreater((String)in.getVariable("_process_creater"));
				todos.add(todo);
			}
		}
		return todos;
	}

	/**
	 * 查询已办任务
	 */
	public List<TaskVO> queryDoenTask(String user) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		List<TaskVO> dones = new ArrayList<TaskVO>();
		List<HistoryLog> logs = queryChannel.queryResult(
				"select log from HistoryLog log where log.user = ?",
				new Object[] { user });
		List<Long> ids = new ArrayList<Long>();
		for (HistoryLog log : logs) {
			if (ids.contains(log.getProcessInstanceId()))
				continue;
			else {
				ids.add(log.getProcessInstanceId());
			}
			TaskVO todo = new TaskVO();
			long processId = log.getProcessInstanceId();
			ProcessInstanceLog processLog = null;
			try {
				processLog =jbpmTaskService.findProcessInstance(processId);
			} catch (Exception e) {
				continue;
			}
			todo.setProcessData(log.getProcessData());
			todo.setProcessInstanceId(processId);
			if(processLog!=null){
				todo.setProcessId(processLog.getProcessId());
				// TODO 存进去，关于流程名称
				todo.setProcessName(processLog.getProcessId());
				todo.setCreateDate(df.format(processLog.getStart()));
			}
			
			dones.add(todo);
		}
		return dones;
	}

	public long startProcess(String processName, String creater,String paramsString) {
		// 读取Global级的变量
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> globalMap = getJbpmSupport().getGlobalVariable();
		globalMap.put("KJ_USER", creater);
		Set<Map.Entry<String, Object>> keyEntrySet = globalMap.entrySet();
		for(Map.Entry<String, Object> entry:keyEntrySet){
			params.put(entry.getKey(), entry.getValue());
		}
		String activeProcessName = getJbpmSupport().getActiveProcess(processName);

		org.drools.definition.process.Process process = getJbpmSupport()
				.getProcess(activeProcessName);
		if (process == null) {
			throw new RuntimeException("不存在的流程，请检查");
		}
		Map<String, Object> packageVariaMap = getJbpmSupport().getPackageVariable(
				process.getPackageName());
		// 读取PACKAGE级的变量
		Set<Map.Entry<String, Object>> entrySet = packageVariaMap.entrySet();
		for(Map.Entry<String, Object> entry:entrySet){
			params.put(entry.getKey(), entry.getValue());
		}

		Map<String, Object> processVariableMap = getJbpmSupport()
				.getProcessVariable(processName);
		// 读取Process级的变
		Set<Map.Entry<String, Object>> processVariableMapEntrySet = processVariableMap.entrySet();
		for(Map.Entry<String, Object> entry:processVariableMapEntrySet){
			params.put(entry.getKey(), entry.getValue());
		}

		Map<String, Object> userParams = XmlParseUtil.xmlToPrams(paramsString);
		if (userParams != null)
			params.putAll(userParams);

		RuleFlowProcessInstance instance = (RuleFlowProcessInstance) getJbpmSupport()
				.startProcess(activeProcessName, params);

		return instance.getId();
	}

	public void delegate(long taskId, String userId, String targetUserId) {
		getJbpmSupport().delegate(taskId, userId, targetUserId);
		Task task = getJbpmSupport().getTask(taskId);
		HistoryLog log = new HistoryLog();
		log.setComment("当初任务由" + userId + "委托给" + targetUserId);
		log.setCreateDate(new Date());
		log.setNodeName("委托");
		log.setResult("当前任务被委托");
		log.setUser(userId);
		log.setProcessInstanceId(task.getTaskData().getProcessInstanceId());
		log.save();
	}

	public boolean completeTask(long processInstanceId, long taskId,
			String user, String params, String data) {
		// 更新流程级的参数
		Map<String, Object> proceeParams = XmlParseUtil.xmlToPrams(params);
		proceeParams.put("KJ_USER", user);
		RuleFlowProcessInstance in = (RuleFlowProcessInstance) getJbpmSupport()
				.getProcessInstance(processInstanceId);
		Set<Map.Entry<String, Object>> entrySet = proceeParams.entrySet();
		for(Map.Entry<String, Object> entry:entrySet){
			in.setVariable(entry.getKey(), entry.getValue());
		}
		Task task = getJbpmSupport().getTask(taskId);
		// 更新TASK参数
		Map<String, Object> dataParams = XmlParseUtil.xmlToPrams(data);
		ContentData contentData = null;
		if (data != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream outs = null;
			try {
				outs = new ObjectOutputStream(bos);
				outs.writeObject(dataParams);
				outs.close();
				contentData = new ContentData();
				contentData.setContent(bos.toByteArray());
				contentData.setAccessType(AccessType.Inline);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (outs != null)
					try {
						outs.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
		JoinAssignVO joginAssign = null;
		// 判断是否是会签流程
		long contentId = getJbpmSupport().getTask(task.getId()).getTaskData()
				.getDocumentContentId();
		Content content = jbpmTaskService.getContent(contentId);
		if(content!=null){
		byte[] conByte = content.getContent();
		ByteArrayInputStream bis = new ByteArrayInputStream(conByte);
		try {
			ObjectInputStream ois = new ObjectInputStream(bis);
			Object obj = ois.readObject();
			Map<String, Object> map = (Map<String, Object>) obj;
			if (map.containsKey("KJ_ASSIGN")) {
				if(in.getVariables().containsKey("KJ_ASSIGN_VAL"+taskId)){
					joginAssign = (JoinAssignVO) in.getVariable("KJ_ASSIGN_VAL"+taskId);
				}else{
					String name = (String) map.get("KJ_ASSIGN");
					joginAssign = joinAssignApplication.getJoinAssignByName(name);
					in.setVariable("KJ_ASSIGN_VAL"+taskId, joginAssign);
				}
				
				if (joginAssign.getAllCount() == 0) {
					joginAssign.setAllCount(task.getPeopleAssignments()
							.getPotentialOwners().size());
				}
				// 获取当前用户的决择
				String choice = String.valueOf(in.getVariable(joginAssign
						.getKeyChoice()));
				joginAssign.addChoice(choice);
				HistoryLog log = new HistoryLog();
				log.setComment(user + ":的选择的是:" + choice);
				log.setCreateDate(new Date());
				log.setNodeName("会签:");
				log.setResult("节点会签");
				log.setUser(user);
				log.setProcessInstanceId(task.getTaskData()
						.getProcessInstanceId());
				log.save();
				// 如果会签成功，流转会签
				String success = joginAssign.queryIsSuccess();
				System.out.println();
				if (success!=null) {
					in.setVariable(joginAssign.getKeyChoice(),success);
					task.getTaskData().setStatus(Status.Reserved);
					completeTask(task, contentData);
				}
				// 如果任务仍然存在，但所有人已经完成投票了，则此次会签失败
				if (joginAssign.queryIsFinished()) {
					// 会签失败，将关键值置为0，完成此任务
					in.setVariable(joginAssign.getKeyChoice(), "FAIL");
					task.getTaskData().setStatus(Status.Reserved);
					completeTask(task, contentData);
				}
				this.jbpmTaskService.removeTaskUser(taskId, user);
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		}
		if (joginAssign == null) {
			completeTask(task, contentData);
		}
		return true;
	}

	private boolean completeTask(Task task, ContentData contentData) {
		try {
			getJbpmSupport().startTask(task.getId(),
					task.getTaskData().getActualOwner().getId());
			getJbpmSupport().completeTask(task.getId(),
					task.getTaskData().getActualOwner().getId(), contentData);
			return true;
		} catch (RuntimeException e) {
			e.printStackTrace();
			logger.error(task.getId() + ":任务执行出错");
			jbpmTaskService.failedTask(task);
			return false;
		}
	}

	public List<HistoryLogVo> queryHistoryLog(long processId) {
		List<HistoryLog> lists = queryChannel
				.queryResult(
						"select log from HistoryLog log where log.processInstanceId = ? order by log.createDate",
						new Object[] { processId });
		List<HistoryLogVo> logs = new ArrayList<HistoryLogVo>();

		for (HistoryLog vo : lists) {
			HistoryLogVo log = new HistoryLogVo();
			BeanUtils.copyProperties(vo, log);
			logs.add(log);
		}

		return logs;
	}

	public List<MessageLogVO> getMessages(String user) {
		List<MessageLog> lists = queryChannel.queryResult(
				"select log from MessageLog log where log.user = ?",
				new Object[] { user });
		List<MessageLogVO> messages = new ArrayList<MessageLogVO>();
		for (MessageLog log : lists) {
			MessageLogVO vo = new MessageLogVO();
			BeanUtils.copyProperties(log, vo);
			messages.add(vo);
		}
		return messages;
	}
	
	/**
	 * 取回功能，如果下一步的人员未进行任何操作，前一步的用户可以主动取回这个任务，重新决策
	 * @param TaskId
	 * @param userId
	 */
	public void fetchBack(long processInstanceId,long taskId,String userId){
		//查询流程的上个节点处理人，将流程回复到此节点
		HistoryLog historyLog = HistoryLog.queryLastActivedNodeId(processInstanceId);
		assignToNode(processInstanceId,historyLog.getNodeId());
	}
	
	/**
	 * 退回功能，当前节点用户可进行退回
	 * @param processInstanceId
	 * @param userId
	 * @param userId
	 */
	public void roolBack(long processInstanceId,long taskId,String userId){
		RuleFlowProcessInstance in = (RuleFlowProcessInstance) getJbpmSupport()
				.getProcessInstance(processInstanceId);
		List<String> actives = in.getActiveNodeIds();
		if(actives.size()>1){
			throw new RuntimeException("多个任务情况下不支持回退");
		}
		HistoryLog historyLog = HistoryLog.queryLastActivedNodeId(processInstanceId);
		if(historyLog==null || historyLog.getNodeId()==0){
			throw new RuntimeException("没有上一个人工处理节点，不能回退");
		}
		assignToNode(processInstanceId,historyLog.getNodeId());
	}

	/**
	 * 将指定的流程转到指定的节点上去
	 * 
	 * @param processInstanceId
	 * @param nodeId
	 */
	public void assignToNode(long processInstanceId, long nodeId) {
		RuleFlowProcessInstance in = (RuleFlowProcessInstance) getJbpmSupport()
				.getProcessInstance(processInstanceId);
		HumanTaskNode node = (HumanTaskNode) in.getNodeContainer().getNode(
				nodeId);
		List<WorkItemInfo> workItemInfos = jbpmTaskService
				.getWorkItemInfo(processInstanceId);
		HumanTaskNodeInstance human = (HumanTaskNodeInstance) in
				.getNodeInstance(node);
		// 如果流程当前节点与要激活的节点一样，则不转移
		boolean isSame = true;

		Collection<org.drools.runtime.process.NodeInstance> instances = in
				.getNodeInstances();
		for (org.drools.runtime.process.NodeInstance nodeInstance : instances) {
			if (nodeInstance.getNodeId() != human.getNodeId()) {
				isSame = false;
			}
		}
		in.setVariable("REMOVE", true);
		for (org.drools.runtime.process.NodeInstance nodeInstance : instances) {
			org.jbpm.workflow.instance.NodeInstance removeNode = in
					.getNodeInstance(nodeInstance.getId());
			if (removeNode != null) {
				in.removeNodeInstance(removeNode);
				in.removeEventListener("workItemCompleted",
						(HumanTaskNodeInstance) removeNode, false);
			}
		}
		jbpmTaskService.exitedTask(processInstanceId);
		human.internalTrigger(null, "");
		for (WorkItemInfo workItemInfo : workItemInfos) {
			jbpmTaskService.removeWorkItemInfo(workItemInfo);
		}
		if (isSame){
			return;
		}
			
		HistoryLog log = new HistoryLog();
		log.setComment("转移到节点:" + human.getNodeName());
		log.setCreateDate(new Date());
		log.setNodeName("人工转移");
		log.setResult("当前流程任务被管理员变更");
		log.setUser("Admin");
		log.setProcessInstanceId(processInstanceId);
		log.save();
	}

	private Map<String, Object> parseVar(Map<String, Object> params,
			Map<String, Object> values) {
		Set<String> keys = params.keySet();
		Map<String, Object> returnVal = new HashMap<String, Object>();
		for (String key : keys) {
			String express = (String) params.get(key);
			if (express.contains("#{") && express.contains("}")) {
				express = express.substring(express.indexOf("#{") + 2,
						express.lastIndexOf("}"));
				Object value = MVEL.eval(express, values);
				returnVal.put(key, value);
			} else {
				returnVal.put(key, params.get(key));
			}
		}
		return returnVal;
	}

	public List<JBPMNode> getProcessNodes(String processId) {
		String processIdActual = getJbpmSupport().getActiveProcess(processId);
		List<JBPMNode> jbpmNodes = new ArrayList<JBPMNode>();
		org.drools.definition.process.Process process = getJbpmSupport()
				.getProcess(processIdActual);
		RuleFlowProcess rule = (RuleFlowProcess) process;
		Node[] nodes = rule.getNodes();
		for (Node node : nodes) {
			if (node instanceof HumanTaskNode) {
				JBPMNode jbpmNode = new JBPMNode(node.getId(), node.getName());
				jbpmNodes.add(jbpmNode);
			}
		}
		return jbpmNodes;
	} 

	public List<JBPMNode> getProcessNodesFromPorcessInstnaceId(
			long processInstanceId) {
		List<JBPMNode> jbpmNodes = new ArrayList<JBPMNode>();
		RuleFlowProcessInstance in = (RuleFlowProcessInstance) getJbpmSupport()
				.getProcessInstance(processInstanceId);
		Node[] nodes = in.getNodeContainer().getNodes();
		for (Node node : nodes) {
			if (node instanceof HumanTaskNode) {
				JBPMNode jbpmNode = new JBPMNode(node.getId(), node.getName());
				HumanTaskNode human = (HumanTaskNode) node;
				jbpmNodes.add(jbpmNode);
			}
		}
		return jbpmNodes;
	}

	public void repairTask(long taskId) {
		Task task = getJbpmSupport().getTask(taskId);
		jbpmTaskService.repairTask(task);
	}

	public List<TaskVO> queryErrorList() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		List<TaskSummary> tasks = jbpmTaskService.getErrorTasks();
		List<TaskVO> todos = new ArrayList<TaskVO>();

		Collection<org.drools.definition.process.Process> processes = getJbpmSupport()
				.queryProcesses();
		List<String> processList = new ArrayList<String>();
		for (org.drools.definition.process.Process process : processes) {
			processList.add(process.getId());
		}
		for (TaskSummary task : tasks) {
			long processId = task.getProcessInstanceId();
			RuleFlowProcessInstance in = null;
			try {
				if (!processList.contains(task.getProcessId()))
					continue;
				ProcessInstance instance = getJbpmSupport().getProcessInstance(
						processId);
				if (instance == null)
					continue;
				in = (RuleFlowProcessInstance) instance;
			} catch (Exception e) {
				continue;
			}
			ProcessInstanceLog log = jbpmTaskService.findProcessInstance(
					processId);
			TaskVO todo = new TaskVO();
			Map<String, Object> processParams = in.getVariables();
			String processData = XmlParseUtil.paramsToXml(processParams);
			todo.setActualOwner(task.getActualOwner().getId());
			todo.setActualName(task.getName());
			todo.setProcessInstanceId(processId);
			todo.setProcessId(in.getProcessId());
			try {
				todo.setProcessName(in.getProcessName());
			} catch (Exception e) {
				todo.setProcessName(in.getProcessId());
			}
			todo.setTaskId(task.getId());
			todo.setProcessData(processData);
			todo.setCreateDate(df.format(log.getStart()));
			// todo.setCreater((String)in.getVariable("_process_creater"));
			todos.add(todo);
		}
		return todos;
	}

	public List<ProcessInstanceVO> queryAllActiveProcess(String processId) {
		String processIdActual = getJbpmSupport().getActiveProcess(processId);
		List<ProcessInstanceVO> instances = new ArrayList<ProcessInstanceVO>();
		List<ProcessInstanceLog> logs = jbpmTaskService.findActiveProcessInstances(processIdActual);
		for (ProcessInstanceLog log : logs) {
			RuleFlowProcessInstance in = (RuleFlowProcessInstance) getJbpmSupport()
					.getProcessInstance(log.getProcessInstanceId());
			instances.add(getProcessInstanceVO(in, log));
		}
		return instances;
	}

    /**
     * 查询一个流程下所部署的所有版本	
     */
	@WebMethod(exclude = true)
	public List<KoalaProcessInfoVO> getProcessVersionByProcessId(String processId){
		List<KoalaProcessInfo> infos = KoalaProcessInfo.getProcessVersionByProcessId(processId);
		List<KoalaProcessInfoVO> vos = new ArrayList<KoalaProcessInfoVO>();
		 for(KoalaProcessInfo info:infos){
			 KoalaProcessInfoVO vo = new KoalaProcessInfoVO();
			 BeanUtils.copyProperties(info, vo,new String[]{"data"});
			 vos.add(vo);
		 }
		return vos;
	}
	/**
	 * 分布查询正在运行的流程
	 */
	@WebMethod(exclude = true)
	public Page<ProcessInstanceVO> queryRunningProcessInstances(String processId,String versionNum,long firstRow,int pageSize) {
		String hql  ="select new org.openkoala.jbpm.application.vo.ProcessInstanceVO(log.processId,log.processInstanceId,exlog.processName,log.start,log.end,exlog.processData) from ProcessInstanceLog log , ProcessInstanceExpandLog exlog where"
			+" exlog.instanceLogId = log.id and exlog.state = 1";
		if(processId!=null && !"".equals(processId)){
			if(versionNum!=null && !"".equals(versionNum)){
				hql = hql + " and log.processId = '"+(processId+"@"+versionNum)+"'";
			}else{
				hql = hql + " and log.processId like '"+processId+"@%'";
			}
		}
		Page<ProcessInstanceVO> logs =this.queryChannel.queryPagedResult(hql, new Object[]{}, firstRow, pageSize);
		return logs;
	}

	@WebMethod(exclude = true)
	public Page<ProcessInstanceVO> queryCompleteProcessInstances(String processId,String versionNum,long firstRow,
			int pageSize) {
		String hql  ="select new org.openkoala.jbpm.application.vo.ProcessInstanceVO(log.processId,log.processInstanceId,exlog.processName,log.start,log.end,exlog.processData) from ProcessInstanceLog log , ProcessInstanceExpandLog exlog where"
				+" exlog.instanceLogId = log.id and exlog.state = 2";
		
		if(processId!=null && !"".equals(processId)){
			if(versionNum!=null && !"".equals(versionNum)){
				hql = hql + " and log.processId = '"+(processId+"@"+versionNum)+"'";
			}else{
				hql = hql + " and log.processId like '"+processId+"@%'";
			}
		}
			Page<ProcessInstanceVO> logs =this.queryChannel.queryPagedResult(hql, new Object[]{}, firstRow, pageSize);
			return logs;
	}

	public ProcessInstanceVO getProcessInstance(long processId) {
		RuleFlowProcessInstance in = (RuleFlowProcessInstance) getJbpmSupport()
				.getProcessInstance(processId);
		ProcessInstanceLog log = jbpmTaskService
				.findProcessInstance(processId);
		return getProcessInstanceVO(in, log);
	}

	private ProcessInstanceVO getProcessInstanceVO(RuleFlowProcessInstance in,
			ProcessInstanceLog log) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		ProcessInstanceVO vo = new ProcessInstanceVO();
		vo.setCreateDate(df.format(log.getStart()));
		// vo.setCreater((String)in.getVariable("_process_creater"));
		// vo.setParentProcessInstanceId(log.getParentProcessInstanceId());
		String processVersionId = log.getProcessId();
		vo.setProcessId(processVersionId.substring(0,processVersionId.lastIndexOf("@")));
		vo.setVersionNum(Integer.parseInt(processVersionId.substring(processVersionId.lastIndexOf("@")+1)));
		vo.setProcessInstanceId(log.getProcessInstanceId());
		try {
			vo.setProcessName(in.getProcessName());
		} catch (Exception e) {
			vo.setProcessName(in.getProcessId());
		}
		vo.setStatus(in.getState());
		vo.setData(XmlParseUtil.paramsToXml(in.getVariables()));
		return vo;
	}

	public void addProcess(String packageName, String processName, int version,
			String data, byte[] png, boolean isActive) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("versionNum", version);
		params.put("processName", processName);
		KoalaProcessInfo processInfo = jbpmTaskService
				.getKoalaProcessInfo(params);
		try {
			if (processInfo != null) {
				processInfo.setData(data.getBytes());
				processInfo.setPng(png);
			} else {
				processInfo = new KoalaProcessInfo(processName, version, data,
						png);
				processInfo.setActive(isActive);
				processInfo.setPackageName(packageName);
			}
			processInfo.publishProcess();
			
			getJbpmSupport().addProcessToCenter(processInfo, isActive);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	public List<ProcessVO> getProcesses() {
		List<ProcessVO> processStrings = new ArrayList<ProcessVO>();
		Collection<org.drools.definition.process.Process> processes = getJbpmSupport()
				.queryProcesses();
		List<String> ids = new ArrayList<String>();
		for (org.drools.definition.process.Process process : processes) {
			String id = process.getId();
			if(id.contains("@"))id = id.substring(0,id.indexOf("@"));
			if(ids.contains(id))continue;
			else{
				ProcessVO vo = new ProcessVO();
				vo.setId(id);
				vo.setName(process.getName());
				processStrings.add(vo);
				ids.add(id);
			}
			
		}
		return processStrings;
	}

	@WebMethod(exclude = true)
	public List<ProcessVO> getProcessesByProcessName(String processName) {
		List<ProcessVO> processStrings = new ArrayList<ProcessVO>();
		List<KoalaProcessInfo> actives = KoalaProcessInfo
				.getProcessByProcessName(processName);
		for (KoalaProcessInfo info : actives) {
			ProcessVO vo = new ProcessVO();
			vo.setId(info.getProcessId());
			vo.setName(info.getProcessName());
			vo.setVersion(String.valueOf(info.getVersionNum()));
			vo.setActive(true);
			processStrings.add(vo);
		}
		return processStrings;
	}

	public List<String> getProcessByPackage(String packageName) {
		return KoalaProcessInfo.getProcessByPackage(packageName);
	}

	public List<String> getPakcages() {
		return KoalaProcessInfo.getPackages();
	}

	/**
	 * 删除一个流程实例
	 * 
	 * @param processInstanceId
	 */
	public void removeProcessInstance(long processInstanceId) {
		getJbpmSupport().abortProcessInstance(processInstanceId);
	}

	/**
	 * 新增一个全局变量
	 * 
	 * @param key
	 *            全局变量的key值
	 * @param value
	 *            全局变量的value
	 * @param type
	 *            全局变量的类型
	 */
	public void setGlobalVariable(String key, String value, String type) {
		KoalaJbpmVariable.setGlobalVariable(key, value, type);
	}

	/**
	 * 删除一个全局变量
	 * 
	 * @param key
	 */
	public void removeGlobalVariable(String key) {
		KoalaJbpmVariable.removeGlobalVariable(key);
	}

	/**
	 * 新增一个Package级的变量
	 * 
	 * @param packageName
	 *            package变量的名称
	 * @param key
	 *            package变量的key值
	 * @param value
	 *            package变量的value
	 * @param type
	 *            package变量的类型
	 */
	public void setPackageVariable(String packageName, String key,
			String value, String type) {
		KoalaJbpmVariable.setPackageVariable(packageName, key, value, type);
	}

	/**
	 * 移除一个Package变量
	 * 
	 * @param packageName
	 * @param key
	 */
	public void removePackageVariable(String packageName, String key) {
		KoalaJbpmVariable.removePackageVariable(packageName, key);
	}

	/**
	 * 添加一个流程定义级的变量
	 * 
	 * @param packageName
	 *            流程定义所有的包名
	 * @param processId
	 *            流程定义的名称
	 * @param key
	 *            变量的KEY值
	 * @param value
	 *            变量的VALUE值
	 * @param type
	 *            变量的类型
	 */
	public void setProcessVariable(String processId, String key, String value,
			String type) {
		KoalaJbpmVariable.setProcessVariable(processId, key, value, type);
	}

	/**
	 * 移除一个流程定义级的变量
	 * 
	 * @param packageName
	 * @param processId
	 * @param key
	 */
	public void removeProcessVariable(String processId, String key) {
		KoalaJbpmVariable.removeProcessVariable(processId, key);
	}

	/**
	 * 设置流程实例变量值
	 * 
	 * @param processInstanceId
	 *            流程实例ID
	 * @param key
	 *            流程实例变量KEY值
	 * @param value
	 *            流程实例变量VALUE值
	 * @param type
	 *            流程实例变量类型
	 */
	public void setProcessInstanceVariable(long processInstanceId, String key,
			String value, String type) {
		RuleFlowProcessInstance in = (RuleFlowProcessInstance) getJbpmSupport()
				.getProcessInstance(processInstanceId);
		String typeValue = type.toUpperCase();
		if ("STRING".equals(typeValue)) {
			in.setVariable(key, value);
		}
		if ("INT".equals(typeValue)) {
			in.setVariable(key, Integer.parseInt(value));
		}
		if ("LONG".equals(typeValue)) {
			in.setVariable(key, Long.parseLong(value));
		}
		if ("DOUBLE".equals(typeValue)) {
			in.setVariable(key, Double.parseDouble(value));
		}
		if ("BOOLEAN".equals(typeValue)) {
			in.setVariable(key, Boolean.parseBoolean(value));
		}
	}
	
	public List<ProcessInstanceVO> queryAllProcess(String processId) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<ProcessInstanceVO> instances = new ArrayList<ProcessInstanceVO>();
		List<ProcessInstanceLog> logs = jbpmTaskService.findProcessInstances(processId);
		for (ProcessInstanceLog log:logs) {
			ProcessInstanceVO instanceVo = new ProcessInstanceVO();
			ProcessInstanceExpandLog instanceExpandLog = jbpmTaskService.getInstanceExpandLog(log.getId());
			if(instanceExpandLog==null){
				continue;
			}
			instanceVo.setProcessName(instanceExpandLog.getProcessName());
			instanceVo.setStatus(instanceExpandLog.getState());
			instanceVo.setProcessId(log.getProcessId());
			instanceVo.setProcessInstanceId(log.getProcessInstanceId());
			instanceVo.setCreateDate(df.format(log.getStart()));
			instanceVo.setLastUpdateDate(log.getEnd()!=null?df.format(log.getEnd()):null);
			instanceVo.setData(instanceExpandLog.getProcessData());
			instances.add(instanceVo);
		}
		return instances;
	}
	
	/**
	 * 查询某个流程下的正在运行的流程
	 */
	@WebMethod(exclude = true)
	public Page<ProcessInstanceVO> queryRunningProcessInstancesByProcessId(String processId,long firstRow,int pageSize) {
		String hql  ="select new org.openkoala.jbpm.application.vo.ProcessInstanceVO(log.processId,log.processInstanceId,exlog.processName,log.start,log.end,exlog.processData) from ProcessInstanceLog log , ProcessInstanceExpandLog exlog where"
			+"  log.processId = ? and exlog.instanceLogId = log.id and exlog.state = 1";
		Page<ProcessInstanceVO> logs =this.queryChannel.queryPagedResult(hql, new Object[]{processId}, firstRow, pageSize);
		return logs;
	}

	/**
	 * 查询某个流程下已运行完成的流程
	 * @param processId
	 * @param firstRow
	 * @param pageSize
	 * @return
	 */
	@WebMethod(exclude = true)
	public Page<ProcessInstanceVO> queryCompleteProcessInstancesByProcessId(String processId,long firstRow,
			int pageSize) {
		String hql  ="select new org.openkoala.jbpm.application.vo.ProcessInstanceVO(log.processId,log.processInstanceId,exlog.processName,log.start,log.end,exlog.processData) from ProcessInstanceLog log , ProcessInstanceExpandLog exlog where"
				+" and log.processId = ? and exlog.instanceLogId = log.id and exlog.state = 0";
			Page<ProcessInstanceVO> logs =this.queryChannel.queryPagedResult(hql, new Object[]{processId}, firstRow, pageSize);
			return logs;
	}
}
