package org.openkoala.koala.monitor.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openkoala.koala.monitor.component.MapCacheDataPool;
import org.openkoala.koala.monitor.def.ComponentDef;
import org.openkoala.koala.monitor.def.DataPolicyDef;
import org.openkoala.koala.monitor.def.NodeDef;
import org.openkoala.koala.monitor.def.TaskDef;
import org.openkoala.koala.monitor.exception.ContextInitialException;
import org.openkoala.koala.monitor.service.remote.RemoteDataPolicyClientHandler;
import org.openkoala.koala.monitor.task.ServerStatusCollectorTask;
import org.openkoala.koala.monitor.task.ServiceConnectionCheckTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 功能描述：监控上下文<br />
 *  
 * 创建日期：2013-6-25 上午11:29:51  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明
 */
public class RuntimeContext extends AbstractContext {

	private static final Logger LOG = LoggerFactory.getLogger(RuntimeContext.class);
	
	private static RuntimeContext context;
	
	private TraceLiftcycleManager _container = null;

	private NodeDef _config=null;
	
	private Map<String,Component> _components = null;
	
	private Map<String,MonitorTask> _monitorTasks = null;
	

	private PersistManager _defManager=null;
	
	//监控数据缓存
	private CacheDataPool dataCache;

	private RuntimeContext(NodeDef config,TraceLiftcycleManager container,PersistManager defManager) {
		_components = new HashMap<String,Component>();
		_monitorTasks = new HashMap<String, MonitorTask>();
		_container = container;
		_config=config;
		_defManager=defManager;
		dataCache = new MapCacheDataPool(_config.getMaxCacheSize(), _config.getCacheExpireTime());
		LOG.info("当前监控配置为：\n"+config.toString());
	}
	
	public static RuntimeContext registerContext(NodeDef config,PersistManager defManager){
		if(context == null){
			context = new RuntimeContext(config,new TraceContainer(), defManager);
		}
		return context;
	}

	public static RuntimeContext getContext() {
		if(context == null){
			throw new ContextInitialException("jwebap not startup successfully,please check the application log.");
		}
		return context;
	}
	

	public CacheDataPool getDataCache() {
		return dataCache;
	}

	/**
	 * 返回当前实例配置
	 * @return
	 */
	public NodeDef getNodeDef(){
		return _config;
	}
	
	/**
	 * 返回jwebap配置管理器
	 * @return
	 */
	public PersistManager getDefManager(){
		return _defManager;
	}
	
	public String getServerName() {
		return getProperty("serverName");
	}

	public String getDeployPath() {
		return getProperty("deployPath");
	}
	
	public void startup(){
		Collection<ComponentDef> components = _config.getComponents();
		Iterator<ComponentDef> componentIt=components.iterator();
		/**
		 * 注册Components
		 */
		while (componentIt.hasNext()) {			
			ComponentDef def = componentIt.next();
			//未激活则不启动
            if(!def.isActive())continue;
            registerComponent(def.getType(), def);

		}
		LOG.info("component startup finished. register Component Count:"+_components.size());
		
		//注册监控任务
		List<TaskDef> tasks = _config.getTasks();
		if(tasks != null && tasks.size()>0){
			for (TaskDef taskDef : tasks) {
				registerTask(taskDef);
			}
		}
		
		//注册数据同步服务
		registerDataProcessService();
	}

	/**
	 * 注册监控组件
	 * @param type
	 * @param def
	 * @throws RegisterException
	 */
	private void registerComponent(String type, ComponentDef def){
        try {
        	Component component = ComponentFactory.getInstance(type);
    		if(component == null){
    			LOG.warn("[{}][{}]无匹配插件，跳过",def.getName(),type);
    			return ;
    		}
    		
    		ComponentContext context = new StandardComponentContext(getContainer(), this, def);
    		component.startup(context);
    		_components.put(type, component);
    		context.setComponent(component);
    		context.initProperties(def.getProperties());
		} catch (Exception e) {
			e.printStackTrace();
			LOG.warn("[{}][{}]插件启动失败",def.getName(),type);
		}
	}
	
	/**
	 * 注册监控服务
	 * @param task
	 */
	private void registerTask(TaskDef taskdef){
		try {
			if(!taskdef.isActive())return;
			if(ServiceConnectionCheckTask.TASK_KEY.equals(taskdef.getType())){
				MonitorTask task = new ServiceConnectionCheckTask(taskdef);
				task.startup();
				_monitorTasks.put(taskdef.getType(), task);
				LOG.info("[{}][{}]启动 OK",taskdef.getName(),taskdef.getType());
			}else if(ServerStatusCollectorTask.TASK_KEY.equals(taskdef.getType())){
				MonitorTask task = new ServerStatusCollectorTask(taskdef);
				task.startup();
				_monitorTasks.put(taskdef.getType(), task);
				LOG.info("[{}][{}]启动 OK",taskdef.getName(),taskdef.getType());
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.warn("[{}][{}]启动 fail",taskdef.getName(),taskdef.getType());
		}
	}
	
	/**
	 * 注册数据处理服务
	 */
	private void registerDataProcessService(){
		if(_components.size() == 0)return;
		try {
			DataPolicyDef type = getNodeDef().getDataPolicy();
			if(type !=null){
				if("remote".equals(type.getType())){
					new RemoteDataPolicyClientHandler().startup(this);
				}
			}
		} catch (Exception e) {
			LOG.error("监控数据同步服务启动失败",e);
		}
	}
	
	/**
	 * 注册服务器容器信息
	 */
	public void registerServerInfos(Map<String, String> serverInfos){
		initProperties(serverInfos);
	}

	public Component getComponent(String name) {
		return _components.get(name);
	}
	
	public ComponentContext getComponentContext(String name) {
		Component component = _components.get(name);
		return component == null ? null : component.getComponentContext();
	}
	
	public ComponentDef getComponentDef(String name){
		ComponentDef componentDef = getNodeDef().getComponentDef(name);
		return componentDef;
	}

	public void unregisterComponent(String name) {
		Component component =  _components.get(name);
		component.destory();
		_components.remove(name);
	}

	public TraceLiftcycleManager getContainer() {
		return _container;
	}

	public void setContainer(TraceLiftcycleManager container) {
		this._container = container;
	}

	/**
	 * RuntimeContext是组件的上下文的根
	 */
	public Context getParent() {
		return null;
	}
	
	/**
	 * 获取指定组件属性
	 * @param componentName
	 * @param propName
	 * @return
	 */
	public static String getComponentProps(String componentName,String propName){
		ComponentContext componentContext = getContext().getComponentContext(componentName);
		return componentContext == null ? null : componentContext.getProperty(propName);
	}
	
	
	/**
	 * 判断组件是否激活
	 * @param componentName
	 * @return
	 */
	public static boolean componentIsActive(String componentName){
		ComponentDef def = getContext().getComponentDef(componentName);
		if(def == null)return false;
		return def.isActive();
	}
	
	/**
	 * 获取指定监控任务
	 * @param taskKey
	 * @return
	 */
	public MonitorTask getMonitorTask(String taskKey) {
		return _monitorTasks.get(taskKey);
	}
}
