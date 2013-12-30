package org.openkoala.opencis.domain;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

@SuppressWarnings("unchecked")
public class PermissionTest {

	public static final String PERMISSION_TEXT =  "koala";

	protected boolean xmlConfigContainNewPermission(Document document, String xpath) {
		List<Element> list = document.selectNodes(xpath);
		for (Element each : list) {
			if (each.getText().contains(PERMISSION_TEXT)) {
				return true;
			}
		}
		return false;
	}
	
	protected void removePermission(Document document, String xpath, String configFilePath) {
		List<Element> list = document.selectNodes(xpath);
		Element parent = list.get(0);
		list = document.selectNodes(xpath + "/permission");
		Element child = list.get(list.size() - 1);
		parent.remove(child);
		try {
			writeToXML(document, configFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeToXML(Document document, String configFilePath) throws IOException {
		OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(Permission.XML_CHARSET);
        XMLWriter writer = new XMLWriter(new FileWriter(configFilePath), format);
        writer.write(document);
        writer.close();
	}

}
