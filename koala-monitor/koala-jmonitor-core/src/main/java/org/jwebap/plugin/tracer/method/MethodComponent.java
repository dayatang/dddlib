package org.jwebap.plugin.tracer.method;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jwebap.core.ComponentContext;
import org.jwebap.core.TraceLiftcycleManager;
import org.jwebap.plugin.analyser.CommonAnalyser;
import org.jwebap.plugin.tracer.AbstractComponent;
import org.jwebap.toolkit.bytecode.ClassEnhancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.twdata.pkgscanner.PackageScanner;

/**
 * @className MethodComponent:类监控组件
 * 
 * 可以对类，对包进行监控
 * 
 * @link TraceMethodHandle
 * @author leadyu
 * @since Jwebap 0.5
 * @date  2007-8-14
 */
public class MethodComponent extends AbstractComponent{
	private static final Logger log = LoggerFactory.getLogger(MethodComponent.class);
	private ComponentContext componentContext=null;
	
	private CommonAnalyser commonAnalyser;
	
    public static String TRACE_TYPE = "METHOD";
	
	public void setTraceType(String traceType){
		TRACE_TYPE = traceType;
	}
	
	
	public void startup(ComponentContext context) {
		componentContext=context;
		TraceLiftcycleManager container = componentContext.getContainer();
		commonAnalyser = new CommonAnalyser(TRACE_TYPE);
		container.registerAnalyser(TRACE_TYPE, commonAnalyser);
		
		String packages = context.getProperty("detect-packages");
		String clazzs = context.getProperty("detect-clazzs");
		List<String> classNames=getClassNamePatterns(packages,clazzs);
		//inject trace
		injectClass(classNames);
		log.info("methodcomponent startup.");
	}
	
	private List<String> getClassNamePatterns(String packages,String clazzs){
		List<String> list = new ArrayList<String>();
		
		if (clazzs != null && !"".equals(clazzs)) {
			clazzs=clazzs.replaceAll("\n\r\t\\s","");
			list.addAll(Arrays.asList(clazzs.split(";")));
		}
		
		if (packages != null && !"".equals(packages)) {
			packages=packages.replaceAll("\n\r\t\\s+","");
			
			String[] packagePatterns = packages.split(";");
	    	Collection<String> cls = PackageScanner.scanMatchPackages(packagePatterns);
	    	
	    	if(cls == null || cls.isEmpty()){
				log.warn("在包[{}]下未扫描到任何类",packages);
			}else{
				for (String s : cls) {
		    		s =  s.substring(0,s.lastIndexOf(".")).replace("/", ".");
		    		if(s.contains("$") || s.endsWith("Exception"))continue;
					if(!list.contains(s))list.add(s);
				}
			}
		}
		
		return list;
	}
	
	private void injectClass(List<String> resourceNames) {	
		ClassEnhancer enhancer = new ClassEnhancer();
//		PackageEnhancer pEnhancer = new PackageEnhancer();
		TraceLiftcycleManager container=getComponentContext().getContainer();
		
		String clazz;
		for (String resource : resourceNames) {
			clazz = resource;
			try {
				enhancer.createClass(clazz,new TraceMethodHandle(container));
				if(log.isDebugEnabled())log.debug("对["+clazz +"]监听部署完成");
			} catch (Exception e) {
				if(!ignoreClassMonitor(clazz)){
					log.warn("对类[" + clazz + "]监听部署失败，不过这并不影响系统的运行。");
				}
			}
		}
	}

	public void destory() {
		
	}

	public void clear() {
	}

	public ComponentContext getComponentContext() {
		return componentContext;
	}
	
	/**
	 * 判断是否跳过监听
	 * @param className
	 * @return
	 */
	private boolean ignoreClassMonitor(String className){

		try {
			Class<?> clazz = Class.forName(className);
			String classAsString = clazz.toString().trim();
			//interface
			if(classAsString.startsWith("interface"))return true;
			//protected
			//public static 
			int publicMethodCount = 0;
			Method[] methods = clazz.getDeclaredMethods();
			for (Method method : methods) {
				String mod = Modifier.toString(method.getModifiers());
				if("public".equals(mod))publicMethodCount++;
			}
			return publicMethodCount==0;
		} catch (Exception e) {
			
		}
	
		return true;
	}


	public CommonAnalyser getCommonAnalyser() {
		return commonAnalyser;
	}
}
