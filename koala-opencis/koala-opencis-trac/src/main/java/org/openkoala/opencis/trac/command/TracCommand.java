package org.openkoala.opencis.trac.command;

import java.io.IOException;

import org.openkoala.opencis.api.Command;
import org.openkoala.opencis.api.Project;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

/**
 * 命令模式之余，此抽象类又是子类的模板，子类只需要实现对应的抽象方法即可
 * @author 赵健华
 * 2013-9-23 下午7:10:32
 */
public abstract class TracCommand implements Command {

	protected Connection connection = null;
	
	protected Project project = null;
	
	private String host;
	
	private String userName;
	
	private String password;
	
	public TracCommand() {
		// TODO Auto-generated constructor stub
		
	}

	public TracCommand(String host, String userName, String password,Project project) {
		this.host = host;
		this.userName = userName;
		this.password = password;
		this.project = project;
		connection = new Connection(host);
	}
	
	/**
	 * 模板方法
	 */
	@Override
	public final void execute() {
		// TODO Auto-generated method stub
		Session session = null;
		try {
			connection.connect();
			boolean isAuthenticated = connection.authenticateWithPassword("root", "password");
			if (isAuthenticated == false){
				throw new IOException("Authentication failed.");
			}
			session = connection.openSession();
			session.execCommand(getCommand());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			session.close();
			connection.close();
		}
	}
	
	public abstract String getCommand();
}
