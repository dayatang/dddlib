package org.openkoala.koala.pojo;

/**
 * 指明项目属于哪一层
 * @author lingen
 *
 */
public enum ModuleType {

	Project,//父项目
	
	BizModel,//领域层项目
	
	Infra,//基础设施层项目
	
	Application,//应用层
	
	Impl,//实现层
	
	War,//WEB层
	
	Ear,//EAR层
	
	conf,//配置层
	
	conf_war,//WAR的配置
	
	Other,//其它无法归类
	
}
