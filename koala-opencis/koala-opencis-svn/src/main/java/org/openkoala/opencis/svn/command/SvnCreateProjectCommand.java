package org.openkoala.opencis.svn.command;


import org.openkoala.opencis.api.Project;

import com.dayatang.configuration.Configuration;

/**
 * svn创建项目命令类
 */
public class SvnCreateProjectCommand extends SvnCommand {

	public SvnCreateProjectCommand() {
		
	}
	
	public SvnCreateProjectCommand(Configuration configuration, Project project) {
		super(configuration, project);
	}

	@Override
	public String getCommand() {
		String createProjectCommand = "svnadmin create /var/www/svn/" + project.getProjectName();
		return createProjectCommand;
	}
}
