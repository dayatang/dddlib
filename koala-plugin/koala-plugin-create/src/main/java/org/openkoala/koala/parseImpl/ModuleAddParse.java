package org.openkoala.koala.parseImpl;
import org.dom4j.Document;
import org.openkoala.koala.action.xml.DocumentUtil;
import org.openkoala.koala.action.xml.PomXmlWriter;
import org.openkoala.koala.util.ProjectQueryUtil;
import org.openkoala.koala.widget.ModuleAdd;
import org.openkoala.koala.widget.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 为项目新增或删除模块的实现
 * @author lingen.liu
 *
 */
public class ModuleAddParse{
	
	private static final Logger logger = LoggerFactory.getLogger(ModuleAddParse.class);
	
	public void parse(ModuleAdd add) throws Exception {
		//读取项目中原本的Project对象.
		Project project = ProjectQueryUtil.parseProject(add.getProjectPath());
		logger.info("新增子项目");
		logger.info("子项目名称:"+add.getModule().getModuleName());
		logger.info("项目路径:"+project.getPath());
		logger.info("项目名称:"+project.getAppName());
		ProjectCreateParse create = new ProjectCreateParse();
		create.parse(project,add.getModule());
		
		//向项目中的pom.xml中加入此子项目，module
		Document document = DocumentUtil.readDocument(project.getPath()+"/"+project.getAppName()+"/pom.xml");
		PomXmlWriter.addModule(add.getModule().getModuleName(), document);
		//生成修改后的文档
		DocumentUtil.document2Xml(project.getPath()+"/"+project.getAppName()+"/pom.xml", document);
		
		//重新序列化project
		project.getModule().add(add.getModule());
	}
}
