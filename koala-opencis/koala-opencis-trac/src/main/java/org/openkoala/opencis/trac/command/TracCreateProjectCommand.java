package org.openkoala.opencis.trac.command;

/**
 * Trac创建项目命令类
 * @author 赵健华
 * 2013-9-23 下午7:11:46
 */
public class TracCreateProjectCommand extends TracCommand {

	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		String createProjectCommand = "trac-admin " + project.getProjectPath() + " initenv " + project.getArtifactId() + " sqlite:db/trac.db";
		return createProjectCommand;
	}
}
