package org.dayatang.security.api;

import java.util.Collection;

/**
 * Created by yyang on 2016/10/31.
 */
public interface SecurityMgmtService {
    UserGroupInfo createUserGroup(String name);
    UserGroupInfo createUserGroup(String name, String remark);
    UserGroupInfo createChildUserGroup(String parentGroup, String name);
    UserGroupInfo createChildUserGroup(String parentGroup, String name, String remark);
    void changeNameOfUserGroup(String originalName, String newName);
    void changeRemarkOfUserGroup(String groupName, String remark);
    void disableUserGroup(String groupName);

    UserInfo createUser(String username, String password);
    UserInfo createUser(String username, String password, String remark);
    void changeNameOfUser(String originalName, String newName);
    void changePasswordOfUser(String username, String newPassword);
    void changeRemarkOfUser(String username, String remark);
    void lockUser(String username);
    void unlockUser(String username);
    void disableUser(String username);

    RoleInfo createRole(String name);
    RoleInfo createRole(String name, String remark);
    void changeNameOfRole(String originalName, String newName);
     void changeRemarkOfRole(String roleName, String remark);
    void disableRole(String roleName);

    PermissionInfo createPermission(String name); 
    PermissionInfo createPermission(String name, String remark); 
    void changeNameOfPermission(String originalName, String newName); 
    void changeRemarkOfPermission(String permissionName, String remark); 
    void disablePermission(String permissionName); 

    void addUsersToGroup(String groupName, String... usernames);
    void addUsersToGroup(String groupName, Collection<String> usernames);
    void removeUsersFromGroup(String groupName, String... usernames);
    void removeUsersFromGroup(String groupName, Collection<String> usernames);


}
