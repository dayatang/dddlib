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
public class SvnRemoveProjectCommand extends SvnCommand {

    public SvnRemoveProjectCommand() {

    }

    public SvnRemoveProjectCommand(SSHConnectConfig configuration, Project project) {
        super(configuration, project);
    }

    @Override
    public String getCommand() {
        String removeProjectCommand = "rm -rf " + project.getProjectPath() + project.getProjectName();
        return removeProjectCommand;
    }

    @Override
    public void doWork(Connection connection, Session session) {

    }
}
