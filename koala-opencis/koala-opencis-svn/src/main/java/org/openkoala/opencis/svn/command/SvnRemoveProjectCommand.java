package org.openkoala.opencis.svn.command;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.svn.ProjectExistenceException;

import com.dayatang.configuration.Configuration;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

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
		String removeProjectCommand = "rm -rf /var/www/svn/" + project.getProjectName();
		return removeProjectCommand;
	}

	@Override
	public void doWork(Connection connection, Session session) {
		
	}
}
