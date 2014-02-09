package org.openkoala.koala.deploy.ejb.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.dom4j.Document;
import org.openkoala.koala.action.XmlParseUtil;
import org.openkoala.koala.action.file.FileCopyAction;
import org.openkoala.koala.action.velocity.VelocityUtil;
import org.openkoala.koala.action.xml.DocumentUtil;
import org.openkoala.koala.action.xml.PomXmlWriter;
import org.openkoala.koala.deploy.ejb.pojo.EJBDeploy;
import org.openkoala.koala.maven.MavenFileFilter;
import org.openkoala.koala.pojo.Dependency;
import org.openkoala.koala.pojo.MavenProject;
import org.openkoala.koala.pojo.ModuleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 项目源码的复制工作
 * 
 * @author lingen
 * 
 */
public class EJBSrcCopy {

	private static final String EAR_MODULE = "ear";
	private static final String SECURITY_MODULE_IMPL = "koala-security-applicationImpl";
	private static final String SECURITY_MODULE_CORE = "koala-security-core";

	private static final String MONITOR_CORE = "koala-jmonitor-core";

	private static final String QUERY_CORE = "koala-gqc-controller";
	
	private static final String ORGANIZATION_CORE ="koala-organisation-core";
	
	private static final String EJB = "-EJB";
	
	private static final String WAR = "-EJB-WAR";
	
	private static final String IS_HAS_SECURITY="hasSecurity_IMPL";
	
	private static final String IS_HAS_QUERY="hasQueryModule";
	
	private static final String IS_HAS_MONITOR="hasMonitorModule";
	
	private static final String IS_HAS_SECURITY_CORE="hasSecurity_CORE";
	
	private static final String IS_HAS_ORGANIZATION="hasOrganizationModule";

	private static final Logger logger = LoggerFactory
			.getLogger(EJBSrcCopy.class);

