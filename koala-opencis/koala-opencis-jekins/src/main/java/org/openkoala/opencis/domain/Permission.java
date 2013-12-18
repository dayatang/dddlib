package org.openkoala.opencis.domain;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.openkoala.opencis.PropertyIllegalException;

public abstract class Permission {

	protected static final String CONFIG_XML_NAME = "config.xml";
	
	private static final String USER_HOME_DIR = System.getProperty("user.home");
	
	private static final String JENKINS_DIR = ".jenkins";
	
	protected static final String USER_HOME_PATH = MessageFormat.format("{0}/{1}/", USER_HOME_DIR, JENKINS_DIR);
	
	public static final String XML_CHARSET = "UTF-8";
	
	public static final String PERMISSION_NODE_NAME = "permission";
	
	private String text;

	public Permission(String text) {
		this.text = text;
		verifyTextNotBlank();
	}
	
	public abstract Document save();
	
	protected void writeToXML(Document document, String filePath) throws IOException {
		OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(XML_CHARSET);
        XMLWriter writer = new XMLWriter(new FileWriter(filePath), format);
        writer.write(document);
        writer.close();
	}

	protected boolean collectionIsBlank(List<Element> list) {
		return !(list != null && list.size() > 0);
	}
	
	protected Document readXMLFile(String filePath) throws DocumentException {
		SAXReader reader = new SAXReader();
		return reader.read(new File(filePath));
	}

	private void verifyTextNotBlank() {
		if(StringUtils.isBlank(text)){
			throw new PropertyIllegalException("权限内容不能为空");
		}
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
	
}
