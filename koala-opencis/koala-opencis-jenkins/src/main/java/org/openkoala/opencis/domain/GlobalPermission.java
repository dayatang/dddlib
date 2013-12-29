package org.openkoala.opencis.domain;

import java.io.IOException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.openkoala.opencis.JenkinsSecurityNotOpenException;
import org.openkoala.opencis.PermissionElementNotExistException;
import org.openkoala.opencis.PermissionOperationException;

public class GlobalPermission extends Permission {

	private static final String PROJECT_MATRIX_PERMISSION_XPATH = "//hudson/authorizationStrategy";
	public static final String JENKINS_CONFIG_XML_PATH = USER_HOME_PATH + CONFIG_XML_NAME;
	public static final String PROJECT_MATRIX_PERMISSION_READ = "hudson.model.Hudson.Read:";
	
	public GlobalPermission(String text) {
		super(text);
	}

	@Override
	public Document save() {
		try {
			Document document = readXMLFile(JENKINS_CONFIG_XML_PATH);
			verifyIsOpenSecurity(document);
			writePermissionToXML(document);
			return document;
		} catch (DocumentException e) {
			throw new PermissionOperationException(e.toString());
		} catch (IOException e) {
			throw new PermissionOperationException(e.toString());
		}
	}

	@SuppressWarnings("unchecked")
	private void verifyIsOpenSecurity(Document document) {
		List<Element> list = document.selectNodes("//hudson/useSecurity");
		Element security = list.get(0);
		String securityMark = security.getText();
		if (securityMark.equals("false")) {
			throw new JenkinsSecurityNotOpenException();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void writePermissionToXML(Document document) throws IOException {
		List<Element> list = document.selectNodes(PROJECT_MATRIX_PERMISSION_XPATH);
		if (collectionIsBlank(list)) {
			throw new PermissionElementNotExistException();
		}
		Element node = list.get(0);
		Element permission = node.addElement(PERMISSION_NODE_NAME);
        permission.setText(PROJECT_MATRIX_PERMISSION_READ + getText());
        writeToXML(document, JENKINS_CONFIG_XML_PATH);
	}
}
