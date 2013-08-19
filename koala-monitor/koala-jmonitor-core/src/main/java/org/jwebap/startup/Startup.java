package org.jwebap.startup;

import java.util.Map;

import org.jwebap.cfg.persist.PersistManager;
import org.jwebap.core.RuntimeContext;
import org.openkoala.koala.monitor.jwebap.NodeDef;

/**
 * Jwebap启动类
 * 
 * @author leadyu
 * @since Jwebap 0.5
 * @date 2007-8-16
 */
public class Startup {
	
	public static void startup(String configPath,Map<String, String> serverInfos)  {
		NodeDef config = null;
		PersistManager defManager=new PersistManager(configPath);
		
		try {
			config = defManager.get();
		} catch (Exception e) {
			throw new JwebapInitialException("",e);
		}
		

		RuntimeContext context = RuntimeContext.registerContext(config,defManager);
		context.registerServerInfos(serverInfos);
		context.startup();
	}
	
	
}
