package org.openkoala.opencis.trac.command;

import org.openkoala.opencis.api.Project;

import org.openkoala.opencis.support.SSHConnectConfig;

/**
 * Trac创建角色，赋予某个配置的默认权限
 *
 * @author 赵健华
 *         2013-9-23 下午8:52:51
 */
public class TracCreateUserToRoleCommand extends TracCommand {

//    private static final String PERMISSION = "TRAC-ADMIN";

    private String userName;


    public TracCreateUserToRoleCommand() {
        // TODO Auto-generated constructor stub
    }

    public TracCreateUserToRoleCommand(SSHConnectConfig configuration, String userName, Project project) {
        super(configuration, project);
        this.userName = userName;
    }

    @Override
    public String getCommand() {
        // TODO Auto-generated method stub
        String createRoleCommand = "trac-admin " + storePath + project.getProjectName() + " permission add " + userName + " " + PERMISSION;
        return createRoleCommand;
    }


}
