package org.openkoala.opencis.support;

import org.openkoala.opencis.support.SSHConnectConfig;

/**
 * SVN的配置类
 * @author zjh
 *
 */
public class SvnConfig extends SSHConnectConfig {

	private String svnAddress; 
	private String svnUser;
	private String svnPassword;
	
	public SvnConfig() {
		// TODO Auto-generated constructor stub
	}

	public SvnConfig(String host, String username, String password,String storePath) {
		super(host, username, password, storePath);
		
	}
	
	/**
	 * SVN配置构造函数
	 * @param Linux的SVN服务器IP
	 * @param Linux SSH 用户
	 * @param Linux SSH 密码
	 * @param svncreate命令所在的路径
	 * @param Apache整合SVN所在的URL
	 */
	public SvnConfig(String host, String username, String password,String storePath,
			String svnAddress,String svnUser,String svnPassword) {
		super(host, username, password, storePath);
		this.svnAddress = svnAddress;
		this.svnUser = svnUser;
		this.svnPassword = svnPassword;
	}

	public String getSvnAddress() {
		return svnAddress;
	}
	
	public void setSvnAddress(String svnAddress) {
		this.svnAddress = svnAddress;
	}

	public String getSvnUser() {
		return svnUser;
	}

	public void setSvnUser(String svnUser) {
		this.svnUser = svnUser;
	}

	public String getSvnPassword() {
		return svnPassword;
	}

	public void setSvnPassword(String svnPassword) {
		this.svnPassword = svnPassword;
	}

}
