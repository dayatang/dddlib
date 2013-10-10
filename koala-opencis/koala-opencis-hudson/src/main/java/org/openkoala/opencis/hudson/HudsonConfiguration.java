package org.openkoala.opencis.hudson;

import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * hudson配置类
 * 
 * @author mluo
 * 
 */
public class HudsonConfiguration {

	private final static String CONFIG_FILE = "/ci/hudson/config.xml";
	private final static String CREATE_JOB_METHOD_NAME = "createItem";
	
	private String url;

	private String projectName;

	private String configXml;
	
	public String getCreateJobUrl() {
		StringBuffer createJobUrl = new StringBuffer();
		createJobUrl.append(url);
		if (!url.endsWith("/")) {
			createJobUrl.append("/");
		}
		createJobUrl.append(CREATE_JOB_METHOD_NAME).append("?name=").append(projectName);
		return createJobUrl.toString();
	}

	public HudsonConfiguration(String url, String projectName) {
		this.url = url;
		this.projectName = projectName;
		this.configXml = readConfigXmlFile();
	}

	public HudsonConfiguration(String url, String projectName, String configXml) {
		this.url = url;
		this.projectName = projectName;
		this.configXml = configXml;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getConfigXml() {
		return configXml;
	}

	public void setConfigXml(String configXml) {
		this.configXml = configXml;
	}

	private String readConfigXmlFile() {
		String content = "";
		try {
			content = FileUtils.readFileToString(FileUtils.toFile(HudsonConfiguration.class.getResource(CONFIG_FILE)), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

}
