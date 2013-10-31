package org.openkoala.bpm.application;

import java.util.List;

import org.openkoala.bpm.application.dto.DynaProcessFormDTO;
import org.openkoala.bpm.application.dto.DynaProcessTemplateDTO;
import org.openkoala.bpm.application.dto.ProcessDTO;
import org.openkoala.bpm.application.dto.SelectOptions;

import com.dayatang.querychannel.support.Page;

public interface ProcessFormOperApplication {

	/**
	 * 创建表单
	 * @param form
	 */
	public void createDynaProcessForm(DynaProcessFormDTO form);

	
	public Page<DynaProcessFormDTO> queryDynaProcessFormsByPage(DynaProcessFormDTO search,int currentPage, int pageSize);

	public DynaProcessFormDTO getDynaProcessFormById(Long id);
	
	public void deleteDynaProcessFormById(Long[] ids);

	public void publishProcessTemplate(DynaProcessTemplateDTO template);

	public DynaProcessTemplateDTO getDynaProcessTemplate(String templateName);

	public List<SelectOptions> getDataTypeList();
	
	public List<SelectOptions> getValidateRules();
	
	public List<SelectOptions> getFieldValTypeList();

	public List<DynaProcessTemplateDTO> getActiveProcessTemplates();

	/**
	 * 获取可用流程
	 * @param ignoreExcludeIds  忽略排除流程ID列表
	 * @return
	 */
	public List<ProcessDTO> getActiveProcesses(String[] ignoreExcludeIds);
}
