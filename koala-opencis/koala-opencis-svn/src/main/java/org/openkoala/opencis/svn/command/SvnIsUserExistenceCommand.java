package org.openkoala.opencis.svn.command;

import java.io.IOException;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.svn.UserExistenceException;

import com.dayatang.configuration.Configuration;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

/**
 * svn分配用户到某个角色
 */
public class SvnIsUserExistenceCommand extends SvnCommand {

	private String userName;
	
	public SvnIsUserExistenceCommand() {
		// TODO Auto-generated constructor stub
	}
	
	public SvnIsUserExistenceCommand(String userName, Configuration configuration, Project project){
		super(configuration, project);
		this.userName = userName;
	}
	
	@Override
	public String getCommand() {
		String isUserExistenceCommand = "grep -q '" + userName + ":' /var/www/svn/" + project.getProjectName() + "/conf/passwd && echo 'exist' || echo 'not exist'";
		return isUserExistenceCommand;
	}

	@Override
	public void doWork(Connection connection, Session session) {
		try {
			 String stdout = readOutput(session.getStdout());
			if( "exist".equals(stdout.trim()) ){
				throw new UserExistenceException("用户已经存在！");
			}
		} catch (IOException e) {
			throw new RuntimeException("执行检查用户" + userName + "是否存在于项目" + project.getProjectName() + "中时异常！");
		}
	}

}
