package org.openkoala.opencis.trac.command;

import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.SSHConnectConfig;

/**
 * Trac删除用户密码
 * @author zjh
 *
 */
public class TracRemoveUserCommand extends TracCommand {

	private Developer developer;
	
	public TracRemoveUserCommand() {
		// TODO Auto-generated constructor stub
	}
	
	public TracRemoveUserCommand(Developer developer, SSHConnectConfig configuration, Project project) {
		// TODO Auto-generated constructor stub
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

}
