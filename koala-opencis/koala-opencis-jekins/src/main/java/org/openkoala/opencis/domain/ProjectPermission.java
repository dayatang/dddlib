package org.openkoala.opencis.domain;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.openkoala.opencis.PermissionElementNotExistException;
import org.openkoala.opencis.PermissionOptionException;
import org.openkoala.opencis.PropertyIllegalException;

public class ProjectPermission extends Permission {

	public static final String JOB_PERMISSION_READ = "hudson.model.Item.Read:";
	public static final String JOB_MATRIX_PERMISSION_XPATH = "//maven2-moduleset/properties/hudson.security.AuthorizationMatrixProperty";
	
	private String artifactId;
	
	public ProjectPermission(String text, String artifactId) {
		super(text);
		this.artifactId = artifactId;
		if(StringUtils.isBlank(artifactId)){
			throw new PropertyIllegalException("项目artifactId不能为空");
		}
	}

	@Override
	public Document save() {
		try {
			Document document = readXMLFile(getJobConfigXMLPath());
			writePermissionToXML(document);
			return document;
		} catch (DocumentException e) {
			throw new PermissionOptionException(e.toString());
		} catch (IOException e) {
			throw new PermissionOptionException(e.toString());
		}
	}

	@SuppressWarnings("unchecked")
	private void writePermissionToXML(Document document) throws IOException {
		List<Element> list = document.selectNodes(JOB_MATRIX_PERMISSION_XPATH);
		if (collectionIsBlank(list)) {
			throw new PermissionElementNotExistException();
		}
		Element node = list.get(0);
		Element permission = node.addElement(PERMISSION_NODE_NAME);
        permission.setText(JOB_PERMISSION_READ + getText());
        writeToXML(document, getJobConfigXMLPath());
	}

	public String getJobConfigXMLPath() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(USER_HOME_PATH).append("\\jobs\\").append(artifactId).append("\\").append(CONFIG_XML_NAME);
		return buffer.toString();
	}
	
	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	
}
