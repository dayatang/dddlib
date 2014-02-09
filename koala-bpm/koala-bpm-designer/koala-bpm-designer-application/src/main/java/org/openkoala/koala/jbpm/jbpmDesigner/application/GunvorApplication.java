package org.openkoala.koala.jbpm.jbpmDesigner.application;

import java.util.List;

import org.openkoala.koala.jbpm.jbpmDesigner.application.vo.Bpmn2;
import org.openkoala.koala.jbpm.jbpmDesigner.application.vo.PackageVO;

public interface GunvorApplication {

	public List<PackageVO> getPackages();
	
	public List<Bpmn2> getBpmn2s(String packageName);
	
	public void createPackage(String packageName,String description);
	
	public void deleteBpmn(String packageName,String bpmnName);
	
	public void deletePackage(String packageName);
	
	public void publichJBPM(String packageName,String name,String wsdl) throws Exception;
	
	public String createBpm(String packageName,String bpmName);
	
	public String getGunvorServerUrl();
}
