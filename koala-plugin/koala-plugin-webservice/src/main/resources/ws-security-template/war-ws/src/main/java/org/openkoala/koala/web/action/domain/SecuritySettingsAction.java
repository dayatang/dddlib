package org.openkoala.koala.web.action.domain;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import org.openkoala.auth.application.domain.SecuritySettingsApplication;
import org.openkoala.auth.application.vo.SecuritySettingsVO;
import com.opensymphony.xwork2.ActionSupport;

public class SecuritySettingsAction extends ActionSupport {
	
	private static final long serialVersionUID = 7761372539851284195L;
	
	private Map<String, Object> dataMap;
	
	@Inject
	private SecuritySettingsApplication securitySettingsApplication;
	
	private SecuritySettingsVO securitySettingsVO;
	
	public SecuritySettingsAction() {
		this.securitySettingsVO = new SecuritySettingsVO();
		this.dataMap = new HashMap<String, Object>();
	}
	
	public String update() {
		securitySettingsApplication.update(securitySettingsVO);
		dataMap.put("result", "success");
		return "JSON";
	}
	
	public String findAll() {
		dataMap.put("data", securitySettingsApplication.findAll());
		return "JSON";
	}
	
	public String save() {
		securitySettingsApplication.save(securitySettingsVO);
		dataMap.put("result", "success");
		return "JSON";
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public void setSecuritySettingsVO(SecuritySettingsVO securitySettingsVO) {
		this.securitySettingsVO = securitySettingsVO;
	}

	public SecuritySettingsVO getSecuritySettingsVO() {
		return securitySettingsVO;
	}

}
