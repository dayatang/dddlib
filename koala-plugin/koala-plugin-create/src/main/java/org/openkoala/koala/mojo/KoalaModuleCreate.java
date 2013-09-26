package org.openkoala.koala.mojo;
import org.openkoala.koala.parse.XML2ObjectUtil;
import org.openkoala.koala.parseImpl.ModuleAddParse;
import org.openkoala.koala.widget.Module;
import org.openkoala.koala.widget.ModuleAdd;

/**
 * 
 * @author lingen.liu
 *
 */
public class KoalaModuleCreate{
	
	/**
	 * 传入一个moduleAdd参数，为项目增加一个子项目
	 * @param moduleAdd
	 * @throws Exception 
	 */
	public void createModule(ModuleAdd moduleAdd) throws Exception{
		ModuleAddParse parse = new ModuleAddParse();
		parse.parse(moduleAdd);
	}
	
	/**
	 * 传入一个增加子项目的定义，为项目增加一个子项目
	 * @param xmlPath
	 * @throws Exception 
	 */
	public void createModule(String xmlPath) throws Exception{
		XML2ObjectUtil util = XML2ObjectUtil.getInstance();
		ModuleAdd moduleAdd =  (ModuleAdd)util.processParse(xmlPath);
		createModule(moduleAdd);
	}
	
	public static void main(String args[]) throws Exception{
//		String path = "project-add.xml";
//		KoalaModuleCreate create = new KoalaModuleCreate();
//		create.createModule(path);
		
		KoalaModuleCreate moduleCreate = new KoalaModuleCreate();

		Module module = new Module();
		module.setModuleName("infra2");
		module.setModuleType("infra");

		ModuleAdd moduleAdd = new ModuleAdd();
		moduleAdd.setProjectPath("E:/tmp/demo2");

		moduleAdd.setModule(module);

		try {
			moduleCreate.createModule(moduleAdd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
