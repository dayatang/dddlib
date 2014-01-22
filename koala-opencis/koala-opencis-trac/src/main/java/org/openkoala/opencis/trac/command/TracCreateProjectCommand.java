package org.openkoala.opencis.trac.command;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.SSHConnectConfig;

/**
 * Trac创建项目命令类
 * @author 赵健华
 * 2013-9-23 下午7:11:46
 */
public class TracCreateProjectCommand extends TracCommand {

	public TracCreateProjectCommand() {
		
	}
	
	public TracCreateProjectCommand(SSHConnectConfig configuration, Project project) {
		super(configuration, project);
	}

	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		String createProjectCommand = "trac-admin " + storePath + project.getProjectName() + 
                "/ initenv " + project.getArtifactId() + " sqlite:db/trac.db";
		return createProjectCommand;
	}
}
