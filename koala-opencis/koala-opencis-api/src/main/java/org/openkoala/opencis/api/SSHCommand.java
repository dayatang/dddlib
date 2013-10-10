package org.openkoala.opencis.api;

import java.io.IOException;

import com.dayatang.configuration.Configuration;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

/**
 * 抽象的SSH命令类
 * @author 赵健华
 * 2013-9-26 下午4:01:14
 */
public abstract class SSHCommand implements Command {

	protected Project project = null;
	
	protected String host;
	
	protected String userName;
	
	protected String password;
	
	public SSHCommand() {
		// TODO Auto-generated constructor stub
		
	}

	public SSHCommand(Configuration configuration,Project project) {
		this.host = configuration.getString("HOST");
		this.userName = configuration.getString("USER");
		this.password = configuration.getString("PASSWORD");
		this.project = project;
		
	}
	
	/**
	 * 模板方法
	 * @throws Exception 
	 */
	@Override
	public final void execute() throws Exception {
		// TODO Auto-generated method stub
		Connection connection = null;
		Session session = null;
		connection = new Connection(host);
		try {
			connection.connect();
			boolean isAuthenticated = connection.authenticateWithPassword(userName, password);
			if (isAuthenticated == false){
				throw new IOException("Authentication failed.");
			}
			session = connection.openSession();
			session.execCommand(getCommand());
			//抽象方法
			doWork(connection, session);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("无法执行命令：" + getCommand());
		}finally{
			session.close();
			connection.close();
		}
	}
	
	public abstract String getCommand();

	public abstract void doWork(Connection connection,Session session);
}
