package org.openkoala.opencis.svn.command;

import org.openkoala.opencis.api.OpencisConstant;
import org.openkoala.opencis.api.Project;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import org.openkoala.opencis.api.SSHConnectConfig;

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
        String removeProjectCommand = "rm -rf " + OpencisConstant.PROJECT_PATH_IN_LINUX_SVN + project.getProjectName();
        return removeProjectCommand;
    }

    @Override
    public void doWork(Connection connection, Session session) {

    }
}
