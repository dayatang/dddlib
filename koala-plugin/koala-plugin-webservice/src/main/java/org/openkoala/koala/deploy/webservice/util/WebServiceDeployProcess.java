package org.openkoala.koala.deploy.webservice.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openkoala.koala.action.velocity.VelocityUtil;
import org.openkoala.koala.deploy.webservice.pojo.InterfaceObj;
import org.openkoala.koala.deploy.webservice.pojo.ValueObj;
import org.openkoala.koala.deploy.webservice.pojo.WebServiceDeploy;
import org.openkoala.koala.exception.KoalaException;

/**
 * 
 * @author lingen
 *
 */
public class WebServiceDeployProcess {

	public static void process(WebServiceDeploy webServiceDeploy) throws KoalaException {
		List<InterfaceObj> interfaces = webServiceDeploy.getInterfaces();
		for(InterfaceObj inter:interfaces){
			InterfaceObjUtil.updateInterfaceObj(inter);
		}
		List<ValueObj> valueObjs = webServiceDeploy.getValueObjs();
		for(ValueObj val:valueObjs){
			ValueObjUtil.updateValueObj(val);
		}
		
		//重新生成新的spring-cxf.xml文件
		List params = new ArrayList();
		params.add(webServiceDeploy);
		try {
			VelocityUtil.velocityObjectParse("vm/ws/war-ws/src/main/resources/META-INF/spring/spring-cxf.xml.vm", webServiceDeploy.getProject().getPath()+"war-ws/src/main/resources/META-INF/spring/spring-cxf.xml", VelocityUtil.getVelocityContext(params));
		} catch (IOException e) {
			e.printStackTrace();
			throw new KoalaException(e.getMessage());
		}
		
		//生成paging.java文件
//		for (MavenProject interfaceProject : webServiceDeploy.getProject().getIntrefaceProjects()) {
//			Map<String, Object> parameters = new HashMap<String, Object>();
//			String packageName = interfaceProject.getProperties().get("base.package") + ".vo";
//			parameters.put("package", packageName);
//			
//			String voClasses = "";
//			for (String javaFile : interfaceProject.getSrcMainJavas()) {
//				if (javaFile.substring(0, javaFile.indexOf(".java")).endsWith("VO")) {
//					voClasses = voClasses + javaFile.substring(javaFile.lastIndexOf("/") + 1) + ",";
//				}
//			}
//			voClasses = voClasses.replace(".java", ".class");
//			parameters.put("classes", voClasses.substring(0, voClasses.length() - 1));
//			
//			try {
//				VelocityUtil.velocityObjectParse("vm/ws-application-impl-paging/Paging.java.vm", 
//						interfaceProject.getPath() + "/src/main/java/" + getPackagePath(packageName) + "/Paging.java",
//						VelocityUtil.getVelocityContext(parameters));
//			} catch (IOException e) {
//				e.printStackTrace();
//				throw new KoalaException(e.getMessage());
//			}
//		}
	}
	
	public static void processSoap(WebServiceDeploy webServiceDeploy) throws KoalaException{
		List<InterfaceObj> interfaces = webServiceDeploy.getInterfaces();
		for(InterfaceObj inter : interfaces){
			InterfaceObjUtil.updateSoapInterfaceObj(inter);
			ImplObjUtil.updateImplObj(inter);
		}
		
		//重新生成新的spring-cxf.xml文件
		List params = new ArrayList();
		params.add(webServiceDeploy);
		try {
			VelocityUtil.velocityObjectParse("vm/soap-config/soap-spring-cxf.xml.vm", webServiceDeploy.getProject().getPath()+"war-ws/src/main/resources/META-INF/spring/spring-cxf.xml", VelocityUtil.getVelocityContext(params));
		} catch (IOException e) {
			e.printStackTrace();
			throw new KoalaException(e.getMessage());
		}
	}
	
//	private static String getPackagePath(String packageName) {
//		return packageName.replaceAll("\\.", "/");
//	}
}
