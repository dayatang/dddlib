package org.openkoala.opencis.domain;

import java.io.IOException;
import java.text.MessageFormat;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.openkoala.opencis.PropertyIllegalException;

public class ProjectPermission extends Permission {

	private static final String PROPERTIES_ELEMENT = "properties";

	public static final String JOB_PERMISSION_READ = "hudson.model.Item.Read:";
	
	private static final String AUTHORIZATIONMATRIXPROPERTY_ELEMENT = "hudson.security.AuthorizationMatrixProperty";
	
	private String artifactId;
	
	private Element rootElement;
	
	private Document document;
	
	public ProjectPermission(String text, String artifactId) {
		super(text);
		this.artifactId = artifactId;
		if(StringUtils.isBlank(artifactId)){
			throw new PropertyIllegalException("项目artifactId不能为空");
		}
		
		createDocument();
		
		initRootElement();
	}
	
	private void createDocument() {
		try {
			document = readXMLFile(getJobConfigXMLPath());
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	private void initRootElement() {
		rootElement = document.getRootElement();
	}

	@Override
	public Document save() {
		try {
			writePermissionToXML();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void writePermissionToXML() throws IOException {
		Element authorizationNode = null;
		Element propertiesNode = rootElement.element(PROPERTIES_ELEMENT);
		if (rootElement.element(AUTHORIZATIONMATRIXPROPERTY_ELEMENT) == null) {
			authorizationNode = propertiesNode.addElement(AUTHORIZATIONMATRIXPROPERTY_ELEMENT);
		}
		Element permission = authorizationNode.addElement(PERMISSION_NODE_NAME);
        permission.setText(MessageFormat.format("{0}{1}", JOB_PERMISSION_READ, getText()));
        writeToXML(document, getJobConfigXMLPath());
	}
	
	public String getJobConfigXMLPath() {
		return MessageFormat.format("{0}/jobs/{1}/{2}", USER_HOME_PATH, artifactId, CONFIG_XML_NAME);
	}
	
	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	
}
