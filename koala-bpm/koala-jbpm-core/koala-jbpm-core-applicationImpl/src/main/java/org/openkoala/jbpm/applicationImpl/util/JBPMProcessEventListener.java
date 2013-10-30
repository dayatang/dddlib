package org.openkoala.jbpm.applicationImpl.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.drools.event.process.ProcessCompletedEvent;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.process.ProcessNodeLeftEvent;
import org.drools.event.process.ProcessNodeTriggeredEvent;
import org.drools.event.process.ProcessStartedEvent;
import org.drools.event.process.ProcessVariableChangedEvent;
import org.openkoala.jbpm.application.vo.KoalaBPMVariable;
import org.openkoala.jbpm.core.HistoryLog;
import org.openkoala.jbpm.core.ProcessInstanceExpandLog;
import org.openkoala.jbpm.infra.XmlParseUtil;
import org.jbpm.process.audit.JPAProcessInstanceDbLog;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;
import org.jbpm.workflow.instance.node.HumanTaskNodeInstance;

public class JBPMProcessEventListener implements ProcessEventListener {

	public void beforeProcessStarted(ProcessStartedEvent event) {
		RuleFlowProcessInstance in = (RuleFlowProcessInstance)event.getProcessInstance();
		HistoryLog log = new HistoryLog();
		log.setComment("流程启动");
		log.setCreateDate(new Date());
		log.setNodeName("启动");
		log.setUser((String)in.getVariable(KoalaBPMVariable.CREATE_USER));
		log.setProcessInstanceId(event.getProcessInstance().getId());
		log.setProcessData(XmlParseUtil.paramsToXml(in.getVariables()));
		log.setProcessId(in.getProcessId());
		log.save();
	}

	public void afterProcessStarted(ProcessStartedEvent event) {
		RuleFlowProcessInstance in = (RuleFlowProcessInstance)event.getProcessInstance();
		//ProcessInstanceLog instanceLog = JPAProcessInstanceDbLog.findProcessInstance(in.getId());
		ProcessInstanceExpandLog instanceExpandLog = new ProcessInstanceExpandLog();
		instanceExpandLog.setProcessName(in.getProcessName());
		instanceExpandLog.setState(in.getState());
		instanceExpandLog.setInstanceLogId(in.getId());
		instanceExpandLog.setProcessData(XmlParseUtil.paramsToXml(in.getVariables()));
		instanceExpandLog.save();
	}

	public void beforeProcessCompleted(ProcessCompletedEvent event) {
		HistoryLog log = new HistoryLog();
		log.setComment("流程结束");
		log.setCreateDate(new Date());
		log.setNodeName("结束"); 
		log.setProcessInstanceId(event.getProcessInstance().getId());
		RuleFlowProcessInstance in = (RuleFlowProcessInstance)event.getProcessInstance();
		log.setProcessData(XmlParseUtil.paramsToXml(in.getVariables()));
		log.setProcessId(in.getProcessId());
		log.save();
	}

	public void afterProcessCompleted(ProcessCompletedEvent event) {
		Map<String,Object> params = new HashMap<String,Object>();
		RuleFlowProcessInstance in = (RuleFlowProcessInstance)event.getProcessInstance();
		ProcessInstanceLog instanceLog = JPAProcessInstanceDbLog.findProcessInstance(in.getId());
		if(instanceLog!=null)params.put("instanceLogId", instanceLog.getId());
		ProcessInstanceExpandLog instanceExpandLog = ProcessInstanceExpandLog.find(params);
		if(instanceExpandLog!=null){
			instanceExpandLog.setState(in.getState());
			instanceExpandLog.setProcessData(XmlParseUtil.paramsToXml(in.getVariables()));
			instanceExpandLog.save();
		}

	}
	
	public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
	}

	public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {
	}

	public void beforeNodeLeft(ProcessNodeLeftEvent event) {
		if(event.getNodeInstance() instanceof HumanTaskNodeInstance){
			RuleFlowProcessInstance in = (RuleFlowProcessInstance)event.getProcessInstance();
			if(in.getVariable(KoalaBPMVariable.INGORE_LOG)!=null && (Boolean)(in.getVariable(KoalaBPMVariable.INGORE_LOG))){
				in.setVariable(KoalaBPMVariable.INGORE_LOG, false);
				return;
			}
			HistoryLog log = new HistoryLog();
			log.setComment((String)event.getNodeInstance().getVariable(KoalaBPMVariable.COMMENT));
			log.setCreateDate(new Date());
			log.setNodeName(event.getNodeInstance().getNodeName());
			log.setNodeId(event.getNodeInstance().getNodeId());
			log.setResult((String)event.getNodeInstance().getVariable(KoalaBPMVariable.RESULT));
			log.setUser((String)event.getNodeInstance().getVariable(KoalaBPMVariable.NODE_USER));
			log.setProcessInstanceId(event.getProcessInstance().getId());
			log.setProcessData(XmlParseUtil.paramsToXml(in.getVariables()));
			log.setProcessId(in.getProcessId());
			log.save();
		}
	}

	public void afterNodeLeft(ProcessNodeLeftEvent event) {
		
	}

	public void beforeVariableChanged(ProcessVariableChangedEvent event) {
	}

	public void afterVariableChanged(ProcessVariableChangedEvent event) {
	}

}