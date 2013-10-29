package org.openkoala.bpm.KoalaBPM.web.controller.businesssupport;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openkoala.bpm.KoalaBPM.infra.common.ConstantUtil;
import org.openkoala.bpm.application.BusinessSupportApplication;
import org.openkoala.bpm.application.dto.FormShowDTO;
import org.openkoala.bpm.application.dto.TaskDTO;
import org.openkoala.bpm.application.dto.TaskVerifyDTO;
import org.openkoala.bpm.processdyna.core.DynaProcessKey;
import org.openkoala.jbpm.wsclient.ProcessVO;
import org.openkoala.jbpm.wsclient.TaskChoice;
import org.openkoala.koala.auth.ss3adapter.AuthUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayatang.querychannel.support.Page;

@Controller
@RequestMapping("/businessSupport")
public class BusinessSupportController {

	@Autowired
	BusinessSupportApplication businessSupportApplication;
	
	@ResponseBody
	@RequestMapping("/getProcesses")
	public final Map<String, Object> getProcesses(){
		Map<String, Object> dataMap = new HashMap<String,Object>();
		try {
			List<ProcessVO> processList = businessSupportApplication.getProcesses();
			dataMap.put("Rows", processList);
		} catch (Exception e) {
			dataMap.put("error", e.getMessage());
		}
		return dataMap;
	}

	@RequestMapping("/toStartProcessPage")
	public String toStartProcessPage(Model model, String processId){
		try {
			FormShowDTO formShowDTO = businessSupportApplication
					.findTemplateHtmlCodeByProcessId(processId);
//			model.addAttribute("dynaProcessKeysForShow", formShowDTO.getDynaProcessKeysForShow());
			model.addAttribute("processId", processId);
			model.addAttribute("templatehtmlCode", formShowDTO.getTemplateHtmlCode());
			return "businesssupport/processstart";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "errors/errormessages";
		}
	}
	
	@RequestMapping("/startProcess")
	public String startProcess(Model model, HttpServletRequest request){
		try {
//			businessSupportApplication.startProcess(this.packagingProcessStartInfo(request));
			businessSupportApplication.startProcess(AuthUserUtil.getLoginUserName(),
					request.getParameter("processId"), this.setValueForDynaProcessKey(request));
			model.addAttribute("success", "提交成功！");
			return "businesssupport/processstart";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "errors/errormessages";
		}
	}
	
	private Set<DynaProcessKey> setValueForDynaProcessKey(HttpServletRequest request){
		Set<DynaProcessKey> keys = businessSupportApplication.
				getDynaProcessKeysByProcessId(request.getParameter("processId"));
		for(DynaProcessKey key : keys){
			String keyValue = request.getParameter(key.getKeyId());
			key.setKeyValueForShow(keyValue);
		}
		return keys;
	}
	
	/*private ProcessStartInfoDTO packagingProcessStartInfo(HttpServletRequest request){
		ProcessStartInfoDTO processStartInfoDTO = new ProcessStartInfoDTO();
		processStartInfoDTO.setCreater(AuthUserUtil.getLoginUserName());
		processStartInfoDTO.setProcessId(request.getParameter("dynaProcessId"));
		
		//所有动态显示列
		StringBuffer allDynaProcessKeysForShow = new StringBuffer();
		//作为流程变量的key放入该列表
		List<DynaProcessKeyDTO> innerVariables = new ArrayList<DynaProcessKeyDTO>();
		//表单所有的key
		List<DynaProcessKeyDTO> allDynaProcessKeys = new ArrayList<DynaProcessKeyDTO>();
		//每一个元素都是诸如：KJ_keyId@@@keyName##showOrder##innerVariable##keyValue
		String[] dynaProcessKeysForShowArr = request.getParameter("dynaProcessKeysForShow").split(ConstantUtil.XML_ELEMENT_SEPERATOR);
		for(String dynaProcessKeysForShowArrOne : dynaProcessKeysForShowArr){
			//元素分别是：KJ_keyId 和 keyName##showOrder##innerVariable##keyValue
			String[] xmlElementNameValue = dynaProcessKeysForShowArrOne.split(ConstantUtil.XML_ELEMENT_NAME_VALUE_SEPERATOR);
			String keyId = xmlElementNameValue[0].split(ConstantUtil.XML_ELEMENT_NAME_SEPERATOR)[1];
			String keyValue = request.getParameter(keyId);
			if(allDynaProcessKeysForShow.length() > 0){
				allDynaProcessKeysForShow.append(ConstantUtil.XML_ELEMENT_SEPERATOR + dynaProcessKeysForShowArrOne + ConstantUtil.XML_ELEMENT_VALUE_SEPERATOR + keyValue);                             
			}else{
				allDynaProcessKeysForShow.append(dynaProcessKeysForShowArrOne + ConstantUtil.XML_ELEMENT_VALUE_SEPERATOR + keyValue);
			}
			
			DynaProcessKeyDTO dynaProcessKeyDTO = new DynaProcessKeyDTO();
			dynaProcessKeyDTO.setKeyId(keyId);
			dynaProcessKeyDTO.setKeyValueForShow(keyValue);
			allDynaProcessKeys.add(dynaProcessKeyDTO);
			
			DynaProcessKeyDTO innerVariable = getInnerVariable(xmlElementNameValue, keyId, keyValue);
			if(innerVariable != null){
				innerVariables.add(innerVariable);
			}
			
		}

		processStartInfoDTO.setDynaProcessKeysForShow(allDynaProcessKeysForShow.toString());
		processStartInfoDTO.setInnerVariables(innerVariables);
		processStartInfoDTO.setAllDynaProcessKeys(allDynaProcessKeys);
		
		return processStartInfoDTO;
	}*/
	
