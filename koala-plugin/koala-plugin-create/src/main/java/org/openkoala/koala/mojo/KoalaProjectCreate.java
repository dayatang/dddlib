package org.openkoala.koala.mojo;

import org.openkoala.koala.parse.XML2ObjectUtil;
import org.openkoala.koala.parseImpl.ProjectCreateParse;
import org.openkoala.koala.widget.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 创建新项目的主类
 * @author lingen.liu
 *
 */
public class KoalaProjectCreate {
	
	
	private static final Logger logger = LoggerFactory.getLogger(KoalaProjectCreate.class);
	/**
	 * 传入一个Project,生成项目
	 * @param project
	 * @throws Exception 
	 */
	public void createProject(Project project) throws Exception{
		logger.info("获得project对象:"+project);
		ProjectCreateParse parse = new ProjectCreateParse();
		logger.info("开始创建项目.....");
		parse.parse(project);
	}
	
	public void createProject(String xmlPath) throws Exception{
		logger.info("开始读取XML:"+xmlPath);
		XML2ObjectUtil util = XML2ObjectUtil.getInstance();
		Project project = (Project) util.processParse(xmlPath);
		createSSJProject(project);
	}
	
	/**
	 * 创建一个SSJ的最简单的项目
	 * @param project
	 * @throws Exception
	 */
	public void createSSJProject(Project project) throws Exception{
		logger.info("获得project对象:"+project);
		ProjectCreateParse parse = new ProjectCreateParse();
		logger.info("开始创建项目.....");
		project.initSSJProject();
		parse.parse(project);
	}
	
}
