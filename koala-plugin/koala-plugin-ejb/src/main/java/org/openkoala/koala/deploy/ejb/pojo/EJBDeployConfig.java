package org.openkoala.koala.deploy.ejb.pojo;

import java.io.Serializable;

import org.openkoala.koala.exception.KoalaException;

/**
 * 
 * 
 * @description EJB自动部署的Config类
 *  
 * @date：       2013-8-26   
 * 
 * @version     Copyright (c) 2013 Koala All Rights Reserved
 * 
 * @author      lingen.liu  <a href=mailto:lingen.liu@gmail.com">lingen.liu@gmail.com</a>
 */
public class EJBDeployConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5175052970919444264L;
	
	/**
	 *  默认IP
	 */
	private static final String DEFAULT_IP = "127.0.0.1";
	
	
	/**
	 * 默认端口
	 */
	private static final String DEFAULT_PORT = "1099";
	
	/**
	 * 默认服务器
	 */
	private static final String DEFAULT_SERVER = "JBoss-EAP-4";
	
	/**
	 * 一键WAR+EJB的项目所在路径
	 */
	private static final String PARAM_FILE="-Dfile";
	
	/**
	 * 指定EAR放置的服务器IP
	 */
	private static final String PARAM_IP="-Dip";
	
	/**
	 * 指定提供EJB服务的服务器端口
	 */
	private static final String PARAM_PORT="-Dport";
	
	private static final String PARAM_SERVER="-Dserver";
	
	private static final String PARAMS_PROFILE="-Dprofile";
	
	public static EJBDeployConfig getEJBDeployConfig(String args[]){
		
		if(args.length==0){
			throw new KoalaException("参数指定不全，调用方式如：java -jar koala-ejb.jar -Dfile=F:/workspace/Koala/tmp/demo -Dip=127.0.0.1 -Dport=1099");
		}
		EJBDeployConfig config = new EJBDeployConfig(DEFAULT_IP,DEFAULT_PORT,DEFAULT_SERVER);
		for(String arg:args){
			if(arg.startsWith(PARAM_FILE)){
				config.filePath = arg.substring(arg.indexOf(PARAM_FILE)+PARAM_FILE.length()+1);
			}
			if(arg.startsWith(PARAM_IP)){
				config.ip = arg.substring(arg.indexOf(PARAM_IP)+PARAM_IP.length()+1);
			}
			if(arg.startsWith(PARAM_PORT)){
				config.port = arg.substring(arg.indexOf(PARAM_PORT)+PARAM_PORT.length()+1);
			}
			if(arg.startsWith(PARAMS_PROFILE)){
				config.profile = arg.substring(arg.indexOf(PARAMS_PROFILE)+PARAMS_PROFILE.length()+1);
			}
			
			//当前不支持此参数，只支持JBOSS_EAP_4服务器
//			if(arg.startsWith(PARAM_SERVER)){
//				config.server = arg.substring(arg.indexOf(PARAM_SERVER)+PARAM_SERVER.length()+1);
//			}
		}
		return config;
	}
	
	public static void main(String args[]){
		String arg ="-Dip=127.0.0.1";
		System.out.println(arg.substring(arg.indexOf(PARAM_IP)+PARAM_IP.length()+1));
	}
	
	/**
	 * 返回一个默认的配置
	 * @return
	 */
	public static EJBDeployConfig getDefaultEJBDeployConfig(){
		EJBDeployConfig config = new EJBDeployConfig(DEFAULT_IP,DEFAULT_PORT,DEFAULT_SERVER);
		return config;
	}

	private String ip;
	
	private String port;
	
	private String server;
	
	private String filePath;
	
	private String profile;
	
	
	public EJBDeployConfig() {
		super();
	}

	public EJBDeployConfig(String ip, String port, String server) {
		super();
		this.ip = ip;
		this.port = port;
		this.server = server;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}
	
	
}
