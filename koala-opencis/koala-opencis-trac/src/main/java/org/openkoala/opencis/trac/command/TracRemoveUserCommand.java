package org.openkoala.opencis.trac.command;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.SSHConnectConfig;

/**
 * 移除用户命令
 * @author zjh
 *
 */
public class TracRemoveUserCommand extends TracCommand {

	public TracRemoveUserCommand() {
		// TODO Auto-generated constructor stub
	}
	
	public TracRemoveUserCommand(SSHConnectConfig configuration, Project project) {
		super(configuration, project);
	}
	
	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		return null;
	}

}
