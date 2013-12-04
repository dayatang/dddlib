package org.openkoala.bpm.application;

import java.util.List;
import java.util.Set;

import org.openkoala.bpm.application.dto.DynaProcessHistoryValueDTO;
import org.openkoala.bpm.application.dto.FormShowDTO;
import org.openkoala.bpm.application.dto.TaskDTO;
import org.openkoala.bpm.application.dto.TaskVerifyDTO;
import org.openkoala.bpm.processdyna.core.DynaProcessKey;
import org.openkoala.bpm.processdyna.core.DynaProcessTemplate;
import org.openkoala.jbpm.application.vo.ProcessVO;

import com.dayatang.querychannel.support.Page;


public interface BusinessSupportApplication {
	
//	boolean isExistDynaProcessTemplate(String templateName);
	
	/**
	 * 初始化表单模版数据（一列模版、两列模版等）
	 * @param dynaProcessTemplates
	 */
	void initDynaProcessTemplateDatas(List<DynaProcessTemplate> dynaProcessTemplates);
	
	/**
	 * 返回所有流程定义
	 * @return
	 */
	List<ProcessVO> getProcesses();
	
	/**
	 * 返回模版表单的html代码
	 * @return
	 */
	FormShowDTO findTemplateHtmlCodeByProcessId(String processId);
	
	/**
	 * 开启流程
	 * @param processStartInfo
	 */
	 void startProcess(String creater, String processId, Set<DynaProcessKey> keys);
	
	/**
	 * 获取指定用户的待办任务列表
	 * @param user
	 * @return
	 */
	List<TaskDTO> getTodoList(String processId, String user);

	/**
	 * 获取指定流程实例的任务列表
	 * @param processInstanceId
	 * @return
	 */
	List<DynaProcessHistoryValueDTO> getDynaProcessHistoryValuesByProcessInstanceId(long processInstanceId);
	
	
	/**
	 * 获取已办任务列表
	 * @return
	 */
	Page<TaskDTO> getDoneTasks(String processId, String user, int currentPage, int pageSize);
	
	/**
	 * 获取指定流程所有的key
	 * @param processId
	 * @return
	 */
	Set<DynaProcessKey> getDynaProcessKeysByProcessId(String processId);
	
	/**
	 *  获取待办任务实例
	 * @param processId
	 * @param processInstanceId
	 * @param taskId
	 * @return
	 */
	FormShowDTO getDynaProcessTaskContentForVerify(String processId, long processInstanceId, long taskId);
	
	/**
	 *  获取历史任务实例
	 * @param processId
	 * @param processInstanceId
	 * @param taskId
	 * @return
	 */
	FormShowDTO getDynaProcessTaskContentForHistory(String processId, long processInstanceId, long taskId);
	
	/**
	 * 返回流程实例的流程图
	 * @param processInstanceId
	 * @return
	 */
	byte[] getPorcessInstanceImageStream(long processInstanceId);
	
	/**
	 * 审核
	 * @param taskVerifyDTO
	 */
	boolean verifyTask(TaskVerifyDTO taskVerifyDTO);
	
	public String packagingHtml(Long formId);

}
