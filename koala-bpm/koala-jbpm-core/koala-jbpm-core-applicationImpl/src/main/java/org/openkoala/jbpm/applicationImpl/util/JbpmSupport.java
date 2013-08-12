package org.openkoala.jbpm.applicationImpl.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.compiler.PackageBuilder;
import org.drools.definition.KnowledgePackage;
import org.drools.io.impl.ByteArrayResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.openkoala.jbpm.application.vo.KoalaProcessInfoVO;
import org.openkoala.jbpm.core.KoalaJbpmVariable;
import org.openkoala.jbpm.core.KoalaProcessInfo;
import org.openkoala.jbpm.core.service.JBPMTaskService;
import org.jbpm.bpmn2.handler.ServiceTaskHandler;
import org.jbpm.bpmn2.xml.BPMNSemanticModule;
import org.jbpm.process.audit.JPAProcessInstanceDbLog;
import org.jbpm.process.audit.JPAWorkingMemoryDbLogger;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.process.audit.VariableInstanceLog;
import org.jbpm.task.Task;
import org.jbpm.task.TaskService;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.ContentData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

public class JbpmSupport {

	private static final Logger logger = LoggerFactory
			.getLogger(JbpmSupport.class);

	@Inject
	private KnowledgeBase kbase;

	@Inject
	private StatefulKnowledgeSession ksession;

	@Inject
	private TaskService localTaskService;

	@Inject
	private KoalaWSHumanTaskHandler humanTaskHandler;

	@Inject
	private JBPMTaskService jbpmTaskService;

	private KnowledgeBuilder kbuilder;

	private static Map<String, Integer> activeProcessMap = new HashMap<String, Integer>();

	private Map<String, Map<String, Object>> variables = new HashMap<String, Map<String, Object>>();// 全局以及包级别的参数

	/**
	 * <dt>核心就是两个session：
	 * <dd>KnowledgeSession 可以完全通过spring drools配置成功
	 * <dd>TaskServiceSession 这里通过手动编码产生local service
	 * 
	 * <dt>todo 学习点：
	 * <dd>能否完全使用spring xml配置出两个session
	 * <dd>usergroup callback的作用和用法进一步研究
	 * 
	 * @throws Exception
	 */
	public void initialize() throws Exception {
		logger.info("Jbpm Support initialize... ... ...");
		kbuilderAllResurce();// 加载所有流程
		ksession.getWorkItemManager().registerWorkItemHandler("Human Task",
				humanTaskHandler);
		ksession.getWorkItemManager().registerWorkItemHandler("Service Task",
				new ServiceTaskHandler());
		System.setProperty("jbpm.usergroup.callback",
				"org.jbpm.task.service.DefaultUserGroupCallbackImpl");// 引入流程用户初始化接口
		ksession.addEventListener(new JBPMProcessEventListener());
		new JPAWorkingMemoryDbLogger(ksession);// 引入流程历史记录接口
		JPAProcessInstanceDbLog.setEnvironment(ksession.getEnvironment());// 引入流程历史记录的ksesson

		initActiveProcess();
		initVariables();
	}

	public List<TaskSummary> findTaskSummary(String user) {
		return localTaskService.getTasksAssignedAsPotentialOwner(user, "en-UK");
	}

	public List<TaskSummary> findTaskSummaryByGroup(String user,
			List<String> groups) {
		return localTaskService.getTasksAssignedAsPotentialOwner(user, groups,
				"en-UK");
	}

	public Collection<org.drools.definition.process.Process> queryProcesses() {
		return ksession.getKnowledgeBase().getProcesses();
	}

	public org.drools.definition.process.Process getProcess(String processId) {
		return ksession.getKnowledgeBase().getProcess(processId);
	}

	public ProcessInstanceLog getProcessInstanceLog(long processId) {
		return JPAProcessInstanceDbLog.findProcessInstance(processId);
	}

	public List<ProcessInstanceLog> findActiveProcessInstances(
			String processIdActual) {
		return JPAProcessInstanceDbLog
				.findActiveProcessInstances(processIdActual);
	}

	public List<ProcessInstanceLog> findProcessInstances(String processIdActual) {
		return JPAProcessInstanceDbLog.findProcessInstances();
	}

	public List<VariableInstanceLog> findVariableInstances(long processId) {
		return JPAProcessInstanceDbLog.findVariableInstances(processId);
	}

	public List<VariableInstanceLog> findVariableInstances(long processId,
			String variableId) {
		return JPAProcessInstanceDbLog.findVariableInstances(processId,
				variableId);
	}

	public ProcessInstance getProcessInstance(long processInstanceId) {
		return ksession.getProcessInstance(processInstanceId);
	}

	public void abortProcessInstance(long processInstanceId) {
		ksession.abortProcessInstance(processInstanceId);
	}

