package org.openkoala.koala.ks;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.openkoala.auth.application.RoleApplication;
import org.openkoala.auth.application.vo.RoleVO;

import javax.inject.Inject;

public class KSUserGroupApplicationImpl implements KSUserGroupApplication {

	@Inject
	private RoleApplication roleApplication;
	
	public String getGroupsByUser(String user) {
		List<RoleVO> roles = roleApplication.findRoleByUserAccount(user);
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("GroupIds");
		for(RoleVO role:roles){
			String value = role.getName();
			Element param = root.addElement("value");
			param.setText(value);
		}
		return document.asXML();
	}
	
	public String getUsersByGroup(String group){
		return null;
	}

}
