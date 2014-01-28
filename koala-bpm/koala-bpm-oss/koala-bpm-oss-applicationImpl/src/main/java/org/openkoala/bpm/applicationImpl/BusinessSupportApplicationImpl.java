package org.openkoala.bpm.applicationImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.inject.Named;

import org.openkoala.bpm.KoalaBPM.infra.common.ConstantUtil;
import org.openkoala.bpm.application.BusinessSupportApplication;
import org.openkoala.bpm.application.dto.DynaProcessHistoryValueDTO;
import org.openkoala.bpm.application.dto.DynaProcessKeyDTO;
import org.openkoala.bpm.application.dto.FormShowDTO;
import org.openkoala.bpm.application.dto.TaskDTO;
import org.openkoala.bpm.application.dto.TaskVerifyDTO;
import org.openkoala.bpm.application.exception.FormHavenDefinedException;
import org.openkoala.bpm.application.exception.VerifyTaskException;
import org.openkoala.bpm.processdyna.core.DynaProcessForm;
import org.openkoala.bpm.processdyna.core.DynaProcessHistoryValue;
import org.openkoala.bpm.processdyna.core.DynaProcessKey;
import org.openkoala.bpm.processdyna.core.DynaProcessTemplate;
import org.openkoala.bpm.processdyna.core.DynaProcessValue;
import org.openkoala.jbpm.application.JBPMApplication;
import org.openkoala.jbpm.application.vo.KoalaBPMVariable;
import org.openkoala.jbpm.application.vo.PageTaskVO;
import org.openkoala.jbpm.application.vo.ProcessVO;
import org.openkoala.jbpm.application.vo.TaskChoice;
import org.openkoala.jbpm.application.vo.TaskVO;
import org.openkoala.jbpm.infra.ElementFilter;
import org.openkoala.jbpm.infra.XmlParseUtil;
import org.springframework.transaction.annotation.Transactional;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.querychannel.support.Page;

