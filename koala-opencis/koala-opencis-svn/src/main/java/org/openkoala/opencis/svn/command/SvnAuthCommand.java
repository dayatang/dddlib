package org.openkoala.opencis.svn.command;

import org.openkoala.opencis.api.Project;

import com.dayatang.configuration.Configuration;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

/**
 * svn分配用户到某个角色
 */
public class SvnAuthCommand extends SvnCommand {

	private String usrId;
	
	private String role;
	
	public SvnAuthCommand() {
		// TODO Auto-generated constructor stub
	}
	
	public SvnAuthCommand(String usrId, String role, Configuration configuration,Project project){
		super(configuration, project);
		this.usrId = usrId;
		this.role = role;
	}
	
	@Override
	public String getCommand() {
		String assignUserToRoleCommand = "trac-admin " + project.getProjectPath() + " permission add " + usrId + " " + role;
		return assignUserToRoleCommand;
	}

	@Override
	public void doWork(Connection connection, Session session) {
		// TODO Auto-generated method stub
		
	}

}
