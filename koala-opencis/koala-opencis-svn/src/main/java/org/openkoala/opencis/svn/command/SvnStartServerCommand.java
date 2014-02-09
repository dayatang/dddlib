package org.openkoala.opencis.svn.command;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.SSHConnectConfig;

/**
 * 远程启动Svnserve命令
 * @author zjh
 *
 */
public class SvnStartServerCommand extends SvnCommand {

	public SvnStartServerCommand() {
		// TODO Auto-generated constructor stub
	}

	public SvnStartServerCommand(SSHConnectConfig config, Project project) {
		super(config, project);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		String strCmd = "svnserve -d -r " + storePath;
		return strCmd;
	}

}
