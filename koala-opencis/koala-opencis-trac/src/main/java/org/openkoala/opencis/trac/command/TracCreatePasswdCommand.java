package org.openkoala.opencis.trac.command;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.SSHConnectConfig;


/**
 * 创建passwd文件命令
 * @author zjh
 *
 */
public class TracCreatePasswdCommand extends TracCommand {

	public TracCreatePasswdCommand() {
		// TODO Auto-generated constructor stub
	}
	
	public TracCreatePasswdCommand(SSHConnectConfig configuration, Project project) {
		super(configuration, project);
	}
	
	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		String strCommand = "touch " + storePath + "passwd"; 
		return strCommand;
	}

}
