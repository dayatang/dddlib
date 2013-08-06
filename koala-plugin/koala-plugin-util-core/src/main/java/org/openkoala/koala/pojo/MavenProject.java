package org.openkoala.koala.pojo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

/**
 * 一个mavenProject代表一个Maven项目
 * 
 * @author lingen
 * 
 */
public class MavenProject {
	
	/**
	 * 项目所在路径
	 */
	private String path;

	/**
	 * 项目名称
	 */
	private String name;

	/**
	 * 项目的groupId
	 */
	private String groupId;

	private String artifactId;

	private String version;

	private String packaging;

	/**
	 * 项目拥有的属性值
	 */
	private Map<String, String> properties;
	
	private List<Dependency> dependencies;
	
	/**
	 * 同项目间的依赖关系，被包含于dependencies属性中
	 */
	private List<Dependency> someProjectDependencies;
	
	private MavenProject parent;
	/**
	 * 项目拥有的子项目
	 */
	private List<MavenProject> childs;

	/**
	 * 项目中拥有的源文件
	 */
	private List<String> srcMainJavas;
	
	/**
	 * 项目所属的类型及层次
	 */
	private ModuleType type;

	/**
	 * 项目中拥有的源配置
	 */
	private List<String> srcMainResources;
	
	private List<String> srcTestJavas;
	
	private List<String> srcTestResources;
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroupId() {
	    if(groupId==null && this.getParent()!=null){
            return this.getParent().getGroupId();
        }
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
	    if(version==null && this.getParent()!=null){
	        return this.getParent().getVersion();
	    }
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPackaging() {
		return packaging;
	}

	public void setPackaging(String packaging) {
		this.packaging = packaging;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public List<Dependency> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<Dependency> dependencies) {
		this.dependencies = dependencies;
	}

	public MavenProject getParent() {
		return parent;
	}

	public void setParent(MavenProject parent) {
		this.parent = parent;
	}

	public List<MavenProject> getChilds() {
		if(childs==null)childs = new ArrayList<MavenProject>();
		return childs;
	}

	public void setChilds(List<MavenProject> childs) {
		this.childs = childs;
	}

	public List<String> getSrcMainJavas() {
		if(srcMainJavas==null)srcMainJavas = new ArrayList<String>();
		return srcMainJavas;
	}

	public void setSrcMainJavas(List<String> srcMainJavas) {
		this.srcMainJavas = srcMainJavas;
	}

	public List<String> getSrcMainResources() {
		if(srcMainResources==null)srcMainResources = new ArrayList<String>();
		return srcMainResources;
	}

	public void setSrcMainResources(List<String> srcMainResources) {
		this.srcMainResources = srcMainResources;
	}

	public List<String> getSrcTestJavas() {
		if(srcTestJavas==null)srcTestJavas = new ArrayList<String>();
		return srcTestJavas;
	}

	public void setSrcTestJavas(List<String> srcTestJavas) {
		this.srcTestJavas = srcTestJavas;
	}

	public List<String> getSrcTestResources() {
		if(srcTestResources==null)srcTestResources = new ArrayList<String>();
		return srcTestResources;
	}

	public void setSrcTestResources(List<String> srcTestResources) {
		this.srcTestResources = srcTestResources;
	}

	public boolean isParent(){
		if(this.childs!=null && this.childs.size()>0)return true;
		return false;
	}
	
	public boolean isChild(){
		if(this.parent!=null)return true;
		return false;
	}

	public List<Dependency> getSomeProjectDependencies() {
		return someProjectDependencies;
	}

	public void setSomeProjectDependencies(List<Dependency> someProjectDependencies) {
		this.someProjectDependencies = someProjectDependencies;
	}

	public ModuleType getType() {
		return type;
	}

	public void setType(ModuleType type) {
		this.type = type;
	}
	
	
	//领域行为
	
	public List<MavenProject> getImplProjects(){
		List<MavenProject> impls = new ArrayList<MavenProject>();
		 for(MavenProject project:this.childs){
			 if(project.getType().equals(ModuleType.Impl))
				 impls.add(project);
		 }
		return impls;
	}
	
	public List<String> getBizModelProjects(){
		List<String> impls = new ArrayList<String>();
		 for(MavenProject project:this.childs){
			 if(project.getType().equals(ModuleType.BizModel))
				 impls.add(project.getName());
		 }
		return impls;
	}
	
	public List<MavenProject> getConfigProjects(){
		List<MavenProject> impls = new ArrayList<MavenProject>();
		 for(MavenProject project:this.childs){
			 if(project.getType().equals(ModuleType.conf))
				 impls.add(project);
		 }
		return impls;
	}
	
	public List<MavenProject> getWarProjects(){
		List<MavenProject> impls = new ArrayList<MavenProject>();
		 for(MavenProject project:this.childs){
			 if(project.getType().equals(ModuleType.War))
				 impls.add(project);
		 }
		return impls;
	}
	
	public List<MavenProject> getIntrefaceProjects(){
		List<MavenProject> interfaces = new ArrayList<MavenProject>();
		 for(MavenProject project:this.childs){
			 if(project.getType().equals(ModuleType.Application))
				 interfaces.add(project);
		 }
		return interfaces;
	}
	
	public MavenProject clone(){
		MavenProject project = null;
		try {
			project = (MavenProject)BeanUtils.cloneBean(this);
			List<MavenProject> childs = new ArrayList<MavenProject>();
			for(MavenProject child:this.childs){
				childs.add((MavenProject)BeanUtils.cloneBean(child));
			}
			project.childs = childs;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return project;
	}
	
	public String getDbType(){
		return properties.get("dbProtocol");
	}
	
	/**
	 * 返回当前项目使用的MVC框架，
	 * SpringMVC 表示使用的Spring mvc框架
	 * Struts2MVC 表示使用的是Struts2MVC框架
	 * @return
	 */
	public String getMvcType(){
		return properties.get("mvcProtocol");
	}
}