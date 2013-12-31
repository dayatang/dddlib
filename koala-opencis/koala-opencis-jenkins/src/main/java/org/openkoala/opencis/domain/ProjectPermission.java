package org.openkoala.opencis.domain;

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.openkoala.opencis.PropertyIllegalException;
import org.openkoala.opencis.utils.JenkinsConfUtil;

public class ProjectPermission extends Permission {

    private static final String PROPERTIES_ELEMENT = "properties";

    public static final String JOB_PERMISSION_READ = "hudson.model.Item.Read:";

    private static final String AUTHORIZATIONMATRIXPROPERTY_ELEMENT = "hudson.security.AuthorizationMatrixProperty";

    private String artifactId;

    private Element rootElement;

    private Document document;

    public ProjectPermission(String text, String artifactId) {
        super(text);
        if (StringUtils.isBlank(artifactId)) {
            throw new PropertyIllegalException("The project's artifactId must not be blank!");
        }
        this.artifactId = artifactId;

        createDocument();

        initRootElement();
    }

    private void createDocument() {
        try {
            document = readXMLFile(JenkinsConfUtil.getJobConfigXMLPath(artifactId));
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
        writeToXML(document, JenkinsConfUtil.getJobConfigXMLPath(artifactId));
    }


    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

}
