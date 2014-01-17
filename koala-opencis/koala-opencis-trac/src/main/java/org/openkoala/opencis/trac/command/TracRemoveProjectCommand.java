package org.openkoala.opencis.trac.command;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.SSHConnectConfig;

/**
 * Trac移除项目命令
 * @author zjh
 *
 */
public class TracRemoveProjectCommand extends TracCommand {

	public TracRemoveProjectCommand() {
		// TODO Auto-generated constructor stub
	}
	
	public TracRemoveProjectCommand(SSHConnectConfig configuration, Project project) {
		super(configuration, project);
	}
	
	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		String removeProjectCommand = "rm -rf " + project.getPhysicalPath();
		return removeProjectCommand;
	}

}
