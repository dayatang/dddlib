package org.openkoala.koala.jbpm.designer.jbpmDesigner.web.action.jbpm;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.inject.Inject;

import org.apache.struts2.ServletActionContext;
import org.openkoala.jbpm.application.vo.ProcessVO;
import org.openkoala.koala.jbpm.designer.application.core.BpmDesignerApplication;
import org.openkoala.koala.jbpm.designer.application.vo.PublishURLVO;
import org.openkoala.koala.jbpm.jbpmDesigner.application.GunvorApplication;
import org.openkoala.koala.jbpm.jbpmDesigner.application.vo.Bpmn2;
import org.openkoala.koala.jbpm.jbpmDesigner.application.vo.PackageVO;
import org.openkoala.koala.jbpm.jbpmDesigner.applicationImpl.GunvorApplicationImpl;
import org.springframework.beans.factory.annotation.Value;

public class JbpmAction {

//	private static final Logger logger = LoggerFactory.getLogger(JbpmAction.class);
	
	@Inject
	private GunvorApplication gunvorApplication;

	@Inject
	private BpmDesignerApplication publishURLApplication;

	private List<PackageVO> packages;

	private List<Bpmn2> bpmns;

	private String packageName;

	private String description;

	private String bpmnName;

	private int result;
	
	private String results;

	private List<String> names;

	private List<PublishURLVO> urls;
	
	private String wsdl;
	
	private List<ProcessVO> processes;
	
	private int id;
	
	private String errors;
	
	@Value("${gunvor.server.url}")
	private String gunvorServerUrl;

	public String findPackages() {
		System.out.println(ServletActionContext.getRequest().getLocalAddr()+":"+ServletActionContext.getRequest().getLocalPort());
		packages = gunvorApplication.getPackages();
		return "json2";
	}
	
	public String createBpmn(){
		String wdsl = null;
		try {
			wdsl = gunvorServerUrl+"/org.drools.guvnor.Guvnor/assetExtend/?packageName=" +URLEncoder.encode(packageName, "UTF-8")+ "&assetName=" + URLEncoder.encode(bpmnName, "UTF-8") + "&description="+description;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		results = gunvorApplication.getConnectionString(wdsl);
		return "json2";
	}

	public String findBpmns() {
		this.bpmns = gunvorApplication.getBpmn2s(packageName);
		return "json2";
	}

	public String createPackage() {
		gunvorApplication.createPackage(packageName, description);
		result = 1;
		return "json2";
	}

	public String deleteBpmn() {
		gunvorApplication.deleteBpmn(packageName, bpmnName);
		result = 1;
		return "json2";
	}

	public String deletePackage() {
		gunvorApplication.deletePackage(packageName);
		result = 1;
		return "json2";
	}

	public String getPublish(){
		try {
			this.urls = publishURLApplication.findAllPublishURL();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "json2";
	}
	
	public String publish(){
		try {
			
			this.gunvorApplication.publichJBPM(packageName, bpmnName, wsdl);
			
			result = 1;
		} catch (Exception e) {
			this.errors = e.getMessage();
			e.printStackTrace();
		}
		return "json2";
	}

	public List<PackageVO> getPackages() {
		return packages;
	}

	public void setPackages(List<PackageVO> packages) {
		this.packages = packages;
	}

	public List<Bpmn2> getBpmns() {
		return bpmns;
	}

	public void setBpmns(List<Bpmn2> bpmns) {
		this.bpmns = bpmns;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBpmnName() {
		return bpmnName;
	}

	public void setBpmnName(String bpmnName) {
		this.bpmnName = bpmnName;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}
	
	public String getResults() {
	
		return results;
	}

	public void setResults(String results) {
	
		this.results = results;
	}

	public String processes(){
		//TODO 获取某个流程引擎中的所有流程信息
		return "json2";
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public List<PublishURLVO> getUrls() {
		return urls;
	}

	public void setUrls(List<PublishURLVO> urls) {
		this.urls = urls;
	}

	public String getWsdl() {
		return wsdl;
	}

	public void setWsdl(String wsdl) {
		this.wsdl = wsdl;
	}

	public List<ProcessVO> getProcesses() {
		return processes;
	}

	public void setProcesses(List<ProcessVO> processes) {
		this.processes = processes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getErrors() {
		return errors;
	}

	public void setErrors(String errors) {
		this.errors = errors;
	}

	public String getGunvorServerUrl() {
		return gunvorServerUrl;
	}

	public void setGunvorServerUrl(String gunvorServerUrl) {
		this.gunvorServerUrl = gunvorServerUrl;
	}
	
}
