package org.openkoala.opencis.trac.command;

import org.openkoala.opencis.api.Project;

import org.openkoala.opencis.support.SSHConnectConfig;

/**
 * Trac创建角色，赋予某个配置的默认权限
 *
 * @author 赵健华
 *         2013-9-23 下午8:52:51
 */
public class TracCreateRoleCommand extends TracCommand {

//    private static final String PERMISSION = "TRAC-ADMIN";

    private String roleName;


    public TracCreateRoleCommand() {
        // TODO Auto-generated constructor stub
    }

    public TracCreateRoleCommand(SSHConnectConfig configuration, String roleName, Project project) {
        super(configuration, project);
        this.roleName = roleName;
    }

    @Override
    public String getCommand() {
        // TODO Auto-generated method stub
        String createRoleCommand = "trac-admin " + project.getPhysicalPath() + " permission add " + roleName + " " + PERMISSION;
        return createRoleCommand;
    }


}