@Named("businessSupportApplication")
@Transactional(value="transactionManager")
public class BusinessSupportApplicationImpl implements
		BusinessSupportApplication {
	
	private JBPMApplication jbpmApplication;
	
	public JBPMApplication getJBPMApplication(){
		if(jbpmApplication == null){
			jbpmApplication = InstanceFactory.getInstance(JBPMApplication.class);
		}
		return jbpmApplication;
	}
	
	public void initDynaProcessTemplateDatas(List<DynaProcessTemplate> dynaProcessTemplates) {
		try {
			saveDynaProcessTemplates(dynaProcessTemplates);
		} catch (Exception e) {
			throw new RuntimeException("初始化模版数据异常！",e);
		}
	}
	
	private void saveDynaProcessTemplates(List<DynaProcessTemplate> dynaProcessTemplates){
		for (DynaProcessTemplate dynaProcessTemplate : dynaProcessTemplates) {
			if (!DynaProcessTemplate.checkIsExistedByName(dynaProcessTemplate.getTemplateName())) {
				dynaProcessTemplate.save();
			}
		}
	}
	
	public List<ProcessVO> getProcesses() {
		List<ProcessVO> process = null;
		try{
		  process =  getJBPMApplication().getProcesses();
		}catch(Exception e){
			e.printStackTrace();
		}
		return process;
	}

	public FormShowDTO findTemplateHtmlCodeByProcessId(String processId) {
		DynaProcessForm dynaProcessForm = DynaProcessForm
				.queryActiveDynaProcessFormByProcessId(processId);
		if (dynaProcessForm == null) {
			throw new FormHavenDefinedException("还未自定义动态表单!");
		}
		return packagingFormShowDTO(dynaProcessForm);
	}
	
	private FormShowDTO packagingFormShowDTO(DynaProcessForm dynaProcessForm){
		FormShowDTO formShowDTO = new FormShowDTO();
		formShowDTO.setTemplateHtmlCode(dynaProcessForm.packagingHtml());
		return formShowDTO;
	}
	
	/**
	 * 把表单中的所有动态显示列的keyId,keyName,showOrder记录下来，以“###”为分隔符
	 * 并且每列数据都要以：KJ_keyId@@@keyName##showOrder##innerVariable方式封装
	 * @param dynaProcessKeys
	 * @return
	 */
	private String convertStringFromDynaProcessKeys(Set<DynaProcessKey> keys){
		StringBuffer allDynaProcessKeysForShow = new StringBuffer();
		for(DynaProcessKey key : keys){
			//需要显示在待办列表中的key
			if(key.isOutputVar()){
				allDynaProcessKeysForShow = appendDynaProcessKeyForShow(allDynaProcessKeysForShow, key);
			}
		}
		return allDynaProcessKeysForShow.toString();
	}
	
	private StringBuffer appendDynaProcessKeyForShow(StringBuffer allDynaProcessKeysForShow, DynaProcessKey key){
		StringBuffer oneKeyForShow = new StringBuffer();
		oneKeyForShow.append(ConstantUtil.XML_ELEMENT_NAME_SEPERATOR).append(key.getKeyId())
					 .append(ConstantUtil.XML_ELEMENT_NAME_VALUE_SEPERATOR).append(key.getKeyName())
					 .append(ConstantUtil.XML_ELEMENT_VALUE_SEPERATOR).append(key.getShowOrder())
					 .append(ConstantUtil.XML_ELEMENT_VALUE_SEPERATOR).append(key.isInnerVariable())
					 .append(ConstantUtil.XML_ELEMENT_VALUE_SEPERATOR).append(key.getKeyValueForShow());
		if(allDynaProcessKeysForShow.length() > 0){
			allDynaProcessKeysForShow.append(ConstantUtil.XML_ELEMENT_SEPERATOR + oneKeyForShow);                             
		}else{
			allDynaProcessKeysForShow.append(oneKeyForShow);
		}
		return allDynaProcessKeysForShow;
	}

	public void startProcess(String creater, String processId, Set<DynaProcessKey> keys) {
		long processInstanceId = startProcessInJBPM(creater, processId, keys);
		saveFormDatas(processInstanceId, keys);
	}
	
	private long startProcessInJBPM(String creater, String processId, Set<DynaProcessKey> keys){
		String params = convertDynaProcessKeyValuesToXml(keys);
		return getJBPMApplication().startProcess(processId, creater, params);
	}
	
	private void saveFormDatas(long processInstanceId, Set<DynaProcessKey> keys){
		List<DynaProcessValue> dynaProcessValues = packagingDynaProcessValues(processInstanceId, keys);
		saveDynaProcessValues(dynaProcessValues);
	}
	
	private List<DynaProcessValue> packagingDynaProcessValues(long processInstanceId, Set<DynaProcessKey> keys){
		List<DynaProcessValue> dynaProcessValues = new ArrayList<DynaProcessValue>();
		for(DynaProcessKey key : keys){
			dynaProcessValues.add(convertFromDynaProcessKey(key, processInstanceId));
		}
		return dynaProcessValues;
	}
	
	private DynaProcessValue convertFromDynaProcessKey(DynaProcessKey key, long processInstanceId){
		DynaProcessValue dynaProcessValue = new DynaProcessValue();
		dynaProcessValue.setProcessInstanceId(processInstanceId);
		dynaProcessValue.setKeyValue(key.getKeyValueForShow());
		dynaProcessValue.setDynaProcessKey(key);
		return dynaProcessValue;
	}
	
	private void saveDynaProcessValues(List<DynaProcessValue> dynaProcessValues){
		for(DynaProcessValue dynaProcessValue : dynaProcessValues){
			dynaProcessValue.save();
		}
	}
	
	private String convertDynaProcessKeyValuesToXml(Set<DynaProcessKey> keys){
		return XmlParseUtil.paramsToXml(turnListToMap(keys));
	}
	
	private Map<String,Object> turnListToMap(Set<DynaProcessKey> keys){
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		paramsMap = insertDynaProcessKeysShowInVerifyTaskTableHead(keys, paramsMap);
		paramsMap = insertProcessVariables(keys, paramsMap);
		return paramsMap;
	}
	
	private Map<String,Object> insertDynaProcessKeysShowInVerifyTaskTableHead(Set<DynaProcessKey> keys, Map<String,Object> paramsMap){
		//dynaProcessKeys中的每个字符串斗志诸如：KJ_keyId@@@keyName##showOrder##innerVariable##keyValue
		String[] dynaProcessKeysForShowArr = convertStringFromDynaProcessKeys(keys).split(ConstantUtil.XML_ELEMENT_SEPERATOR);
		//当keys含有元素
		if(dynaProcessKeysForShowArr.length > 1){
			//显示在审核任务列表头的key
			for(String  dynaProcessKeyForShow : dynaProcessKeysForShowArr){
				String[] dynaProcessKeyXmlElement = dynaProcessKeyForShow.split(ConstantUtil.XML_ELEMENT_NAME_VALUE_SEPERATOR);
				paramsMap.put(dynaProcessKeyXmlElement[0], dynaProcessKeyXmlElement[1]);
			}
		}
		return paramsMap;
	}
	
	private Map<String,Object> insertProcessVariables(Set<DynaProcessKey> keys, Map<String,Object> paramsMap){
		//流程变量
		for(DynaProcessKey key : keys){
			if(key.isInnerVariable()){
				paramsMap.put(key.getKeyId(), key.getKeyValueForShow());
			}
		}
		return paramsMap;
	}
	
	public List<TaskDTO> getTodoList(String processId, String user) {
		List<TaskVO> taskVOs = getJBPMApplication().processQueryTodoListWithGroup(processId, user, null);
		return getTaskDTOs(taskVOs);
	}

	public List<DynaProcessHistoryValueDTO> getDynaProcessHistoryValuesByProcessInstanceId(
			long processInstanceId) {
		List<DynaProcessHistoryValue> dynaProcessHistoryValues = DynaProcessHistoryValue
				.querykeyValuesByProcessInstanceId(processInstanceId);
		return turnToDTOList(dynaProcessHistoryValues);
	}
	
	private List<DynaProcessHistoryValueDTO> turnToDTOList(List<DynaProcessHistoryValue> dynaProcessHistoryValues){
		List<DynaProcessHistoryValueDTO> results = new ArrayList<DynaProcessHistoryValueDTO>();
		for (DynaProcessHistoryValue dynaProcessHistoryValue : dynaProcessHistoryValues) {
			results.add(turnFromDynaProcessHistoryValue(dynaProcessHistoryValue));
		}
		return results;
	}
	
	private DynaProcessHistoryValueDTO turnFromDynaProcessHistoryValue(DynaProcessHistoryValue dynaProcessHistoryValue){
		DynaProcessHistoryValueDTO dto = new DynaProcessHistoryValueDTO();
		dto.setId(dynaProcessHistoryValue.getId());
		dto.setKeyValue(dynaProcessHistoryValue.getKeyValue());
		dto.setProcessInstanceId(dynaProcessHistoryValue.getProcessInstanceId());
		dto.setDynaProcessKey(turnFromDynaProcessKey(dynaProcessHistoryValue.getDynaProcessKey()));
		return dto;
	}
	
	private DynaProcessKeyDTO turnFromDynaProcessKey(DynaProcessKey dynaProcessKey){
		DynaProcessKeyDTO dynaProcessKeyDTO = new DynaProcessKeyDTO();
		dynaProcessKeyDTO.setKeyId(dynaProcessKey.getKeyId());
		dynaProcessKeyDTO.setKeyName(dynaProcessKey.getKeyName());
		dynaProcessKeyDTO.setShowOrder(dynaProcessKey.getShowOrder());
		return dynaProcessKeyDTO;
	}
	
	public Page<TaskDTO> getDoneTasks(String processId, String user, int currentPage, int pageSize) {
		PageTaskVO pageTaskVO = getJBPMApplication().pageQueryDoneTask(processId, user, currentPage, pageSize);
		return new Page<TaskDTO>(pageTaskVO.getStart(), pageTaskVO.getTotalCount(),
				pageSize, getTaskDTOs(pageTaskVO.getData()));
	}
	
	private List<TaskDTO> getTaskDTOs(List<TaskVO> taskVOs) {
		List<TaskDTO> results = new ArrayList<TaskDTO>();
		for (TaskVO eachTask : taskVOs) {
			TaskDTO taskDTO = new TaskDTO();
			taskDTO.setDynamicColumns(convertFromProcessData(eachTask.getProcessData()));
			taskDTO.setProcessName(eachTask.getProcessName());
			taskDTO.setProcessInstanceId(eachTask.getProcessInstanceId());
			taskDTO.setTaskId(eachTask.getTaskId());
			taskDTO.setProcessId(eachTask.getProcessId());
			results.add(taskDTO);
		}
		return results;
	}
	
	private List<DynaProcessKeyDTO> convertFromProcessData(String processData){
		List<DynaProcessKeyDTO> columns = new ArrayList<DynaProcessKeyDTO>();
		for (Entry<String, Object> eachEntry : getProcessData(processData).entrySet()) {
			DynaProcessKeyDTO column = new DynaProcessKeyDTO();
			String[] values = ((String) eachEntry.getValue()).split(ConstantUtil.XML_ELEMENT_VALUE_SEPERATOR);
			if(values.length > 1){
				column.setKeyId(eachEntry.getKey().substring(eachEntry.getKey()
						.indexOf(ConstantUtil.XML_ELEMENT_NAME_SEPERATOR) + 
						ConstantUtil.XML_ELEMENT_NAME_SEPERATOR.length()));
				column.setKeyName(values[0]);
				column.setShowOrder(Integer.valueOf(values[1]));
				//该填选项可能没有值
				if(values.length > 3){
					column.setKeyValueForShow(values[3]);
				}
				
				columns.add(column);
			}
		}
		return columns;
	}

	/**
	 * 将流程XML数据转成Map
	 * @param content
	 * @return
	 */
	private Map<String, Object> getProcessData(String content) {
		return XmlParseUtil.xmlToPrams(content, new ElementFilter() {
			public String getElementName() {
				return ConstantUtil.XML_ELEMENT_NAME_SEPERATOR + "\\w+";
			}
		});
	}

	public Set<DynaProcessKey> getDynaProcessKeysByProcessId(String processId) {
		return DynaProcessForm.queryActiveDynaProcessFormByProcessId(processId).getKeys();
	}

	public FormShowDTO getDynaProcessTaskContentForVerify(String processId,
			long processInstanceId, long taskId){
		FormShowDTO formShowDTO = getDynaProcessTaskContent(processId,processInstanceId,taskId);
		formShowDTO.setTaskChoices(getJBPMApplication().queryTaskChoice(processInstanceId,taskId));
		return formShowDTO;
	}
	
	public FormShowDTO getDynaProcessTaskContentForHistory(String processId,
			long processInstanceId, long taskId){
		return getDynaProcessTaskContent(processId,processInstanceId,taskId);
	}
	
	private FormShowDTO getDynaProcessTaskContent(String processId,
			long processInstanceId, long taskId){
		FormShowDTO formShowDTO = new FormShowDTO();
		formShowDTO.setProcessInstanceId(processInstanceId);
		formShowDTO.setTaskId(taskId);
		formShowDTO.setHistoryLogs(getJBPMApplication().queryHistoryLog(processInstanceId));
		formShowDTO.setTemplateHtmlCode(findTemplateHtmlCode(processId.split("@")[0], processInstanceId));
		return formShowDTO;
	}
	
	private String findTemplateHtmlCode(String processId, long processInstanceId){
		DynaProcessForm dynaProcessFormInstance = DynaProcessForm
				.queryDynaProcessFormInstance(processId, processInstanceId);
		dynaProcessFormInstance = setKeyValuesToReadOnly(dynaProcessFormInstance);
		return dynaProcessFormInstance.packagingHtml();
	}
	
	/**
	 * 将表单内容设置成只读
	 * @param dynaProcessFormInstance
	 * @return
	 */
	private DynaProcessForm setKeyValuesToReadOnly(DynaProcessForm dynaProcessFormInstance){
		Set<DynaProcessKey> keys = dynaProcessFormInstance.getKeys();
		for(DynaProcessKey key : keys){
			key.setSecurity(ConstantUtil.SECURITY_READ);
		}
		return dynaProcessFormInstance;
	}
	
	public byte[] getPorcessInstanceImageStream(long processInstanceId){
		return getJBPMApplication().getPorcessImageStream(processInstanceId);
	}

	public boolean verifyTask(TaskVerifyDTO taskVerifyDTO) {
		try {
			return getJBPMApplication().completeTask(
					taskVerifyDTO.getProcessInstanceId(), 
					taskVerifyDTO.getTaskId(), 
					taskVerifyDTO.getUser(), 
					convertTaskChoiceToXml(taskVerifyDTO.getComment(),taskVerifyDTO.getTaskChoice()),
					null);
		} catch (Exception e) {
			throw new VerifyTaskException("审核任务出错！",e);
		}
	}
	
	protected String convertTaskChoiceToXml(String comment, TaskChoice taskChoice){
		return XmlParseUtil.paramsToXml(convertTaskChoiceToMap(comment, taskChoice));
	}
	
	private Map<String,Object> convertTaskChoiceToMap(String comment, TaskChoice taskChoice){
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		paramsMap.put(KoalaBPMVariable.COMMENT, comment);
		paramsMap.put(KoalaBPMVariable.RESULT, taskChoice.getName());
		paramsMap.put(taskChoice.getKey(), convertToRightType(taskChoice));
		return paramsMap;
	}
	
	private Object convertToRightType(TaskChoice taskChoice){
		Object val = taskChoice.getValue();
		String valueType = taskChoice.getValueType();
		if ("int".equalsIgnoreCase(valueType)) {
			val = Integer.parseInt(taskChoice.getValue());
		}else if ("long".equalsIgnoreCase(valueType)) {
			val = Long.parseLong(taskChoice.getValue());
		}else if ("float".equalsIgnoreCase(valueType)) {
			val = Float.parseFloat(taskChoice.getValue());
		}else if ("double".equalsIgnoreCase(valueType)) {
			val = Double.parseDouble(taskChoice.getValue());
		}else if ("boolean".equalsIgnoreCase(valueType)) {
			val = Boolean.valueOf(taskChoice.getValue());
		}
		return val;
	}
	
	public String packagingHtml(Long formId) {
		return DynaProcessForm.load(DynaProcessForm.class, formId).packagingHtml();
	}
	
	/**
	 * 仅供测试用例使用
	 * @param jbpmApplication
	 */
	protected void setJbpmApplication(JBPMApplication jbpmApplication) {
		this.jbpmApplication = jbpmApplication;
	}
}
