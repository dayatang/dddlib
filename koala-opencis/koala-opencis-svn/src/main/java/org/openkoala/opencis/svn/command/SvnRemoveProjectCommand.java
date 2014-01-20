package org.openkoala.opencis.svn.command;

import java.io.IOException;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.exception.RemoveProjectException;
import org.openkoala.opencis.support.SSHConnectConfig;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

/**
 * svn创建项目命令类
 */
public class SvnRemoveProjectCommand extends SvnCommand {

    public SvnRemoveProjectCommand() {

    }

    public SvnRemoveProjectCommand(SSHConnectConfig configuration, Project project) {
        super(configuration, project);
    }

    @Override
    public String getCommand() {
        String removeProjectCommand = "rm -rf " + project.getPhysicalPath() + project.getProjectName();
        return removeProjectCommand;
    }

    @Override
    public void doWork(Connection connection, Session session) {
    	try {
			String stderr = readOutput(session.getStderr());
			if( !stderr.contains("Adding password for user")){
				throw new RemoveProjectException("创建用户失败！");
			}
		} catch (IOException e) {
			throw new RuntimeException("删除项目" + project.getPhysicalPath() + project.getProjectName()
					+ "发生异常，原因：" + e.getMessage(),e);
		}
    }
}
