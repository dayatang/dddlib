package org.openkoala.koala.deploy.ejb.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.openkoala.koala.pojo.MavenProject;

/**
 * EJB部署的定义
 * @author lingen
 *
 */
public class EJBDeploy implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5827365798965886800L;

	/**
	 * 项目路径所在
	 */
	private String path;
	
	private List<ImplObj> impls;
	
	private MavenProject project;
	
	private EJBDeployConfig deployConfig;

	private boolean isLocal = true;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<ImplObj> getImpls() {
		if(impls==null)return new ArrayList<ImplObj>();
		return impls;
	}

	public void setImpls(List<ImplObj> impls) {
		this.impls = impls;
	}
	
	public MavenProject getProject() {
		return project;
	}

	public void setProject(MavenProject project) {
		this.project = project;
	}

	public void addImpl(ImplObj implObj){
		if(this.impls==null)this.impls = new ArrayList<ImplObj>();
		this.impls.add(implObj);
	}

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }

	public EJBDeployConfig getDeployConfig() {
		return deployConfig;
	}

	public void setDeployConfig(EJBDeployConfig deployConfig) {
		this.deployConfig = deployConfig;
	}
    
}
