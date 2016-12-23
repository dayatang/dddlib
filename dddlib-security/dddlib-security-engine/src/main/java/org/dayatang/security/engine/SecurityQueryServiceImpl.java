package org.dayatang.security.engine;

import org.dayatang.security.api.SecurityQueryService;
import org.dayatang.security.api.UserInfo;
import org.dayatang.security.domain.AuthorizationScope;
import org.dayatang.security.domain.Permission;
import org.dayatang.security.domain.Role;
import org.dayatang.security.domain.User;

/**
 * Created by yyang on 2016/10/31.
 */
public class SecurityQueryServiceImpl implements SecurityQueryService {
    @Override
    public UserInfo getUser(String id) {
        return null;
    }

    @Override
    public UserInfo getUserByUsername(String username) {
        return null;
    }

    @Override
    public boolean usernameExisted(String username) {
        return false;
    }

    @Override
    public UserInfo login(String username, String password) {
        return null;
    }

    @Override
    public boolean hasRole(String username, String role) {
        return false;
    }

    @Override
    public boolean hasPermission(String username, String permission) {
        return false;
    }

    @Override
    public boolean hasRoleInScope(String username, String role, String scope) {
        return false;
    }

    @Override
    public boolean hasPermissionInScope(String username, String permission, String scope) {
        return false;
    }
}
