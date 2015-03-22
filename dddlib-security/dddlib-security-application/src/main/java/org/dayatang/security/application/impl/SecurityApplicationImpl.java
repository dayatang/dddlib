package org.dayatang.security.application.impl;

import org.dayatang.security.application.SecurityApplication;
import org.dayatang.security.domain.*;

import java.util.Set;

/**
 * Created by yyang on 15/1/20.
 */
public class SecurityApplicationImpl implements SecurityApplication {

    @Override
    public void createActor(Actor actor) {

    }

    @Override
    public void disableActor(Actor actor) {

    }

    @Override
    public void createAuthority(Authority authority) {

    }

    @Override
    public void disableAuthority(Authority authority) {

    }

    @Override
    public void lockUser(User user) {

    }

    @Override
    public void unlockUser(User user) {

    }

    @Override
    public void changePassword(User user, String origPassword, String newPassword) {

    }

    @Override
    public String resetPassword(User user) {
        return null;
    }

    @Override
    public User getUser(String id) {
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return null;
    }

    @Override
    public Permission getPermission(String id) {
        return null;
    }

    @Override
    public Role getRole(String id) {
        return null;
    }

    @Override
    public void authorize(Actor actor, Authority... authorities) {

    }

    @Override
    public void withdrawAuthority(Actor actor, Authority... authorities) {

    }

    @Override
    public Set<Role> getRolesOf(Actor actor) {
        return null;
    }

    @Override
    public <T extends Permission> Set<T> getPermissionsOf(Class<T> permissionClass, Actor actor) {
        return null;
    }
}