	public void delegate(long taskId, String userId, String targetUserId) {
		localTaskService.delegate(taskId, userId, targetUserId);
	}

	public Task getTask(long taskId) {
		return localTaskService.getTask(taskId);
	}

	public void startTask(long taskId, String userId) {
		localTaskService.start(taskId, userId);
	}

	public void completeTask(long taskId, String userId, ContentData outputData) {
		localTaskService.complete(taskId, userId, outputData);
	}

	public Map<String, Object> getGlobalVariable() {
		Map<String, Object> values = new HashMap<String, Object>();
		Map<String, Object> globalMap = variables
				.get(KoalaJbpmVariable.KOALA_GLOBAL);
		if (globalMap != null)
			values.putAll(globalMap);
		return values;
	}

	public Map<String, Object> getPackageVariable(String packageName) {
		Map<String, Object> values = new HashMap<String, Object>();
		Map<String, Object> packageMap = variables
				.get(KoalaJbpmVariable.KOALA_PACKAGE + packageName);
		if (packageMap != null)
			values.putAll(packageMap);
		return values;
	}

	public Map<String, Object> getProcessVariable(String processId) {
		Map<String, Object> values = new HashMap<String, Object>();
		Map<String, Object> processMap = variables
				.get(KoalaJbpmVariable.KOALA_PROCESS);
		if (processMap != null)
			values.putAll(processMap);
		return values;
	}

	public ProcessInstance startProcess(String processName,
			Map<String, Object> params) {
		ProcessInstance instance = ksession.startProcess(processName, params);
		ksession.insert(instance);
		ksession.fireAllRules();
		return instance;
	}

	/**
	 * 初始化全局以及包级别的参数
	 */
	private void initVariables() {
		List<KoalaJbpmVariable> varsi = jbpmTaskService.queryJbpmVariable();
		variables = new HashMap<String, Map<String, Object>>();
		for (KoalaJbpmVariable variable : varsi) {
			String key = variable.getScope();
			Map<String, Object> values = null;
			if (variables.containsKey(key)) {
				values = variables.get(key);
			} else {
				values = new HashMap<String, Object>();
			}
			values.put(variable.getKey(), variable.getObjValue());

			variables.put(key, values);
		}
	}

	public void initActiveProcess() {
		List<KoalaProcessInfo> list = jbpmTaskService.findActiveProcess();
		for (KoalaProcessInfo koalaProcessInfo : list) {
			activeProcessMap.put(koalaProcessInfo.getProcessName(),
					koalaProcessInfo.getVersionNum());
		}
	}

	public String getActiveProcess(String process) {
		if (activeProcessMap.containsKey(process)) {
			return process + "@" + activeProcessMap.get(process);
		} else {
			return process;
		}
	}

	public void addProcessToCenter(KoalaProcessInfo processInfo,
			boolean isActive) throws Exception {
		addKoalaProcessInfo(processInfo);
		if (isActive)
			activeProcessMap.put(processInfo.getProcessName(),
					processInfo.getVersionNum());
	}

	private void kbuilderAllResurce() throws Exception {
		
		List<KoalaProcessInfo> processes = jbpmTaskService.getDBResource();
		for (KoalaProcessInfo process : processes) {
			KoalaProcessInfoVO info = new KoalaProcessInfoVO();
			BeanUtils.copyProperties(process, info);
			addKoalaProcessInfoVO(info);
		}
	}

	private void addKoalaProcessInfoVO(KoalaProcessInfoVO info)
			throws Exception {
		PackageBuilder packageBuilder = new PackageBuilder();
		BPMNSemanticModule d = new BPMNSemanticModule();
		packageBuilder.getPackageBuilderConfiguration().addSemanticModule(d);
		packageBuilder.addProcessFromXml(new ByteArrayResource(info.getData()));
		if (packageBuilder.getErrors().size() > 0) {
			throw new Exception(packageBuilder.getErrors().toString());
		}
		try {
			addProcess(info.getData());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private void addProcess(byte[] data) throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(new ByteArrayResource(data), ResourceType.BPMN2);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();

		if (errors.size() > 0) {
			throw new Exception(errors.toString());
		}
		
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());// 刷新
		

	}

	@SuppressWarnings("unused")
	private void addKoalaProcessInfo(KoalaProcessInfo info) throws Exception {
		// 检验发布的BMPN2的正确性
		PackageBuilder packageBuilder = new PackageBuilder();
		BPMNSemanticModule d = new BPMNSemanticModule();
		packageBuilder.getPackageBuilderConfiguration().addSemanticModule(d);
		packageBuilder.addProcessFromXml(new ByteArrayResource(info.getData()));
		if (packageBuilder.getErrors().size() > 0) {
			throw new Exception(packageBuilder.getErrors().toString());
		}
		try {
			addProcess(info.getData());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}