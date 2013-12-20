package org.openkoala.jbpm.applicationImpl.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.compiler.PackageBuilder;
import org.drools.container.spring.beans.persistence.DroolsSpringJpaManager;
import org.drools.container.spring.beans.persistence.DroolsSpringTransactionManager;
import org.drools.io.impl.ByteArrayResource;
import org.drools.persistence.PersistenceContextManager;
import org.drools.persistence.TransactionManager;
import org.drools.persistence.jpa.JPAKnowledgeService;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.openkoala.jbpm.application.vo.KoalaProcessInfoVO;
import org.openkoala.jbpm.core.KoalaJbpmVariable;
import org.openkoala.jbpm.core.KoalaProcessInfo;
import org.openkoala.jbpm.core.service.JBPMTaskService;
import org.jbpm.bpmn2.handler.ServiceTaskHandler;
import org.jbpm.bpmn2.xml.BPMNSemanticModule;
import org.jbpm.process.ProcessBaseImpl;
import org.jbpm.process.audit.JPAProcessInstanceDbLog;
import org.jbpm.process.audit.JPAWorkingMemoryDbLogger;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.process.audit.VariableInstanceLog;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;
import org.jbpm.task.Task;
import org.jbpm.task.TaskService;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.ContentData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import bitronix.tm.TransactionManagerServices;

public class JbpmSupport {

	private static final Logger logger = LoggerFactory
			.getLogger(JbpmSupport.class);

	private KnowledgeBase kbase;

	@Inject
	private StatefulKnowledgeSession ksession;

	@Inject
	private TaskService localTaskService;

	@Inject
	private JBPMTaskService jbpmTaskService;

	private static Map<String, Integer> activeProcessMap = new HashMap<String, Integer>();

	private Map<String, Map<String, Object>> variables = new HashMap<String, Map<String, Object>>();// 全局以及包级别的参数

	private Map<String,List<String>> allProcesses = new HashMap<String,List<String>>();
	@Autowired @Qualifier("jbpmEM")
	EntityManager jbpmEM;
	
	private TransactionManager transactionManager;

	@Autowired @Qualifier("transactionManager")
	private AbstractPlatformTransactionManager aptm;

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
		
//		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
//
//		kbase = kbuilder.newKnowledgeBase();
		
		kbase = ksession.getKnowledgeBase();
		
		
		kbuilderAllResurce();// 加载所有流程
		
//		Environment env = KnowledgeBaseFactory.newEnvironment();
//
//		env.set(EnvironmentName.APP_SCOPED_ENTITY_MANAGER, jbpmEM);
//
//		env.set(EnvironmentName.CMD_SCOPED_ENTITY_MANAGER, jbpmEM);
//
//		env.set("IS_JTA_TRANSACTION", false);
//
//		env.set("IS_SHARED_ENTITY_MANAGER", true);
//
//		transactionManager = new DroolsSpringTransactionManager(aptm);
//
//		env.set(EnvironmentName.TRANSACTION_MANAGER, transactionManager);
//
//		PersistenceContextManager persistenceContextManager = new DroolsSpringJpaManager(
//				env);
//
//		env.set(EnvironmentName.PERSISTENCE_CONTEXT_MANAGER,
//				persistenceContextManager);
//
//		ksession =
//
//		JPAKnowledgeService.newStatefulKnowledgeSession(kbase, null, env);
		
		

		logger.info("Jbpm Support initialize... ... ...");
		KoalaWSHumanTaskHandler humanTaskHandler = new KoalaWSHumanTaskHandler(
				this.localTaskService, ksession);
		humanTaskHandler.setLocal(true);
		humanTaskHandler.connect();
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

	private ThreadLocal<Boolean> ownerStatu = new ThreadLocal<Boolean>();
	private ThreadLocal<TransactionManager> transactions = new ThreadLocal<TransactionManager>();
	public void startTransaction() {
//		transactions.set(new DroolsSpringTransactionManager(new JtaTransactionManager()));
//		boolean owner = transactions.get().begin();
//		ownerStatu.set(owner);
	}

	public void commitTransaction() {
		//transactions.get().commit(ownerStatu.get());
	}

	public void rollbackTransaction() {
		//transactions.get().rollback(ownerStatu.get());
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
		RuleFlowProcessInstance in = (RuleFlowProcessInstance) ksession
				.getProcessInstance(processInstanceId);
		return in;
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
	
	private void updateProcessVersion(String processId,int versionNum){
		List<String> allVersionProcess = null;
		if(allProcesses.containsKey(processId)){
			allVersionProcess = allProcesses.get(processId);
		}else{
			allVersionProcess = new ArrayList<String>();
		}
		allVersionProcess.add(processId+"@"+versionNum);
		allProcesses.put(processId, allVersionProcess);
	}
	
	public List<String> getAllVersionProcess(String processId){
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
			throw e;
		}
		
		updateProcessVersion(info.getProcessName(),info.getVersionNum());
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
		updateProcessVersion(info.getProcessName(),info.getVersionNum());
	}

}