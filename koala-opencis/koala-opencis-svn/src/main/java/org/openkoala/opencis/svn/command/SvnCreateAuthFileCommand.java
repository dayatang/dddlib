package org.openkoala.opencis.svn.command;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.SSHConnectConfig;

public class SvnCreateAuthFileCommand extends SvnCommand {

	public SvnCreateAuthFileCommand() {
		// TODO Auto-generated constructor stub
	}

	public SvnCreateAuthFileCommand(SSHConnectConfig config, Project project) {
		super(config, project);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		String strCommand = "touch " + storePath + "authz"; 
		return strCommand;
	}

}
