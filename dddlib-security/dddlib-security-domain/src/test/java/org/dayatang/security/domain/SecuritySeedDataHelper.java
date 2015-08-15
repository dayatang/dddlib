package org.dayatang.security.domain;


import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by yyang on 15/1/14.
 */
public class SecuritySeedDataHelper {

    private Map<String, FunctionalPermission> permissionMap = new HashMap<String, FunctionalPermission>();
    private Map<String, Role> roleMap = new HashMap<String, Role>();
    private Map<String, User> userMap = new HashMap<String, User>();
    private Map<String, UserGroup> groupMap = new HashMap<String, UserGroup>();
    private Map<String, Authorization> authorizationMap = new HashMap<String, Authorization>();

    private FunctionalPermission getOrCreateFunctionalPermission(String functionalPermission) {
        FunctionalPermission permission = permissionMap.get(functionalPermission);
        if (permission == null) {
            permission = new FunctionalPermission(functionalPermission);
            permission.save();
            permissionMap.put(functionalPermission, permission);
        }
        return permission;
    }

    public SecuritySeedDataHelper createRole(String roleName) {
        getOrCreateRole(roleName);
        return this;
    }

    private Role getOrCreateRole(String roleName) {
        Role role = roleMap.get(roleName);
        if (role == null) {
            role = new Role(roleName);
            role.save();
            roleMap.put(roleName, role);
        }
        return role;
    }

    public SecuritySeedDataHelper createUser(String userName) {
        getOrCreateUser(userName);
        return this;
    }

    private User getOrCreateUser(String userName) {
        User user = userMap.get(userName);
        if (user == null) {
            user = new MyUser(userName, "xxxx");
            user.save();
            userMap.put(userName, user);
        }
        return user;
    }

    public SecuritySeedDataHelper createGroup(String userGroup) {
        getOrCreateGroup(userGroup);
        return this;
    }

    private SecuritySeedDataHelper getOrCreateGroup(String userGroup) {
        UserGroup group = groupMap.get(userGroup);
        if (group == null) {
            group = UserGroup.create(userGroup);
            group.save();
            groupMap.put(userGroup, group);
        }
        return this;
    }

    public SecuritySeedDataHelper authorizeRoleToUser(String roleName, String userName) {
        User user = getOrCreateUser(userName);
        Role role = getOrCreateRole(roleName);
        Authorization authorization = new Authorization(user, role);
        authorization.save();
        return this;
    }

    public SecuritySeedDataHelper authorizeFunctionToUser(String function, String userName) {
        FunctionalPermission permission = getOrCreateFunctionalPermission(function);
        User user = getOrCreateUser(userName);
        Authorization authorization = new Authorization(user, permission);
        authorization.save();
        return this;
    }

    public void authorizeFunctionToRole(String function, String roleName) {
        FunctionalPermission permission = getOrCreateFunctionalPermission(function);
        Role role = getOrCreateRole(roleName);
        role.assignPermissions(permission);
        role.save();
    }

    public void clear() {
        for (Authorization each : authorizationMap.values()) {
            each.remove();
        }
        for (FunctionalPermission each : permissionMap.values()) {
            each.remove();
        }
        for (User each : userMap.values()) {
            each.remove();
        }
        for (Role each : roleMap.values()) {
            each.remove();
        }
    }
}
