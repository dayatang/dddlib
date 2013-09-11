package org.openkoala.koala.deploy.webservice.util;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openkoala.koala.deploy.webservice.pojo.InterfaceObj;
import org.openkoala.koala.deploy.webservice.pojo.MediaType;
import org.openkoala.koala.deploy.webservice.pojo.RestWebServiceMethod;
import org.openkoala.koala.deploy.webservice.pojo.ValueObj;
import org.openkoala.koala.deploy.webservice.pojo.WebServiceDeploy;
import org.openkoala.koala.deploy.webservice.pojo.WebServiceMethod;
import org.openkoala.koala.deploy.webservice.pojo.WebServiceType;
import org.openkoala.koala.exception.KoalaException;
import org.openkoala.koala.java.JavaManagerUtil;
import org.openkoala.koala.pojo.MavenProject;
import org.openkoala.koala.util.ProjectParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Webservice的帮助类
 * 
 * @author lingen
 * 
 */
public class WebServiceDeployHelper {

	private static final Logger logger = LoggerFactory.getLogger(WebServiceDeployHelper.class);

	public static MavenProject getProject(String path) throws KoalaException {
		MavenProject project = ProjectParseUtil.parseProject(path);
		return project;
	}

	public static WebServiceDeploy getWebServiceDeploy(String path) throws KoalaException {
		MavenProject project = getProject(path);
		return getWebServiceDeploy(project);
	}

	public static WebServiceDeploy getWebServiceDeploy(MavenProject project) throws KoalaException {
		WebServiceDeploy webServiceDeploy = new WebServiceDeploy();
		webServiceDeploy.setProject(project);
		Map<String, String> inters = new HashMap<String, String>();
		List<MavenProject> interfaces = project.getIntrefaceProjects();
		for (MavenProject inter : interfaces) {
			for (String interSrc : inter.getSrcMainJavas()) {
				inters.put(interSrc, inter.getPath() + "/src/main/java/" + interSrc);
			}
		}

		List<MavenProject> impls = project.getImplProjects();
		for (MavenProject impl : impls) {
			List<String> javas = impl.getSrcMainJavas();
			for (String java : javas) {
				List<String> implList = JavaManagerUtil.getInterfaces(impl.getPath() + "/src/main/java/" + java);
				for (String implInter : implList) {
					implInter = implInter.replaceAll("\\.", "/") + ".java";
					if (inters.containsKey(implInter)) {
						String interfaceJava = inters.get(implInter);
						List<MethodDeclaration> methods = JavaManagerUtil.getMethodDeclaration(interfaceJava);
						List<WebServiceMethod> methodList = new ArrayList<WebServiceMethod>();
						for (MethodDeclaration method : methods) {
							methodList.add(generateRestWebServiceMethod(method));
						}
						InterfaceObj interfaceObj = new InterfaceObj(implInter, interfaceJava, java, impl.getPath()
								+ "/src/main/java/" + java, methodList);
						interfaceObj.setQualifiedImplName(impl.getPath() + "/src/main/java/" + java);
						webServiceDeploy.getInterfaces().add(interfaceObj);
					}
				}
			}
		}
		// 分析接口中所拥有的所有对象
		Map<String, String> valueObjMap = new HashMap<String, String>();
		Map<String, String> valueSrc = new HashMap<String, String>();
		for (MavenProject valueObjProject : project.getChilds()) {
			for (String interSrc : valueObjProject.getSrcMainJavas()) {
				valueSrc.put(interSrc.substring(interSrc.lastIndexOf("/") + 1, interSrc.lastIndexOf(".java")), interSrc);
				valueObjMap.put(interSrc.substring(interSrc.lastIndexOf("/") + 1, interSrc.lastIndexOf(".java")),
						valueObjProject.getPath() + "/src/main/java/" + interSrc);
			}
		}
		// 分析接口，接口的返回对象，参数中的对象，如果是非基本类型，都要解析成为值对象。
		// 如果某个接口中有返回对象或参数中的值对象在项目中不存在，则此接口不纳入WebService的配置中，从deploy
		// 中删除

		for (String inter : inters.values()) {
			CompilationUnit cu;
			try {
				logger.info(inter);
				File file = new File(inter);
				cu = JavaParser.parse(file);
				List<String> vos = JavaManagerUtil.getClassVO(cu);
				System.out.println("VOS:" + vos);
				for (String vo : vos) {
					String javaFile = valueObjMap.get(vo);
					if (javaFile != null) {
						String voSrc = valueSrc.get(vo);
						javaFile = javaFile.replaceAll("//", "/");
						logger.info("JARFILE:" + javaFile);
						List<String> properties = JavaManagerUtil.getJavaProperties(javaFile);
						logger.info(properties.toString());
						ValueObj valueObj = new ValueObj(voSrc, valueObjMap.get(vo), properties);
						logger.info(valueObj.toString());
						if (webServiceDeploy.getValueObjs().contains(valueObj) == false) {
							webServiceDeploy.getValueObjs().add(valueObj);
						}
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return webServiceDeploy;
	}

	private static RestWebServiceMethod generateRestWebServiceMethod(MethodDeclaration method) {
		if (method == null) {
			return null;
		}

		WebServiceType httpMehtod = AutoRestRuleUtils.getHttpMethodByMethodName(method.getName());
		RestWebServiceMethod restWebServiceMethod = new RestWebServiceMethod(method, httpMehtod);
		restWebServiceMethod.setParamType(AutoRestRuleUtils.getDefaultParamType(httpMehtod, method));
		restWebServiceMethod
				.setUriPath(AutoRestRuleUtils.generateUriPathByParameType(restWebServiceMethod.getParamType(), method));

		Set<MediaType> mediaTypes = new HashSet<MediaType>();
		mediaTypes.add(MediaType.APPLICATION_XML);
		restWebServiceMethod.setProducesMediaTypes(mediaTypes);
		// restWebServiceMethod.setConsumesMediaTypes(mediaTypes);

		return restWebServiceMethod;
	}

	public static void main(String args[]) {
		String interSrc = "org.koala";
		System.out.println(interSrc.substring(interSrc.lastIndexOf(".") + 1));
	}
}