	/*private DynaProcessKeyDTO getInnerVariable(String[] xmlElementNameValue, String keyId, String keyValue){
		//元素分别是： keyName 和 showOrder 和 innerVariable 和 keyValue
		String[] xmlElementValues = xmlElementNameValue[1].split("##");
		if(xmlElementValues[2].equalsIgnoreCase(ConstantUtil.TRUE)){
			DynaProcessKeyDTO dynaProcessKeyDTO = new DynaProcessKeyDTO();
			dynaProcessKeyDTO.setKeyId(keyId);
			dynaProcessKeyDTO.setKeyValueForShow(keyValue);
			return dynaProcessKeyDTO;
		}
		return null;
	}*/
	
	@ResponseBody
	@RequestMapping("/pageQueryDoneTask")
	public Map<String, Object> pageQueryDoneTask(String processId, int page, int pagesize) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Page<TaskDTO> pageTaskVO = businessSupportApplication.getDoneTasks(processId, 
				AuthUserUtil.getLoginUserName(), page, pagesize);
		dataMap.put("Rows", pageTaskVO.getResult());
		dataMap.put("start", page * pagesize - pagesize);
		dataMap.put("limit", pagesize);
		dataMap.put("Total", pageTaskVO.getTotalCount());
		return dataMap;
	}
	
	@ResponseBody
	@RequestMapping("/getTodoTaskList")
	public Map<String, Object> getTodoTaskList(String processId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("Rows", businessSupportApplication.
				getTodoList(processId, AuthUserUtil.getLoginUserName()));
		return dataMap;
	}
	
	/**
	 * 到审核页面
	 * @param model
	 * @param processId
	 * @param processInstanceId
	 * @return
	 */
	@RequestMapping("/toVerifyPage")
	public String toVerifyPage(Model model, String processId, long processInstanceId, long taskId){
		try {
			FormShowDTO taskInstance = businessSupportApplication.
					getDynaProcessTaskContent(processId, processInstanceId, taskId);
			model.addAttribute("taskInstance", taskInstance); 
			return "businesssupport/verifyTask";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "errors/errormessages";
		}
	}
	
	/**
	 * 到审核页面
	 * @param model
	 * @param processId
	 * @param processInstanceId
	 * @return
	 */
	@RequestMapping("/toHistoryPage")
	public String toHistoryPage(Model model, String processId, long processInstanceId, long taskId){
		try {
			FormShowDTO taskInstance = businessSupportApplication.
					getDynaProcessTaskContent(processId, processInstanceId, taskId);
			model.addAttribute("taskInstance", taskInstance); 
			
			//查询该历史任务的策略及意见
			//????????????????????
			//model.addAttribute("taskVerifyDTO", taskVerifyDTO); 
			
			return "businesssupport/historyTask";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "errors/errormessages";
		}
	}
	
	@RequestMapping("/viewProcessImage")
	public void viewProcessImage(HttpServletRequest request, 
			HttpServletResponse response, long processInstanceId){
		try {
			response.setContentType("image/jpeg"); 
			OutputStream outs = response.getOutputStream(); 
			byte[] image = businessSupportApplication.getPorcessInstanceImageStream(processInstanceId);
			outs.write(image);
			outs.flush();
			outs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@ResponseBody
	@RequestMapping("/verifyTask")
	public Map<String, Object> verifyTask(TaskVerifyDTO taskVerifyDTO){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			//获取表单内容
//			Set<DynaProcessKey> keys = setValueForDynaProcessKey(request);
			taskVerifyDTO.setUser(AuthUserUtil.getLoginUserName());
			//获取策略信息(是否同意、理由)
			taskVerifyDTO = this.getTaskChoice(taskVerifyDTO);
			businessSupportApplication.verifyTask(taskVerifyDTO);
			
			dataMap.put("success", "操作成功！");
		} catch (Exception e) {
			dataMap.put("error", e.getMessage());
		}
		return dataMap;
	}
	
	private TaskVerifyDTO getTaskChoice(TaskVerifyDTO taskVerifyDTO){
		String[] choiceArr = taskVerifyDTO.getChoice().split(ConstantUtil.TASK_CHOICE_SEPERATOR);
		TaskChoice taskChoice = new TaskChoice();
		taskChoice.setKey(choiceArr[0]);
		taskChoice.setValue(choiceArr[1]);
		taskChoice.setValueType(choiceArr[2]);
		taskChoice.setName(choiceArr[3]);
		taskVerifyDTO.setTaskChoice(taskChoice);
		return taskVerifyDTO;
	}

}
