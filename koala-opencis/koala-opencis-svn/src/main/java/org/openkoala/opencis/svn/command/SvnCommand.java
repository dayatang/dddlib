package org.openkoala.opencis.svn.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.CommonUtil;
import org.openkoala.opencis.support.SSHCommand;
import org.openkoala.opencis.support.SSHConnectConfig;

/**
 * 命令模式之余，此抽象类又是子类的模板，子类只需要实现对应的抽象方法即可
 */
public abstract class SvnCommand extends SSHCommand {

    public SvnCommand() {
        super();
    }

    public SvnCommand(SSHConnectConfig config, Project project) {
        super(config, project);
        this.storePath = CommonUtil.validatePath(config.getStorePath());

    }
}
