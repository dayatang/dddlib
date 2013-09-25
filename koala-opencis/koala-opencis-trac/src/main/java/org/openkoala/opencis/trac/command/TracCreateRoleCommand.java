package org.openkoala.opencis.trac.command;

import org.openkoala.opencis.api.Project;

import com.dayatang.configuration.Configuration;

/**
 * Trac创建角色，赋予某个配置的默认权限
 * @author 赵健华
 * 2013-9-23 下午8:52:51
 */
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
