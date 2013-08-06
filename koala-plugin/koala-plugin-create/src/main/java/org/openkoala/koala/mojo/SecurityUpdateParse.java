package org.openkoala.koala.mojo;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.VelocityContext;
import org.dom4j.Document;
import org.openkoala.koala.action.XmlParseUtil;
import org.openkoala.koala.action.velocity.VelocityUtil;
import org.openkoala.koala.action.xml.DocumentUtil;
import org.openkoala.koala.action.xml.PomXmlWriter;
import org.openkoala.koala.action.xml.WebXmlUtil;
import org.openkoala.koala.exception.KoalaException;
import org.openkoala.koala.parseImpl.ProjectCreateParse;
import org.openkoala.koala.widget.Module;
import org.openkoala.koala.widget.Project;
import org.openkoala.koala.widget.Security;

/**
 * 更新权限模块，给WEB层加上或修改或去除权限模块
 * @author lingen
 *
 */
public class SecurityUpdateParse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3031526384706866081L;
	
	
	/**
	 * 更新指定项目的，指定Module的权限信息
	 * @param project
	 * @param module
	 * @param security
	 * @throws KoalaException 
	 */
	public static void updateSecurity(Project project,Module module,Security security) throws KoalaException{
		if(project==null || module==null){
			throw new KoalaException("更新权限，指定的project及module不能为空");
		}
		
		if(!module.getModuleType().equals("war")){
			throw new KoalaException("只能指定WEB模块才能进行权限操作");
		}
		
		if(security!=null){
		  //新增或修改权限
		  ProjectCreateParse parse =  new ProjectCreateParse();
		  parse.parse(project, module, security);
		}else{
		    //根据security-remove.xml文件，清除对应的文件
		    Map<String,Object> params = new HashMap<String,Object>();
		    params.put("Project", project);
		    params.put("Module", module);
		    params.put("Security", security);
		    XmlParseUtil.parseXmlAction("xml/remove/security-remove.xml", VelocityUtil.getVelocityContext(params));
		    
		    
		    //删除SysInitDB的servlet
		    String xmlPath = project.getPath()+"/"+project.getAppName()+"/"+module.getModuleName()+"/src/main/webapp/WEB-INF/web.xml";
		    Document webDocument = DocumentUtil.readDocument(xmlPath);
		    //me.lingen.demo1.web.action.auth.SecurityDBInit
		    WebXmlUtil.removeServlet(project.getGroupId()+"."+project.getArtifactId()+".web.action.auth.SecurityDBInit", webDocument);
		    DocumentUtil.document2Xml(xmlPath, webDocument);
		}
	}
}
