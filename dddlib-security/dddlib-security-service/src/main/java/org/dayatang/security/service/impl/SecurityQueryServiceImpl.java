package org.dayatang.security.service.impl;

import org.dayatang.security.domain.AuthorizationScope;
import org.dayatang.security.domain.Permission;
import org.dayatang.security.domain.Role;
import org.dayatang.security.domain.User;
import org.dayatang.security.service.SecurityQueryService;

/**
 * Created by yyang on 2016/10/31.
 */
public class SecurityQueryServiceImpl implements SecurityQueryService {
    @Override
    public User getUser(String id) {
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return null;
    }

    @Override
    public boolean usernameExisted(String username) {
        return false;
    }

    @Override
    public User login(String username, String password) {
        return null;
    }

    @Override
    public boolean hasRole(User user, Role role) {
        return false;
    }

    @Override
    public boolean hasPermission(User user, Permission permission) {
        return false;
    }

    @Override
    public boolean hasRoleInScope(User user, Role role, AuthorizationScope scope) {
        return false;
    }

    @Override
    public boolean hasPermissionInScope(User user, Permission permission, AuthorizationScope scope) {
        return false;
    }
}
