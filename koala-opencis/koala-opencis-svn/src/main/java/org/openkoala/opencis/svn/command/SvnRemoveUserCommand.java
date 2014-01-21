package org.openkoala.opencis.svn.command;

import java.io.IOException;

import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.exception.RemoveUserException;
import org.openkoala.opencis.support.SSHConnectConfig;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

/**
 * SVN删除用户命令
 * @author zjh
 *
 */
public class SvnRemoveUserCommand extends SvnCommand {

	private Developer developer;
	
	public SvnRemoveUserCommand() {
		// TODO Auto-generated constructor stub
	}
	
	public SvnRemoveUserCommand(Developer developer, SSHConnectConfig configuration, Project project) {
        super(configuration, project);
        this.developer = developer;
    }

	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		String removeUserCommand = "htpasswd -D " + storePath 
				+ project.getProjectName() + "/conf/passwd " + developer.getId();
        return removeUserCommand;
	}
	
	@Override
	public void doWork(Connection connection, Session session) {
		// TODO Auto-generated method stub
		try {
			String stderr = readOutput(session.getStderr());
			if( !stderr.contains("Deleting password for user")){
				throw new RemoveUserException("创建用户失败！");
			}
		} catch (IOException e) {
			throw new RuntimeException("为项目" + project.getProjectName() + "创建用户" + 
					developer.getId() + "和密码" + developer.getPassword() + "时异常！，原因：" + e.getMessage());
		}
	}

}
