package org.openkoala.jbpm.applicationImpl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.SystemEventListenerFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.compiler.PackageBuilder;
import org.drools.io.impl.ByteArrayResource;
import org.drools.persistence.jpa.JPAKnowledgeService;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.openkoala.jbpm.application.vo.KoalaProcessInfoVO;
import org.openkoala.jbpm.core.KoalaJbpmVariable;
import org.openkoala.jbpm.core.KoalaProcessInfo;
import org.openkoala.jbpm.core.service.JBPMTaskService;
import org.jbpm.bpmn2.handler.ServiceTaskHandler;
import org.jbpm.bpmn2.xml.BPMNSemanticModule;
import org.jbpm.process.audit.JPAProcessInstanceDbLog;
import org.jbpm.process.audit.JPAWorkingMemoryDbLogger;
import org.jbpm.process.workitem.wsht.LocalHTWorkItemHandler;
import org.jbpm.task.TaskService;
import org.jbpm.task.service.local.LocalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

public class KoalaBPMSession {

	private static final Logger logger = LoggerFactory
			.getLogger(KoalaBPMSession.class);

	private KnowledgeBase kbase;

	private StatefulKnowledgeSession ksession;

	private TaskService localTaskService;

	@Inject
	private JBPMTaskService jbpmTaskService;

	private static Map<String, Integer> activeProcessMap = new HashMap<String, Integer>();

	private Map<String, Map<String, Object>> variables = new HashMap<String, Map<String, Object>>();// 全局以及包级别的参数

	private Map<String, List<String>> allProcesses = new HashMap<String, List<String>>();

	@Inject
	private EntityManagerFactory entityManagerFactory;

	public void initialize() throws Exception {

		ksession = createSession();

		logger.info("Jbpm Support initialize... ... ...");

		initTaskService();

		initActiveProcess();

		initVariables();
	}

	private void initTaskService() {
		org.jbpm.task.service.TaskService taskService = new org.jbpm.task.service.TaskService(
				entityManagerFactory,
				SystemEventListenerFactory.getSystemEventListener());

		localTaskService = new LocalTaskService(taskService);
		ksession.getWorkItemManager().registerWorkItemHandler("Human Task",
				new LocalHTWorkItemHandler(localTaskService, ksession));

		ksession.getWorkItemManager().registerWorkItemHandler("Service Task",
				new ServiceTaskHandler());
		System.setProperty("jbpm.usergroup.callback",
				"org.openkoala.jbpm.usergroup.KoalaBPMUserGroupCallBack");// 引入流程用户初始化接口
		ksession.addEventListener(new JBPMProcessEventListener());
		new JPAWorkingMemoryDbLogger(ksession);// 引入流程历史记录接口
		JPAProcessInstanceDbLog.setEnvironment(ksession.getEnvironment());// 引入流程历史记录的ksesson
	}

	private StatefulKnowledgeSession createSession() throws Exception {

		kbase = KnowledgeBuilderFactory.newKnowledgeBuilder()
				.newKnowledgeBase();

		kbuilderAllResurce();// 加载所有流程

		Environment env = KnowledgeBaseFactory.newEnvironment();
		env.set(EnvironmentName.ENTITY_MANAGER_FACTORY, entityManagerFactory);
		Properties properties = new Properties();
		KnowledgeSessionConfiguration config = KnowledgeBaseFactory
				.newKnowledgeSessionConfiguration(properties);

		return JPAKnowledgeService.newStatefulKnowledgeSession(kbase, config,
				env);

	}

	public StatefulKnowledgeSession getKsession() {
		return ksession;
	}

	public TaskService getLocalTaskService() {
		return localTaskService;
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

	private void updateProcessVersion(String processId, int versionNum) {
		List<String> allVersionProcess = null;
		if (allProcesses.containsKey(processId)) {
			allVersionProcess = allProcesses.get(processId);
		} else {
			allVersionProcess = new ArrayList<String>();
		}
		allVersionProcess.add(processId + "@" + versionNum);
		allProcesses.put(processId, allVersionProcess);
		activeProcessMap.put(processId, versionNum);
	}

	public List<String> getAllVersionProcess(String processId) {
		return allProcesses.get(processId);
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
		}

		updateProcessVersion(info.getProcessName(), info.getVersionNum());
	}

	private void addProcess(byte[] data) throws Exception {

		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();
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
		updateProcessVersion(info.getProcessName(), info.getVersionNum());
	}

}