package org.openkoala.koala.deploy.webservice.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openkoala.koala.action.XmlParseUtil;
import org.openkoala.koala.action.velocity.VelocityUtil;
import org.openkoala.koala.deploy.webservice.pojo.WebServiceDeploy;
import org.openkoala.koala.exception.KoalaException;
import org.openkoala.koala.pojo.MavenProject;
import org.openkoala.koala.pojo.ModuleType;

/**
 * WS的客户端生成
 * @author lingen
 *
 */
public class WebServiceDeployClient {

	/**
	 * 创建一个WebService的客户端
	 * @param deploy
	 * @throws KoalaException 
	 */
	public static void createClient(WebServiceDeploy deploy, boolean isRestFull) throws KoalaException{
		String path = new File(deploy.getProject().getPath()).getParent();
		
		Map<String,Object> params =  new HashMap<String,Object>();
		params.put("path", path);
		
		params.put("Project", deploy.getProject());
		
		List<MavenProject> dependencies = deploy.getProject().getChilds();
		
		List<MavenProject> inters = new ArrayList<MavenProject>();
		for(MavenProject inter:dependencies){
			if(inter.getType().equals(ModuleType.Application) || inter.getType().equals(ModuleType.conf))
				inters.add(inter);
		}
		params.put("dependencies", inters);
		params.put("Applications", deploy.getInterfaces());
		
		if (isRestFull) {
			XmlParseUtil.parseXmlAction("xml/ws-deploy.xml", VelocityUtil.getVelocityContext(params));
		} else {
			XmlParseUtil.parseXmlAction("xml/ws-soap-deploy.xml", VelocityUtil.getVelocityContext(params));
		}
	}
}
