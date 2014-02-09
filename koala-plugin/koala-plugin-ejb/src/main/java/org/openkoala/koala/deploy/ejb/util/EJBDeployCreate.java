package org.openkoala.koala.deploy.ejb.util;

import java.io.Serializable;

import org.openkoala.koala.action.file.FileCopyAction;
import org.openkoala.koala.deploy.ejb.pojo.EJBDeploy;
import org.openkoala.koala.deploy.ejb.pojo.EJBDeployConfig;
import org.openkoala.koala.exception.JavaDoException;
import org.openkoala.koala.exception.KoalaException;
import org.openkoala.koala.maven.MavenExcuter;
import org.openkoala.koala.pojo.MavenProject;
import org.openkoala.koala.pojo.ModuleType;
import org.openkoala.koala.util.ProjectParseUtil;

/**
 * 帮助类，将一个项目按照默认的规则分析成为一个EJB部署对象
 * @author lingen
 *
 */
public class EJBDeployCreate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5556421817189267244L;
	
	
	public static String publishToEjbWar(EJBDeployConfig config) throws KoalaException{
		MavenProject project = ProjectParseUtil.parseProject(config.getFilePath());
		EJBDeploy ejbDeploy = EJBDeployHelper.getEJBDeploy(project);
		ejbDeploy.setDeployConfig(config);
		ejbDeploy.setLocal(true);
		String newEar = process(ejbDeploy);
		return newEar;
	}
	
	/**
	 * 分析项目产生EJBDeploy,传入项目路径
	 * @param path 项目路径
	 * @return
	 * @throws KoalaException 
	 */
	public static EJBDeploy analytics(String path) throws KoalaException{
		MavenProject project = ProjectParseUtil.parseProject(path);
		return analytics(project);
	}
	
	/**
	 * 分析项目产生EJBDeploy,传入Project
	 * @param project
	 * @return
	 * @throws JavaDoException 
	 */
	public static EJBDeploy analytics(MavenProject project) throws KoalaException{
		//MavenProject destProject = EJBSrcCopy.copySrc(project);
		EJBDeploy ejbDeploy = EJBDeployHelper.getEJBDeploy(project);
		return ejbDeploy;
	}
	
	/**
	 * 给指定的项目生成EJB
	 * @param ejbDeploy
	 * @throws KoalaException 
	 * @throws CloneNotSupportedException 
	 */
	public static String process(EJBDeploy ejbDeploy) throws KoalaException{
		//把源码复制成一份EJB源码
		MavenProject destProject = EJBSrcCopy.copySrc(ejbDeploy);
		MavenProject warProject  = null;
		if(ejbDeploy.isLocal()){
			warProject =EJBSrcCopy.copySrcWar(ejbDeploy);
			MavenExcuter.runMaven(warProject.getPath());
		}
		
		ejbDeploy.setProject(destProject);
		EJBDeployProcess.process(ejbDeploy);
		MavenProject project = ejbDeploy.getProject();
		String newProjectPath = project.getPath();
		MavenExcuter.runMaven(newProjectPath);
		String ear = project.getPath() + "/ear/target/"+project.getName()+"-ear-"+project.getVersion()+".ear";
		ear = ear.replaceAll("//", "/");
		String newEar =  project.getPath().substring(0,project.getPath().lastIndexOf(project.getName()+"-EJB"))+project.getName()+"-EJB.ear";
		FileCopyAction.copyFile(ear,newEar);

		if(ejbDeploy.isLocal()){
			for(MavenProject child:warProject.getChilds()){
				if(child.getType().equals(ModuleType.War)){
					String warsrc = child.getPath()+"/target/"+child.getName()+"-EJB-WAR-"+child.getVersion()+".war";
					String newWar = project.getPath().substring(0,project.getPath().lastIndexOf(project.getName()+"-EJB"))+project.getName()+"-EJB.war";
					FileCopyAction.copyFile(warsrc,newWar);
				}
			}
		}
		
		//生成客户端代码
		if(!ejbDeploy.isLocal()){
			EJBDeployClient.createClient(ejbDeploy);
		}
		return newEar;
	}
	
	public static void main(String args[]) throws KoalaException{
		EJBDeployConfig config = EJBDeployConfig.getEJBDeployConfig(args);
		EJBDeployCreate.publishToEjbWar(config);
	}
	
}
