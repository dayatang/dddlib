package org.openkoala.opencis.trac.command;

import org.openkoala.opencis.api.Project;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

import org.openkoala.opencis.support.CommonUtil;
import org.openkoala.opencis.support.SSHCommand;
import org.openkoala.opencis.support.SSHConnectConfig;

/**
 * 命令模式之余，此抽象类又是子类的模板，子类只需要实现对应的抽象方法即可
 *
 * @author 赵健华
 *         2013-9-23 下午7:10:32
 */
public abstract class TracCommand extends SSHCommand {
	
	protected static final String PERMISSION = "TRAC_ADMIN";
	
	protected static final String KEY_PATH = "TRAC_PROJECT_PATH";

    public TracCommand() {
        // TODO Auto-generated constructor stub
        super();
    }

    public TracCommand(SSHConnectConfig configuration, Project project) {
        this.host = configuration.getHost();
        this.userName = configuration.getUsername();
        this.password = configuration.getPassword();
        this.storePath = CommonUtil.validatePath(configuration.getStorePath());
        this.project = project;

    }
}
