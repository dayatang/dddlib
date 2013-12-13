package org.openkoala.opencis.svn.command;

import org.openkoala.opencis.api.Project;

import com.dayatang.configuration.Configuration;

/**
 * svn分配用户到某个角色
 */
public class SvnAssignUserToRoleCommand extends SvnCommand {

	private String usrId;
	
	private String role;
	
	public SvnAssignUserToRoleCommand() {
		// TODO Auto-generated constructor stub
	}
	
	public SvnAssignUserToRoleCommand(String usrId, String role, Configuration configuration,Project project){
		super(configuration, project);
		this.usrId = usrId;
		this.role = role;
	}
	
	@Override
	public String getCommand() {
		String createRoleCommand = "trac-admin " + project.getProjectPath() + " permission add " + usrId + " " + role;
		return createRoleCommand;
	}

}
