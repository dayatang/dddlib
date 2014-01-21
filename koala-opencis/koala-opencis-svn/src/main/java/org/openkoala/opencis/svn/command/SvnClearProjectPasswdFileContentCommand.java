package org.openkoala.opencis.svn.command;

import java.io.File;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.OpencisConstant;
import org.openkoala.opencis.support.SSHConnectConfig;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

/**
 * svn创建项目命令类
 */
public class SvnClearProjectPasswdFileContentCommand extends SvnCommand {

	public SvnClearProjectPasswdFileContentCommand() {
		
	}
	
	public SvnClearProjectPasswdFileContentCommand(SSHConnectConfig config, Project project) {
		super(config, project);
	}

	@Override
	public String getCommand() {
		String clearProjectPasswdFileContentCommand = "echo \"\" > " + storePath  +"passwd";
		return clearProjectPasswdFileContentCommand;
	}

	@Override
	public void doWork(Connection connection, Session session) {
		
	}
}
