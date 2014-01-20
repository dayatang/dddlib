package org.openkoala.opencis.trac.command;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.SSHConnectConfig;

/**
 * 移除用户(角色)命令
 * @author zjh
 *
 */
public class TracRemoveRoleCommand extends TracCommand {
	
	private String roleName;

	public TracRemoveRoleCommand() {
		// TODO Auto-generated constructor stub
	}
	
	public TracRemoveRoleCommand(SSHConnectConfig configuration,String roleName, Project project) {
		super(configuration, project);
		this.roleName = roleName;
	}
	
	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		String removeRoleCommand = "trac-admin " + storePath + project.getArtifactId() 
				+ " permission remove " + roleName + " " + PERMISSION;
        return removeRoleCommand;
	}

}
