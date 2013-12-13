package org.openkoala.opencis.svn.command;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.api.SSHCommand;

import com.dayatang.configuration.Configuration;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

/**
 * 命令模式之余，此抽象类又是子类的模板，子类只需要实现对应的抽象方法即可
 */
public abstract class SvnCommand extends SSHCommand {
	
	public SvnCommand() {
		super();
	}
	
	public SvnCommand(Configuration configuration,Project project) {
		this.host = configuration.getString("HOST");
		this.userName = configuration.getString("USER");
		this.password = configuration.getString("PASSWORD");
		this.project = project;
		
	}
	
	@Override
	public void doWork(Connection connection, Session session) {
		// TODO Auto-generated method stub
		
	}
}
