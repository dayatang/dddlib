package org.openkoala.koala.mojo;

import org.openkoala.koala.parse.XML2ObjectUtil;
import org.openkoala.koala.parseImpl.ModuleUpdateParse;
import org.openkoala.koala.widget.ModuleUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 提供针对Module的模块增加与删除的功能
 * @author lingen.liu
 *
 */
public class KoalaModuleUpdate {
	
	private static final Logger logger = LoggerFactory.getLogger(KoalaModuleUpdate.class);
	
	/**
	 * 传入一个ModuleUpdate参数，更新项目
	 * @param moduleUpdate
	 * @throws Exception 
	 */
	public void updateModule(ModuleUpdate moduleUpdate) throws Exception{
		logger.info("更新项目功能");
		String projectPath = moduleUpdate.getProjectPath();
		ModuleUpdateParse update = new ModuleUpdateParse(moduleUpdate);
		update.parse();
	}
	
	/**
	 * 传入一个定义模块修改的参数，更新项目
	 * @param path
	 */
	public void updateModule(String xmlPath) throws Exception{
		XML2ObjectUtil util = XML2ObjectUtil.getInstance();
		ModuleUpdate update = (ModuleUpdate)util.processParse(xmlPath);
		updateModule(update);
	}
	
	public static void main(String args[]) throws Exception{
		String xmlPath = "xml/project-update.xml";
		KoalaModuleUpdate update = new KoalaModuleUpdate();
		update.updateModule(xmlPath);
	}
}
