package org.openkoala.opencis.svn.command;

import java.io.File;
import java.io.IOException;

import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.exception.CreateUserFailedException;
import org.openkoala.opencis.support.OpencisConstant;
import org.openkoala.opencis.support.SSHConnectConfig;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

/**
 * svn分配用户到某个角色
 */
public class SvnCreateUserCommand extends SvnCommand {

	private Developer developer;
	
	public SvnCreateUserCommand() {
		
	}
	
	public SvnCreateUserCommand(Developer developer, SSHConnectConfig configuration, Project project){
		super(configuration, project);
		this.developer = developer;
	}
	
	@Override
	public String getCommand() {
		String filePath = storePath + "passwd";

		String createUserCommand = "htpasswd -b " +  filePath + " " + developer.getId() + " " 
								+ developer.getPassword();
		return createUserCommand;
	}

	@Override
	public void doWork(Connection connection, Session session) {
		try {
			String stderr = readOutput(session.getStderr());
			if( !stderr.contains("Adding password for user")){
				throw new CreateUserFailedException("创建用户失败！");
			}
		} catch (IOException e) {
			throw new RuntimeException("为项目" + project.getProjectName() + "创建用户" + 
							developer.getId() + "和密码" + developer.getPassword() + "时异常！");
		}
	}

}
