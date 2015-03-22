package org.dayatang.security.application;

import org.dayatang.security.domain.*;

import java.util.Set;

/**
 * Created by yyang on 15/1/13.
 */
public interface SecurityApplication {

    /**
     * 锁定用户。被锁定的用户无法登录系统，直至解锁
     * @param user 要锁定的用户
     */
    void lockUser(User user);

    /**
     * 解锁用户。解锁后用户重新可以登录系统
     * @param user 要解锁的用户
     */
    void unlockUser(User user);

    /**
     * 更改用户登录口令。如果原口令验证失败则不允许更改口令
     * @param user 用户
     * @param origPassword 原口令
     * @param newPassword 新口令
     */
    void changePassword(User user, String origPassword, String newPassword);

    /**
     * 重设用户口令
     * @param user 用户
     */
    String resetPassword(User user);

    /**
     * 根据ID找到用户
     * @param id 用户ID
     * @return 如果找到用户则返回该用户，否则返回null
     */
    User getUser(String id);

    /**
     * 根据用户名获取用户（注意该用户可能已经失效）
     * @param username 用户名
     * @return 如果找到用户则返回该用户，否则返回null
     */
    User getUserByUsername(String username);

    /**
     * 根据ID获取权限
     * @param id 权限ID
     * @return 权限
     */
    Permission getPermission(String id);

    /**
     * 根据ID获取角色
     * @param id 角色ID
     * @return 角色
     */
    Role getRole(String id);

    /**
     * 给Actor授权
     * @param actor 要授权的主体(用户User或用户组UserGroup)
     * @param authorities 要授予的权（角色Role或权限Permission）
     */
    void authorize(Actor actor, Authority... authorities);

    /**
     * 撤回授权
     * @param actor 要撤回授权的主体(用户User或用户组UserGroup)
     * @param authorities 要撤回的权（角色Role或权限Permission）
     */
    void withdrawAuthority(Actor actor, Authority... authorities);

    /**
     * 获取指定Actor拥有的所有角色
     * @param actor 授权主体，例如用户或用户组
     * @return 角色集合
     */
    Set<Role> getRolesOf(Actor actor);

    /**
     * 获取指定Actor拥有的某种类型的所有权限
     * @param permissionClass 权限类
     * @param actor 授权主体
     * @param <T> 权限类型
     * @return 权限集合
     */
    <T extends Permission> Set<T> getPermissionsOf(Class<T> permissionClass, Actor actor);
}
