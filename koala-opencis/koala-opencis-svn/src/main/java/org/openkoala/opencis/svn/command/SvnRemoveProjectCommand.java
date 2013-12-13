package org.openkoala.opencis.svn.command;

import org.openkoala.opencis.api.Project;

import com.dayatang.configuration.Configuration;

/**
 * svn创建项目命令类
 */
public class SvnRemoveProjectCommand extends SvnCommand {

	public SvnRemoveProjectCommand() {
		
	}
	
	public SvnRemoveProjectCommand(Configuration configuration, Project project) {
		super(configuration, project);
	}

	@Override
	public String getCommand() {
		String createProjectCommand = "trac-admin " + project.getProjectPath() + " initenv " + project.getArtifactId() + " sqlite:db/trac.db";
		return createProjectCommand;
	}
}
