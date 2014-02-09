package org.openkoala.koala.monitor.core;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.vfs.VirtualFile;


/**
 * 
 * 功能描述：<br />
 *  
 * 创建日期：2013-7-12 下午2:40:40  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明
 */
public class KoalaMonitorListener implements ServletContextListener {
	
	private static final Log log = LogFactory.getLog(KoalaMonitorListener.class);
	
	public static final String CONFIG_PARAM_NAME="monitor-config";
	
	public void contextInitialized(ServletContextEvent contextEnvent) {
		ServletContext servletContext=contextEnvent.getServletContext();
		String configPath = servletContext.getInitParameter(CONFIG_PARAM_NAME);

		try {
			String path = null;
			if(configPath.startsWith("classpath")){
				configPath = configPath.replace("classpath", "").replace("*:", "");
				URL url = Thread.currentThread().getContextClassLoader().getResource(configPath);
				path = url.getFile();
				
				if(url.getProtocol().equals("vfs")){
					 VirtualFile vf = (VirtualFile)url.getContent();
					 path = vf.getPhysicalFile().getPath();
				}
			}else{
				path=servletContext.getRealPath(configPath);
				if(path==null){
					URL url = servletContext.getResource(configPath);
					path = url.getFile();
				}
			}
			
			Map<String, String> serverInfos = new HashMap<String, String>();
			serverInfos.put("serverName", servletContext.getServerInfo());
			serverInfos.put("deployPath", servletContext.getRealPath("/"));
			//
			System.out.println("monitor config file:" + path);
			ContextStartup.startup(path,serverInfos);
		} catch (Exception e) {
			log.warn(e.getMessage());
			e.printStackTrace();
		}
		
	}

	public void contextDestroyed(ServletContextEvent arg0) {
		
	}
}
