package org.openkoala.opencis.support;

import org.openkoala.opencis.support.SSHConnectConfig;

/**
 * SVN的配置类
 * @author zjh
 *
 */
public class SvnConfig extends SSHConnectConfig {

	private String svnAddress; 
	
	public SvnConfig() {
		// TODO Auto-generated constructor stub
	}

	public SvnConfig(String host, String username, String password,String storePath) {
		super(host, username, password, storePath);
		
	}
	
	public SvnConfig(String host, String username, String password,String storePath,String svnAddress) {
		super(host, username, password, storePath);
		this.svnAddress = svnAddress;
	}

	public String getSvnAddress() {
		return svnAddress;
	}
	
	public void setSvnAddress(String svnAddress) {
		this.svnAddress = svnAddress;
	}

}
