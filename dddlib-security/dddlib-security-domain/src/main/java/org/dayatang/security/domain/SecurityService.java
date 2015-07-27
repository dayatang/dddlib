package org.dayatang.security.domain;

import java.util.Date;
import java.util.Set;

/**
 * 安全服务
 * Created by yyang on 15/7/27.
 */
public class SecurityService {

    public void createActor(Actor actor) {
        actor.save();
    }

    public void disableActor(Actor actor) {
        disableActor(actor, new Date());
    }

    public void disableActor(Actor actor, Date date) {
        actor.disable(date);
    }

    public void enableActor(Actor actor) {
        actor.enable();
    }

    public void createAuthority(Authority authority) {
        authority.save();
    }

    public void disableAuthority(Authority authority) {
        disableAuthority(authority, new Date());
    }

    public void disableAuthority(Authority authority, Date date) {
        authority.disable(date);
    }

    public void lockUser(User user) {
        user.lock();
    }

    public void unlockUser(User user) {
        user.unlock();
    }

    public void changePassword(User user, String origPassword, String newPassword) {
        if (!user.matchPassword(origPassword)) {
            throw new PasswordUnmatchException();
        }
        user.setPassword(newPassword);
        user.save();
    }

    public User getUserById(String id) {
        return AbstractEntity.get(User.class, id);
    }

    public User getUserByUsername(String username) {
        return User.getByName(username);
    }

    public User getUserByNameAndPassword(String name, String password) {
        return AbstractEntity.getRepository().createCriteriaQuery(User.class).eq("name", name).eq("password", password).singleResult();
    }

    public Permission getPermissionById(String id) {
        return AbstractEntity.get(Permission.class, id);
    }

    public Role getRoleById(String id) {
        return AbstractEntity.get(Role.class, id);
    }

    public Role getRoleByName(String roleName) {
        return Role.getByName(roleName);
    }

    public void grantAuthorities(Actor actor, Authority... authorities) {
        for (Authority authority : authorities) {
            Authorization.authorize(actor, authority);
        }
    }

    public void grantAuthorities(Actor actor, AuthorityScope scope, Authority... authorities) {
        actor.grantAuthorities(scope, authorities);
    }

    public void withdrawAuthorities(Actor actor, Authority... authorities) {
        actor.withdrawAuthorities(authorities);
    }

    public void withdrawAuthorities(Actor actor, AuthorityScope scope, Authority... authorities) {
        actor.withdrawAuthorities(scope, authorities);
    }

    public Set<Role> getRolesOf(Actor actor) {
        return actor.getRoles();
    }

    public Set<Role> getRolesOf(Actor actor, AuthorityScope scope) {
        return actor.getRoles(scope);
    }

    public Set<Permission> getPermissionsOf(Actor actor) {
        return actor.getPermissions();
    }

    public Set<Permission> getPermissionsOf(Actor actor, AuthorityScope scope) {
        return actor.getPermissions(scope);
    }
}
