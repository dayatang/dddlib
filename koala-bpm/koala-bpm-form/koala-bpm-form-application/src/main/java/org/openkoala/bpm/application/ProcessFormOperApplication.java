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

	/**
	 * 分页查询表单列表
	 * @param search
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public Page<DynaProcessFormDTO> queryDynaProcessFormsByPage(DynaProcessFormDTO search,int currentPage, int pageSize);

	/**
	 * 按ID查询表单
	 * @param id
	 * @return
	 */
	public DynaProcessFormDTO getDynaProcessFormById(Long id);
	
	/**
	 * 删除表单
	 * @param ids
	 */
	public void deleteDynaProcessFormById(Long[] ids);

	/**
	 * 获取字段控件类型列表
	 * @return
	 */
	public List<SelectOptions> getDataTypeList();
	
	/**
	 * 获取验证规则列表
	 * @return
	 */
	public List<SelectOptions> getValidateRules();
	
	/**
	 * 获取字段值类型列表
	 * @return
	 */
	public List<SelectOptions> getFieldValTypeList();

	/**
	 * 获取当前所有可用模板
	 * @return
	 */
	public List<DynaProcessTemplateDTO> getActiveProcessTemplates();

	/**
	 * 获取可用流程
	 * @param ignoreExcludeIds  忽略排除流程ID列表
	 * @return
	 */
	public List<ProcessDTO> getActiveProcesses(String[] ignoreExcludeIds);
}
