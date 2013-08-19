package org.openkoala.koala.deploy.ejb.util;

import java.util.List;

import org.dom4j.Document;
import org.openkoala.koala.action.xml.DocumentUtil;
import org.openkoala.koala.action.xml.PomXmlWriter;
import org.openkoala.koala.deploy.ejb.pojo.EJBDeploy;
import org.openkoala.koala.deploy.ejb.pojo.ImplObj;
import org.openkoala.koala.exception.KoalaException;
import org.openkoala.koala.pojo.MavenProject;

/**
 * EJB生成部署工具
 * 
 * @author lingen
 * 
 */
public class EJBDeployProcess {

	public static void process(EJBDeploy ejbDeploy) throws KoalaException {
		MavenProject project  = ejbDeploy.getProject();
		List<ImplObj> impls = ejbDeploy.getImpls();
		String projectPath = project.getPath();
		for (ImplObj impl : impls) {
			String interfacePath = projectPath +"/"+impl.getInterfaceModuleName()+"-EJB/src/main/java/"+impl.getInterfaceName();
			//发布给项目WEB用的EAR
			if (ejbDeploy.isLocal()) {
				InterfaceCopyUtil.selfInterface(interfacePath);
				ImplCopyUtil.updateImpl(projectPath, impl);
			}else{
				//发布给第三方系统的EAR
				if (impl.getLocalInterface() != null && impl.getLocalInterface().getMethods() != null) {
					InterfaceCopyUtil.localInterfaceCreate(interfacePath,
							impl.getLocalInterface());
				}
				if (impl.getRemoteInterface() != null && impl.getRemoteInterface().getMethods() != null) {
					InterfaceCopyUtil.remoteIntrefaceCreate(interfacePath,
							impl.getRemoteInterface());
				}
				if ( (impl.getLocalInterface() != null && impl.getLocalInterface().getMethods() != null)
						|| (impl.getRemoteInterface() != null && impl.getRemoteInterface().getMethods() != null)) {
					ImplCopyUtil.updateImpl(projectPath, impl);
				}
			}
			
		}
	}
}
