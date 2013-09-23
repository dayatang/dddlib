package org.openkoala.opencis.trac.command;

import org.openkoala.opencis.api.Project;

import com.dayatang.configuration.Configuration;

/**
 * Trac创建项目命令类
 * @author 赵健华
 * 2013-9-23 下午7:11:46
 */
public class TracCreateProjectCommand extends TracCommand {

	public TracCreateProjectCommand() {
		
	}
	
	public TracCreateProjectCommand(Configuration configuration, Project project) {
		super(configuration, project);
	}

	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		String createProjectCommand = "trac-admin " + project.getProjectPath() + " initenv " + project.getArtifactId() + " sqlite:db/trac.db";
		return createProjectCommand;
	}
}
