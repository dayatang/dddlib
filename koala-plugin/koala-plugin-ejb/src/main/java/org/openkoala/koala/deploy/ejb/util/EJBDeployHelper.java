package org.openkoala.koala.deploy.ejb.util;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openkoala.koala.deploy.ejb.pojo.EJBDeploy;
import org.openkoala.koala.deploy.ejb.pojo.ImplObj;
import org.openkoala.koala.exception.JavaDoException;
import org.openkoala.koala.exception.KoalaException;
import org.openkoala.koala.java.JavaManagerUtil;
import org.openkoala.koala.pojo.MavenProject;
import org.openkoala.koala.util.ProjectParseUtil;

/**
 * 项目的分析
 * @author lingen
 *
 */
public class EJBDeployHelper {

	/**
	 * 获取指定的项目的信息
	 * @param path
	 * @return
	 * @throws KoalaException 
	 */
	public static MavenProject getProject(String path) throws KoalaException{
		MavenProject project = ProjectParseUtil.parseProject(path);
		return project;
	}
	
	/**
	 * 对指定的项目进行扫描
	 * @param path
	 * @return
	 * @throws KoalaException 
	 */
	public static EJBDeploy getEJBDeploy(String path) throws KoalaException{
		MavenProject project =  getProject(path);
		return getEJBDeploy(project);
	}
	
	/**
	 * 扫描指定的项目
	 * @param project
	 * @return
	 * @throws JavaDoException 
	 */
	public static EJBDeploy getEJBDeploy(MavenProject project) throws JavaDoException{
		
		Map<String,String> inters = new HashMap<String,String>();
		List<MavenProject> interfaces = project.getIntrefaceProjects();
		for(MavenProject inter:interfaces){
			for(String interSrc:inter.getSrcMainJavas())
			inters.put(interSrc,inter.getName());
		}
		EJBDeploy ejbDeploy = new EJBDeploy();
		ejbDeploy.setProject(project);
		
		List<MavenProject> impls = project.getImplProjects();
		for(MavenProject impl:impls){
			List<String> javas = impl.getSrcMainJavas();
			for(String implJava:javas){
				List<String> implList = JavaManagerUtil.getInterfaces(impl.getPath()+"/src/main/java/"+implJava);
				for(String interfaceJava:implList){
					interfaceJava = interfaceJava.replaceAll("\\.", "/")+".java";
					if(inters.containsKey(interfaceJava)){
						String interfacePath = inters.get(interfaceJava);
						List<String> methods = JavaManagerUtil.getJavaMethods(project.getPath()+"/"+interfacePath+"/src/main/java/"+interfaceJava);
						ImplObj implObj = new ImplObj(impl.getName(),implJava,interfacePath,interfaceJava,methods);
						ejbDeploy.addImpl(implObj);
					}
				}
			}
		}
		return ejbDeploy;
	}
}