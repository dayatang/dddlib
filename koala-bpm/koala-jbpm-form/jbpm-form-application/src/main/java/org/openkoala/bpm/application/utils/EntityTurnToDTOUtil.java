package org.openkoala.bpm.application.utils;

import java.util.Set;

import org.openkoala.bpm.application.dto.DynaProcessFormDTO;
import org.openkoala.bpm.application.dto.DynaProcessKeyDTO;
import org.openkoala.bpm.application.dto.DynaProcessTemplateDTO;
import org.openkoala.bpm.processdyna.core.DynaProcessForm;
import org.openkoala.bpm.processdyna.core.DynaProcessKey;
import org.openkoala.bpm.processdyna.core.DynaProcessTemplate;

public class EntityTurnToDTOUtil {

	public static DynaProcessFormDTO DynaProcessForm2DTO(DynaProcessForm processForm) {
		DynaProcessFormDTO formDTO = new DynaProcessFormDTO();
		formDTO.setActive(processForm.isActive() ? "true" : "false");
		formDTO.setBizDescription(processForm.getBizDescription());
		formDTO.setBizName(processForm.getBizName());
		formDTO.setId(processForm.getId());
		formDTO.setProcessId(processForm.getProcessId());
		
		DynaProcessTemplate template = processForm.getTemplate();
		if(template != null){
			formDTO.setTemplateId(template.getId());
			formDTO.setTemplateName(template.getTemplateName());
			formDTO.setTemplate(new DynaProcessTemplateDTO(template.getTemplateName(), template.getTemplateDescription(), template.getTemplateData()));
		}

		Set<DynaProcessKey> keys = processForm.getKeys();
		if(keys != null){
			for (DynaProcessKey key : keys) {
				DynaProcessKeyDTO keyDTO = new DynaProcessKeyDTO(key.getKeyId(), key.getKeyName(), key.getKeyType());
				keyDTO.setInnerVariable(key.isInnerVariable() ? "true" : "false");
				keyDTO.setRequired(key.isRequired() ? "true" : "false");
				keyDTO.setValidationExpr(key.getValidationExpr());
				keyDTO.setValidationType(key.getValidationType());
				keyDTO.setShowOrder(key.getShowOrder());
				keyDTO.setOutputVar(key.isOutputVar() ? "true" : "false");
				keyDTO.setKeyOptions(key.getKeyOptions());
				keyDTO.setValOutputType(key.getValOutputType());
				keyDTO.setKeyValueForShow(key.getKeyValueForShow());
				keyDTO.setSecurity(key.getSecurity());
				formDTO.getProcessKeys().add(keyDTO);
			}
		}
		return formDTO;
	}
}
