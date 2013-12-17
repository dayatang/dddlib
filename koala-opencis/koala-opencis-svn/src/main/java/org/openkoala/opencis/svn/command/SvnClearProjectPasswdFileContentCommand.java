package org.openkoala.opencis.svn.command;

import org.openkoala.opencis.api.OpencisConstant;
import org.openkoala.opencis.api.Project;

import com.dayatang.configuration.Configuration;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

/**
 * svn创建项目命令类
 */
public class SvnClearProjectPasswdFileContentCommand extends SvnCommand {

	public SvnClearProjectPasswdFileContentCommand() {
		
	}
	
	public SvnClearProjectPasswdFileContentCommand(Configuration configuration, Project project) {
		super(configuration, project);
	}

	@Override
	public String getCommand() {
		String clearProjectPasswdFileContentCommand = "echo \"\" > " + OpencisConstant.PROJECT_PATH_IN_LINUX_SVN + project.getProjectName() +"/conf/passwd";
		return clearProjectPasswdFileContentCommand;
	}

	@Override
	public void doWork(Connection connection, Session session) {
		
	}
}
