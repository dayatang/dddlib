package org.openkoala.opencis.svn.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.openkoala.opencis.api.Project;
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
        this.host = config.getHost();
        this.userName = config.getUsername();
        this.password = config.getPassword();
        this.project = project;

    }

    protected String readOutput(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
}
