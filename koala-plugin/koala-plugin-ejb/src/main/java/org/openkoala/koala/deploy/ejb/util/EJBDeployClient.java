package org.openkoala.koala.deploy.ejb.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openkoala.koala.action.XmlParseUtil;
import org.openkoala.koala.action.velocity.VelocityUtil;
import org.openkoala.koala.deploy.ejb.pojo.EJBDeploy;
import org.openkoala.koala.exception.KoalaException;
import org.openkoala.koala.pojo.MavenProject;
import org.openkoala.koala.pojo.ModuleType;

/**
 * 生成调用EJB的客户端代码，提供使用
 * 
 * @author lingen
 * 
 */
public class EJBDeployClient {

	/**
	 * 创建一个EJB的客户端
	 * 
	 * @param deploy
	 * @throws KoalaException
	 */
	public static void createClient(EJBDeploy deploy) throws KoalaException {
		String path = new File(deploy.getProject().getPath()).getParent();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("path", path);

		params.put("Project", deploy.getProject());

		List<MavenProject> dependencies = deploy.getProject().getChilds();

		List<MavenProject> inters = new ArrayList<MavenProject>();
		for (MavenProject inter : dependencies) {
			if (inter.getType().equals(ModuleType.Application)) {
				inters.add(inter);
			}
		}
		params.put("dependencies", inters);
		params.put("Applications", deploy.getImpls());
		XmlParseUtil.parseXmlAction("xml/ejb-deploy.xml",
				VelocityUtil.getVelocityContext(params));
	}
}
