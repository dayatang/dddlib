package org.openkoala.bpm.KoalaBPM.web.controller.processform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.openkoala.bpm.application.BusinessSupportApplication;
import org.openkoala.bpm.application.ProcessFormOperApplication;
import org.openkoala.bpm.application.dto.DynaProcessFormDTO;
import org.openkoala.bpm.application.dto.DynaProcessKeyDTO;
import org.openkoala.bpm.application.dto.DynaProcessTemplateDTO;
import org.openkoala.bpm.application.dto.ProcessDTO;
import org.openkoala.bpm.application.dto.SelectOptions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dayatang.querychannel.support.Page;

@Controller
@RequestMapping("/processform")
public class FormMgrController {

	@Inject
	private ProcessFormOperApplication processFormOperApplication;
	
	@Inject 
	private BusinessSupportApplication businessSupportApplication;

	@RequestMapping("/list")
	public String listForms(DynaProcessFormDTO search, ModelMap modelMap) {
		// 初始化表单条件
		List<DynaProcessTemplateDTO> templates = processFormOperApplication.getActiveProcessTemplates();
		modelMap.put("templates", templates);
		List<SelectOptions> dataTypeList = processFormOperApplication.getDataTypeList();
		modelMap.put("dataTypes", dataTypeList);
		List<SelectOptions> validateRules = processFormOperApplication.getValidateRules();
		modelMap.put("validateRules", validateRules);
		
		List<SelectOptions> valOutputTypes = processFormOperApplication.getFieldValTypeList();
		modelMap.put("valOutputTypes", valOutputTypes);

		return "processform/list";
	}
	
	
	@ResponseBody
	@RequestMapping("/getDataList")
	public Map<String, Object> getFormList(HttpServletRequest request,Integer page,int pagesize) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Page<DynaProcessFormDTO> pageObj = processFormOperApplication.queryDynaProcessFormsByPage(null, page, pagesize);
		dataMap.put("Rows", pageObj.getResult());
		dataMap.put("start", page * pagesize - pagesize);
		dataMap.put("limit", pagesize);
		dataMap.put("Total", pageObj.getTotalCount());
		return dataMap;
	}


	@ResponseBody
	@RequestMapping(value = "/create", method = { RequestMethod.POST })
	public Map<String, Object> createForm(HttpServletRequest request,String jsondata) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		DynaProcessFormDTO form = JSON.parseObject(jsondata, DynaProcessFormDTO.class);
		
		List<DynaProcessKeyDTO> processKeys = form.getProcessKeys();
		if(processKeys == null || processKeys.isEmpty()){
			dataMap.put("result", "字段不能为空");
			return dataMap;
		}
		try {
			processFormOperApplication.createDynaProcessForm(form);
			dataMap.put("result", "OK");
		} catch (Exception e) {
			dataMap.put("result", e.getMessage());
		}
		return dataMap;
	}


	
	@ResponseBody
	@RequestMapping(value = "/get", method = { RequestMethod.GET })
	public Map<String, Object> getForm(HttpServletRequest request,long id) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		DynaProcessFormDTO form = processFormOperApplication.getDynaProcessFormById(id);
		dataMap.put("result", form);
		return dataMap;
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = { RequestMethod.POST })
	public Map<String, Object> deleteForm(HttpServletRequest request,String id) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		String[] idStrings = id.split(",");
		Long[] ids = new Long[idStrings.length];
		for (int i = 0; i < idStrings.length; i++) {
			ids[i] = Long.parseLong(idStrings[i]);
		}
		processFormOperApplication.deleteDynaProcessFormById(ids);
		dataMap.put("result", "OK");
		return dataMap;
	}
	
	@RequestMapping("/templatePreview")
	public String templatePreview(Long formId,ModelMap modelMap) {
		modelMap.put("htmlContent", businessSupportApplication.packagingHtml(formId));
		return "/processform/preview";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getActiveProcesses", method = { RequestMethod.GET })
	public Map<String, Object> getActiveProcesses(HttpServletRequest request,String usedId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<ProcessDTO> processes = processFormOperApplication.getActiveProcesses(new String[]{usedId});
		//TODO
//		if(processes == null)processes = new ArrayList<ProcessDTO>();
//		processes.add(new ProcessDTO("qingjia", "请假"));
//		processes.add(new ProcessDTO("jiaban", "加班"));
//		processes.add(new ProcessDTO("yuzhi", "预支"));
		dataMap.put("processes", processes);
		return dataMap;
	}
}
