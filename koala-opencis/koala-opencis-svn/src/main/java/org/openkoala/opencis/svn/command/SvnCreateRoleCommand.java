package org.openkoala.opencis.svn.command;

import org.openkoala.opencis.api.Project;

import com.dayatang.configuration.Configuration;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

/**
 * svn创建角色，赋予某个配置的默认权限
 */
public class SvnCreateRoleCommand extends SvnCommand{

	private String roleName;
	
	private String permission;
	
	public SvnCreateRoleCommand() {
		// TODO Auto-generated constructor stub
	}
	
	public SvnCreateRoleCommand(Configuration configuration,String roleName, Project project) {
		super(configuration, project);
		this.permission = configuration.getString("PERMISSION");
		this.roleName = roleName;
	}
	
	@Override
	public String getCommand() {
		String createRoleCommand = "trac-admin " + project.getProjectPath() + " permission add " + roleName + " " + permission;
		return createRoleCommand;
	}

	@Override
	public void doWork(Connection connection, Session session) {
		// TODO Auto-generated method stub
		
	}

	
}
