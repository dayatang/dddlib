package org.openkoala.opencis.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.exception.CreateUserFailedException;

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
	
	protected String storePath;
	
	public SSHCommand() {
		// TODO Auto-generated constructor stub
		
	}

	public SSHCommand(SSHConnectConfig config,Project project) {
		this.host = config.getHost();
		this.userName = config.getUsername();
		this.password = config.getPassword();
		this.project = project;
		
	}
	
	/**
	 * 模板方法
	 * @throws Exception 
	 */
	@Override
	public final void execute() throws Exception {
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
			throw new Exception("无法执行命令：" + getCommand());
		}finally{
			session.close();
			connection.close();
		}
	}
	
	public abstract String getCommand();

	/**
	 * 查看输出
	 * @param connection
	 * @param session
	 */
	public void doWork(Connection connection,Session session){
		try {
			String stderr = readOutput(session.getStderr());
			System.out.println(stderr);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
