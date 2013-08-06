package org.openkoala.koala.deploy.webservice.util;

import org.openkoala.koala.action.file.FileCopyAction;
import org.openkoala.koala.deploy.webservice.pojo.WebServiceDeploy;
import org.openkoala.koala.exception.KoalaException;
import org.openkoala.koala.maven.MavenExcuter;
import org.openkoala.koala.pojo.MavenProject;
import org.openkoala.koala.util.ProjectParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServiceDeployCreate {

	
	private static final Logger logger = LoggerFactory
			.getLogger(WebServiceDeployCreate.class);
	
	public static WebServiceDeploy analytics(String path) throws KoalaException {
		MavenProject project = ProjectParseUtil.parseProject(path);
		return analytics(project);
	}

	public static WebServiceDeploy analytics(MavenProject project) throws KoalaException {
		MavenProject newProject = WebServiceSrcCopy.copySrc(project);
		WebServiceDeploy deploy = null;
		try{
			deploy = WebServiceDeployHelper.getWebServiceDeploy(newProject);
		}catch(Exception e){e.printStackTrace();logger.error("出错了");}
		return deploy;
	}

	public static String process(WebServiceDeploy webServiceDeploy) throws KoalaException {
		WebServiceDeployProcess.process(webServiceDeploy);
		String newProjectPath = webServiceDeploy.getProject().getPath();
		MavenExcuter.runMaven(newProjectPath);
		
		MavenProject project = webServiceDeploy.getProject();
		String warPath = newProjectPath+"war-ws/target/"+webServiceDeploy.getProject().getName()+"-war-"+webServiceDeploy.getProject().getVersion()+".war";
		String newWar =  project.getPath().substring(0,project.getPath().lastIndexOf(project.getName()+"-WS"))+project.getName()+"-WS.war";
		FileCopyAction.copyFile(warPath,newWar);
		
		WebServiceDeployClient.createClient(webServiceDeploy, true);
		return newWar;
	}

	public static String processSoap(WebServiceDeploy webServiceDeploy) throws KoalaException {
		WebServiceDeployProcess.processSoap(webServiceDeploy);
		String newProjectPath = webServiceDeploy.getProject().getPath();
		MavenExcuter.runMaven(newProjectPath);
		
		MavenProject project = webServiceDeploy.getProject();
		String warPath = newProjectPath+"war-ws/target/"+webServiceDeploy.getProject().getName()+"-war-"+webServiceDeploy.getProject().getVersion()+".war";
		String newWar =  project.getPath().substring(0,project.getPath().lastIndexOf(project.getName()+"-WS"))+project.getName()+"-WS.war";
		FileCopyAction.copyFile(warPath, newWar);
		
		WebServiceDeployClient.createClient(webServiceDeploy, false);
		return newWar;
	}
	
	public static void main(String args[]) throws KoalaException {
		String path = "E:/workspaces/runtime-EclipseApplication/ws-demo2";
		WebServiceDeploy webServiceDeploy = WebServiceDeployCreate
				.analytics(path);
		WebServiceDeployCreate.process(webServiceDeploy);
	}
}