	/**
	 * 实现EJB项目的源码的复制
	 * 
	 * @param project
	 * @throws CloneNotSupportedException
	 */
	public static MavenProject copySrc(EJBDeploy ejbDeploy) {
		logger.info("COPY SRC");
		MavenProject project = ejbDeploy.getProject();
		MavenProject destProject = project.clone();
		String path = project.getPath() + "/target/" + project.getName() + EJB
				+ "/";
		try {
			List<MavenProject> impls = project.getImplProjects();

			List<MavenProject> interfaces = project.getIntrefaceProjects();

			interfaces.addAll(impls);

			interfaces.addAll(project.getConfigProjects());

			List<MavenProject> wars = project.getWarProjects();

			// 获取需要修改的依赖关系
			List<Dependency> changes = new ArrayList<Dependency>();

			List<Dependency> earDepens = new ArrayList<Dependency>();

			// 文件的复制工作
			FileCopyAction.clearDir(path);
			FileCopyAction.clearDir(project.getPath() + "/target/ejb-client");
			FileCopyAction.copyFile(project.getPath() + "/pom.xml", path
					+ "/pom.xml");

			List<String> newModules = new ArrayList<String>();

			List<MavenProject> modules = destProject.getChilds();

			Map<String, Object> hasModules = new HashMap<String, Object>();

			if (ejbDeploy.isLocal()) {
				
				hasModules.put(IS_HAS_SECURITY, checkIsHasSecurityWeb(modules));
				
				hasModules.put(IS_HAS_QUERY, checkIsHasQueryModule(modules));
				
				hasModules.put(IS_HAS_MONITOR, checkIsHasMonitorModule(modules));
				
				hasModules.put(IS_HAS_SECURITY_CORE, checkIsHasSecurityCoreModule(modules));
				
				hasModules.put(IS_HAS_ORGANIZATION, checkIsHasOrganizationModule(modules));
				
			}

			for (MavenProject impl : interfaces) {
				Dependency dependency = new Dependency(impl.getGroupId(),
						impl.getArtifactId(), impl.getVersion());
				changes.add(dependency);
				if (impl.getType().equals(ModuleType.Impl)) {
					earDepens.add(dependency);
				}

				// 进行复制
				FileCopyAction.copyDir(impl.getPath(), path + impl.getName()
						+ EJB, MavenFileFilter.newInstance());
				newModules.add(impl.getName() + EJB);
			}
			newModules.add(EAR_MODULE);
			// 开始修改文件
			// 第一步，修改父项目的pom.xml，需要修改artifactId以及module
			Document parentPomXmlDocument = DocumentUtil.readDocument(path
					+ "/pom.xml");
			PomXmlWriter.updatePomArtifactId(parentPomXmlDocument,
					project.getArtifactId() + EJB);
			PomXmlWriter.updateModules(parentPomXmlDocument, newModules);
			DocumentUtil.document2Xml(path + "/pom.xml", parentPomXmlDocument);
			destProject.setPath(path);
			destProject.setArtifactId(project.getArtifactId() + EJB);
			destProject.getChilds().clear();
			// 第二步，开始修改EJB子项目
			for (MavenProject impl : interfaces) {
				String implPomXml = path + "/" + impl.getName() + EJB
						+ "/pom.xml";
				Document modulePomXmlDocument = DocumentUtil
						.readDocument(implPomXml);
				PomXmlWriter.updatePomArtifactId(modulePomXmlDocument,
						impl.getArtifactId() + EJB);
				PomXmlWriter.updatePomParentArtifactId(modulePomXmlDocument,
						project.getArtifactId() + EJB);
				for (Dependency change : changes) {
					PomXmlWriter.updateDependencyRemoveScope(
							modulePomXmlDocument, change, EJB);
				}
				PomXmlWriter.addDependencies("javax.ejb", "ejb-api", "3.0",
						modulePomXmlDocument);
				DocumentUtil.document2Xml(implPomXml, modulePomXmlDocument);

				MavenProject destImpl = impl.clone();
				destImpl.setPath(path + "/" + impl.getName() + EJB);
				destImpl.setArtifactId(impl.getArtifactId() + EJB);
				destImpl.setParent(destProject);
				destProject.getChilds().add(destImpl);
				if (destImpl.getType().equals(ModuleType.conf)) {
					for (MavenProject war : wars) {
						updateEjbConf(destImpl, war);
					}
				}
			}

			// 第三步，生成EJB子项目
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("Project", project);
			params.put("Dependencys", earDepens);
			params.putAll(hasModules);
			VelocityContext context = VelocityUtil.getVelocityContext(params);
			VelocityUtil.velocityDirParse("vm/ear", path + "/ear", context);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return destProject;
	}

	/**
	 * 实现EJB项目的源码的复制
	 * 
	 * @param project
	 * @throws CloneNotSupportedException
	 */
	public static MavenProject copySrcWar(EJBDeploy ejbDeploy) {
		MavenProject project = ejbDeploy.getProject();
		MavenProject destProject = project.clone();
		String path = project.getPath() + "/target/" + project.getName() + WAR
				+ "/";
		try {

			List<MavenProject> interfaces = project.getConfigProjects();

			List<MavenProject> wars = project.getWarProjects();

			interfaces.addAll(wars);

			// 获取需要修改的依赖关系
			List<Dependency> changes = new ArrayList<Dependency>();

			// 文件的复制工作
			FileCopyAction.clearDir(path);
			FileCopyAction.copyFile(project.getPath() + "/pom.xml", path
					+ "/pom.xml");

			List<String> newModules = new ArrayList<String>();

			for (MavenProject impl : interfaces) {
				Dependency dependency = new Dependency(impl.getGroupId(),
						impl.getArtifactId(), impl.getVersion());
				changes.add(dependency);

				// 进行复制
				FileCopyAction.copyDir(impl.getPath(), path + impl.getName()
						+ WAR, MavenFileFilter.newInstance());
				newModules.add(impl.getName() + WAR);
			}

			// 开始修改文件
			// 第一步，修改父项目的pom.xml，需要修改artifactId以及module
			Document parentPomXmlDocument = DocumentUtil.readDocument(path
					+ "/pom.xml");
			PomXmlWriter.updatePomArtifactId(parentPomXmlDocument,
					project.getArtifactId() + WAR);
			PomXmlWriter.updateModules(parentPomXmlDocument, newModules);
			DocumentUtil.document2Xml(path + "/pom.xml", parentPomXmlDocument);
			destProject.setPath(path);
			destProject.setArtifactId(project.getArtifactId() + WAR);
			destProject.getChilds().clear();

			// 第二步，开始修改EJB子项目
			for (MavenProject impl : interfaces) {
				String implPomXml = path + "/" + impl.getName() + WAR
						+ "/pom.xml";
				Document modulePomXmlDocument = DocumentUtil
						.readDocument(implPomXml);
				PomXmlWriter.updatePomArtifactId(modulePomXmlDocument,
						impl.getArtifactId() + WAR);
				PomXmlWriter.updatePomParentArtifactId(modulePomXmlDocument,
						project.getArtifactId() + WAR);
				for (Dependency change : changes) {
					PomXmlWriter.updateDependencyRemoveScope(
							modulePomXmlDocument, change, WAR);
				}
				PomXmlWriter.addDependencies("javax.ejb", "ejb-api", "3.0",
						modulePomXmlDocument);
				DocumentUtil.document2Xml(implPomXml, modulePomXmlDocument);

				MavenProject destImpl = impl.clone();
				destImpl.setPath(path + "/" + impl.getName() + WAR);
				destImpl.setArtifactId(impl.getArtifactId() + WAR);
				destImpl.setParent(destProject);
				destProject.getChilds().add(destImpl);
				if (destImpl.getType().equals(ModuleType.War)) {
					updateEjbWar(ejbDeploy, destProject, destImpl);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return destProject;
	}

	/**
	 * 是否加入了权限系统
	 * 
	 * @param module
	 * @return
	 */
	private static boolean hasModule(MavenProject module, String type) {
		for (Dependency dependency : module.getDependencies()) {
			if (dependency.getArtifactId().equals(type)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否Web模块
	 * 
	 * @param module
	 * @return
	 */
	private static boolean isWebModule(MavenProject module) {
		if (module.getType().equals(ModuleType.War)) {
			return true;
		}
		return false;
	}

	/**
	 * 对EJB的WAR客户端进行修改
	 * 
	 * @param war
	 * @throws IOException
	 */
	private static void updateEjbWar(EJBDeploy ejbDeploy, MavenProject parent,
			MavenProject war) throws IOException {
		// 第一步，删除WAR中对权限子系统，监控子系统，查询子系统的实现依赖，要换成EJB依赖实现
		String pom = war.getPath() + "/pom.xml";
		Document pomDocument = DocumentUtil.readDocument(pom);
		PomXmlWriter.removeDependencies("org.openkoala.security",
				"koala-security-applicationImpl", pomDocument);
		PomXmlWriter.removeDependencies("org.openkoala.security",
				"koala-security-authImpl", pomDocument);
		PomXmlWriter.removeDependencies("org.openkoala.gqc",
				"koala-gqc-applicationImpl", pomDocument);
		PomXmlWriter.removeDependencies("org.openkoala.monitor",
				"koala-jmonitor-applicationImpl", pomDocument);
		PomXmlWriter.removeDependencies("org.openkoala.organisation",
				"koala-organisation-applicationImpl", pomDocument);
		
		//TODO 取消对客户端JAR的依赖与引用
		//PomXmlWriter.addDependencies("org.openkoala.ejb.client", ejbDeploy.getDeployConfig().getServer(), "1.0", pomDocument);

		// 第二步，删除项目本身对应用层实现的依赖，需改成EJB依赖实现
		for (MavenProject impl : ejbDeploy.getProject().getImplProjects()) {
			PomXmlWriter.removeDependencies(impl.getGroupId(),
					impl.getArtifactId(), pomDocument);
		}

		DocumentUtil.document2Xml(pom, pomDocument);

		// 第三步：添加spring-ejb.xml以及spring-ejb.properties的EJB配置文件
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ProjectName", parent.getName());
		params.put("ProjectPath", war.getPath());
		params.put("ConfigPath", parent.getConfigProjects().get(0).getPath());
		params.put("impls", ejbDeploy.getImpls());
		params.put("deployConfig", ejbDeploy.getDeployConfig());
		params.put("koalaversion", parent.getProperties().get("koala.version"));

		params.put("ProjectVersion", parent.getVersion());
		params.put(IS_HAS_SECURITY,hasModule(war, SECURITY_MODULE_IMPL)
				|| hasModule(war, SECURITY_MODULE_CORE));
		params.put(IS_HAS_MONITOR, hasModule(war,MONITOR_CORE));
		params.put(IS_HAS_QUERY, hasModule(war,QUERY_CORE));
		params.put(IS_HAS_ORGANIZATION, hasModule(war,ORGANIZATION_CORE));
		
		

		VelocityContext context = VelocityUtil.getVelocityContext(params);
		VelocityUtil.vmToFile(context, "vm/ejb/"+ejbDeploy.getDeployConfig().getServer()+"/spring-ejb.xml", war.getPath()
				+ "/src/main/resources/META-INF/spring/spring-ejb.xml");
		VelocityUtil.vmToFile(context,
						"vm/ejb/"+ejbDeploy.getDeployConfig().getServer()+"/spring-ejb.properties",
						war.getPath()+ "/src/main/resources/META-INF/props/spring-ejb.properties");

		// 第四步,在root.xml中加入spring-ejb.xml的配置
		XmlParseUtil.parseXml("vm/ejb/war-ejb-spring-update.xml", params);
	}

	/**
	 * 对EJB的Conf配置
	 * 
	 * @param war
	 * @throws IOException
	 */
	private static void updateEjbConf(MavenProject conf, MavenProject war)
			throws IOException {

		// 第三步：添加spring-ejb.xml以及spring-ejb.properties的EJB配置文件
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("ProjectPath", war.getPath());
		params.put("ConfigPath", conf.getPath());
		VelocityUtil.getVelocityContext(params);
	}
	
	
	/**
	 * 检测项目是否包含组织子系统
	 * @param modules
	 * @param hasModules
	 */
	private  static boolean checkIsHasOrganizationModule(List<MavenProject> modules){
		for (MavenProject module : modules) {
				if (isWebModule(module)) {
					if (hasModule(module, ORGANIZATION_CORE)) {
						//hasModules.put("hasOrganizationModule", true);
						return true;
					}
				}
		}
		return false;
	}
	/**
	 * 检测是否包含权限核心
	 * @param modules
	 * @param hasModules
	 */
	private static boolean checkIsHasSecurityCoreModule(List<MavenProject> modules){
		for (MavenProject module : modules) {
			if (isWebModule(module) && hasModule(module, SECURITY_MODULE_CORE)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检测是否包含监控子系统
	 * @param modules
	 * @param hasModules
	 */
	private static boolean checkIsHasMonitorModule(List<MavenProject> modules){
		for (MavenProject module : modules) {
			// 如果是WEB模块并且加入了权限系统
			if (isWebModule(module) && hasModule(module, MONITOR_CORE)) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean checkIsHasQueryModule(List<MavenProject> modules){
		for (MavenProject module : modules) {
			if (isWebModule(module) && hasModule(module, QUERY_CORE)) {
					return true;
			}
		}
		return false;
	}
	
	private static boolean checkIsHasSecurityWeb(List<MavenProject> modules){
		for (MavenProject module : modules) {
			// 如果是WEB模块并且加入了权限系统
			if (isWebModule(module) && hasModule(module, SECURITY_MODULE_IMPL)) {
				return true;
			}
		}
		return false;
	}
}