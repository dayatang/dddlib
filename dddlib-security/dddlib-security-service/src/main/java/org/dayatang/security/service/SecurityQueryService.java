package org.dayatang.security.service;

import org.dayatang.security.domain.AuthorizationScope;
import org.dayatang.security.domain.Permission;
import org.dayatang.security.domain.Role;
import org.dayatang.security.domain.User;

/**
 * 权限查询服务
 * Created by yyang on 2016/10/31.
 */
public interface SecurityQueryService {

    /**
     * 根据ID获取用户
     * @param id 用户ID
     * @return 用户存在则返回该用户，否则返回null
     */
    User getUser(String id);

    /**
     * 根据用户名获取用户
     * @param username 用户名
     * @return 用户存在则返回该用户，否则返回null
     */
    User getUserByUsername(String username);

    /**
     * 判断系统中是否已经存在指定名字的用户
     * @param username 用户名
     * @return 如果用户存在，返回true；否则返回false
     */
    boolean usernameExisted(String username);

    /**
     * 通过帐号口令进行登录
     * @param username 用户名
     * @param password 口令
     * @return 如果用户存在、未失效、未被锁定，且口令正确，返回该用户；否则抛出AuthenticationException异常。
     */
    User login(String username, String password);

    /**
     * 判断用户是否在全局范围拥有指定角色
     * @param user 用户
     * @param role 角色
     * @return 如果user用户拥有role角色，返回true；否则返回false。
     */
    boolean hasRole(User user, Role role);

    /**
     * 判断用户是否在全局范围拥有指定权限
     * @param user 用户
     * @param permission 权限
     * @return 如果user用户拥有permission权限，返回true；否则返回false。
     */
    boolean hasPermission(User user, Permission permission);

    /**
     * 判断用户是否在全局范围拥有指定角色
     * @param user 用户
     * @param role 角色
     * @param scope 授权范围
     * @return 如果user用户在scope授权范围拥有role角色，返回true；否则返回false。
     */
    boolean hasRoleInScope(User user, Role role, AuthorizationScope scope);

    /**
     * 判断用户是否在全局范围拥有指定权限
     * @param user 用户
     * @param permission 权限
     * @param scope 授权范围
     * @return 如果user用户在scope授权范围拥有permission权限，返回true；否则返回false。
     */
    boolean hasPermissionInScope(User user, Permission permission, AuthorizationScope scope);

}
