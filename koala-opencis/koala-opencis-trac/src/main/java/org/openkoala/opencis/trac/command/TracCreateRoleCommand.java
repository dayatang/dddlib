package org.openkoala.opencis.trac.command;

import org.openkoala.opencis.api.Project;

import com.dayatang.configuration.Configuration;

public class TracCreateRoleCommand extends TracCommand{

	private String roleName;
	
	private String permission;
	
	public TracCreateRoleCommand() {
		// TODO Auto-generated constructor stub
	}
	
	public TracCreateRoleCommand(Configuration configuration,String roleName, Project project) {
		super(configuration, project);
		this.permission = configuration.getString("PERMISSION");
		this.roleName = roleName;
	}
	
	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		String createRoleCommand = "trac-admin " + project.getProjectPath() + " permission add " + roleName + " " + permission;
		return createRoleCommand;
	}

	
}
