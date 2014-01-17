package org.openkoala.opencis.svn.command;

import java.io.File;
import java.util.List;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.OpencisConstant;
import org.openkoala.opencis.support.SSHConnectConfig;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

/**
 * svn分配用户到某个角色
 */
public class SvnCreateGroupAndAddGroupUsersCommand extends SvnCommand {

    private List<String> userNames;

    private String role;

    public SvnCreateGroupAndAddGroupUsersCommand() {

    }

    public SvnCreateGroupAndAddGroupUsersCommand(List<String> userNames, String role, SSHConnectConfig config, Project project) {
        super(config, project);
        this.userNames = userNames;
        this.role = role;
    }

    @Override
    public String getCommand() {
        String groupName = project.getProjectName() + "_" + role;
        String groupUsers = ConvertGroupUserListToString();
        StringBuilder assignUserToRoleCommand = new StringBuilder();
        assignUserToRoleCommand.append("grep -q '^\\[groups\\]' ")
                .append(project.getProjectPath()).append(project.getProjectName()).append("/conf/authz ")
                .append("&& sed -i '/\\[groups\\]/a ").append(groupName).append("=").append(groupUsers).append("' ")
                .append(project.getProjectPath()).append(project.getProjectName()).append("/conf/authz ")
                .append("|| echo -ne '\n[groups]\n").append(groupName).append("=").append(groupUsers).append("' >>  ")
                .append(project.getProjectPath()).append(project.getProjectName()).append("/conf/authz ");
        return assignUserToRoleCommand.toString();
    }

    private String ConvertGroupUserListToString() {
        StringBuffer groupUsersStr = new StringBuffer();
        for (String groupUser : userNames) {
            if (groupUsersStr.length() > 0) {
                groupUsersStr.append("," + groupUser);
            } else {
                groupUsersStr.append(groupUser);
            }
        }
        return groupUsersStr.toString();
    }

    @Override
    public void doWork(Connection connection, Session session) {

    }

}
