package org.openkoala.opencis.trac.command;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.api.SSHCommand;

import com.dayatang.configuration.Configuration;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

/**
 * 命令模式之余，此抽象类又是子类的模板，子类只需要实现对应的抽象方法即可
 * @author 赵健华
 * 2013-9-23 下午7:10:32
 */
public abstract class TracCommand extends SSHCommand {
	
	public TracCommand() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public TracCommand(Configuration configuration,Project project) {
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
