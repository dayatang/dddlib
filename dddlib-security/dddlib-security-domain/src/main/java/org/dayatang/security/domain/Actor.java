package org.dayatang.security.domain;

import javax.persistence.*;
import java.util.*;

/**
 * 参与者，是用户User和角色Role的共同基类，授权就是将某种权限授予某个参与者
 * Created by yyang on 15/1/13.
 */
@Entity
@Table(name = "security_actors")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Actor extends AbstractEntity {

    //名字
    private String name;

    //说明
    private String remark;

    @ManyToMany(mappedBy = "members")
    private Set<UserGroup> parentGroups = new HashSet<UserGroup>();

    public Actor() {
    }

    public Actor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Set<UserGroup> getParentGroups() {
        return parentGroups;
    }

    public void addParentGroup(UserGroup group) {
        parentGroups.add(group);
    }

    public void removeParentGroup(UserGroup group) {
        parentGroups.remove(group);
    }

    @Override
    public String[] businessKeys() {
        return new String[] {"name"};
    }

    /*=====================================全局范围的=============================*/

    /**
     * 授予新的权力
     * @param authorities 要授予的权力（Role或Permission）
     */
    public void grantAuthorities(Authority... authorities) {
        grantAuthorities(GlobalAuthorizationScope.get(), authorities);
    }

    /**
     * 撤销已有的权力
     * @param authorities 要撤销的权力（Role或Permission）
     */
    public void withdrawAuthorities(Authority... authorities) {
        withdrawAuthorities(GlobalAuthorizationScope.get(), authorities);
    }

    /**
     * 在全局范围内设置直接授予Actor的角色。此前已经授予的角色如果不在此范围内则自动撤销
     * @param roles 要设置的权力
     */
    public void setRoles(Role... roles) {
        setRoles(GlobalAuthorizationScope.get(), roles);
    }

    /**
     * 获取在全局范围内设置直接授予Actor的角色，不包括从用户组及用户组的所有上级用户组继承下来的角色。
     * @return 用户的全部角色
     */
    public Set<Role> getRoles() {
        return getRoles(GlobalAuthorizationScope.get());
    }

    /**
     * 获取Actor在全局范围内拥有的所有角色，包括从用户组及用户组的所有上级用户组继承下来的角色。
     * @return 用户的全部角色
     */
    public Set<Role> getAllRoles() {
        return getAllRoles(GlobalAuthorizationScope.get());
    }

    /**
     * 判断Actor在全局范围内是否拥有指定的角色
     * @param role 角色
     * @return 如果Actor拥有该角色，返回true；否则返回false
     */
    public boolean hasRole(Role role) {
        return getAllRoles(GlobalAuthorizationScope.get()).contains(role);
    }

    /**
     * 在全局范围内设置直接授予Actor的权限。此前已经授予的权限如果不在此范围内则自动撤销
     * @param permissions 要设置的权限
     */
    public void setPermissions(Set<Permission> permissions) {
        setPermissions(GlobalAuthorizationScope.get(), permissions);
    }

    /**
     * 获取在全局范围内设置直接授予Actor的权限，不包括从角色和用户组中继承下来的权限
     * @return 用户拥有的全部权限
     */
    public Set<Permission> getPermissions() {
        return getPermissions(GlobalAuthorizationScope.get());
    }

    /**
     * 获取全局范围内用户拥有的全部权限，包括从角色和用户组中继承下来的权限
     * @return 用户拥有的全部权限
     */
    public Set<Permission> getAllPermissions() {
        return getAllPermissions(GlobalAuthorizationScope.get());
    }

    /**
     * 判断Actor在全局范围内是否拥有指定的权限
     * @param permission 权限
     * @return 如果拥有指定的权限则返回true，否则返回false
     */
    public boolean hasPermission(Permission permission) {
        return getAllPermissions(GlobalAuthorizationScope.get()).contains(permission);
    }

    /*=====================================带范围的=============================*/

    public void grantAuthorities(AuthorizationScope scope, Authority... authorities) {
        for (Authority authority : authorities) {
            Authorization.grantAuthority(this, authority, scope);
        }
    }

    public void withdrawAuthorities(AuthorizationScope scope, Authority... authorities) {
        for (Authority authority : authorities) {
            Authorization.withdrawAuthority(this, authority, scope);
        }
    }

    public void setRoles(AuthorizationScope scope, Role... roles) {
        setRoles(scope, new HashSet<Role>(Arrays.asList(roles)));
    }

    public void setRoles(AuthorizationScope scope, Set<Role> roles) {
        for (Authorization authorization : Authorization.findByActor(this, scope)) {
            if (!roles.contains(authorization.getAuthority())) {
                authorization.remove();
            }
        }
        for (Role role : roles) {
            Authorization.grantAuthority(this, role, scope);
        }
    }

    public Set<Role> getRoles(AuthorizationScope scope) {
        return Authorization.getAuthoritiesOfActor(this, scope, Role.class);
    }

    public Set<Role> getAllRoles(AuthorizationScope scope) {
        Set<Role> results = new HashSet<Role>();
        results.addAll(getRoles(scope));
        for (UserGroup group : getParentGroups()) {
            results.addAll(group.getAllRoles(scope));
        }
        return results;
    }

    public boolean hasRole(Role role, AuthorizationScope scope) {
        return getAllRoles(scope).contains(role);
    }

    public void setPermissions(AuthorizationScope scope, Permission... permissions) {
        setPermissions(scope, new HashSet<Permission>(Arrays.asList(permissions)));
    }

    public void setPermissions(AuthorizationScope scope, Set<Permission> permissions) {
        for (Authorization authorization : Authorization.findByActor(this, scope)) {
            if (!permissions.contains(authorization.getAuthority())) {
                authorization.remove();
            }
        }
        for (Permission permission : permissions) {
            Authorization.grantAuthority(this, permission, scope);
        }
    }

    public Set<Permission> getPermissions(AuthorizationScope scope) {
        return Authorization.getAuthoritiesOfActor(this, scope, Permission.class);
    }

    public Set<Permission> getAllPermissions(AuthorizationScope scope) {
        Set<Permission> results = new HashSet<Permission>();
        results.addAll(getPermissions(scope));
        for (Role role : getRoles(scope)) {
            results.addAll(role.getPermissions());
        }
        for (UserGroup group : getParentGroups()) {
            results.addAll(group.getAllPermissions(scope));
        }
        return results;
    }

    /**
     * 判断是否拥有指定的权限
     * @param permission 权限
     * @return 如果拥有指定的权限则返回true，否则返回false
     */
    public boolean hasPermission(Permission permission, AuthorizationScope scope) {
        return getAllPermissions(scope).contains(permission);
    }

    /**
     * 失效参与者，同时失效它参与的授权信息
     * @param date 失效日期
     */
    @Override
    public void disable(Date date) {
        for (Authorization authorization : Authorization.findByActor(this)) {
            authorization.disable(date);
        }
        for (UserGroup group : getParentGroups()) {
            group.removeMember(this);
        }
        super.disable(date);
    }

    /**
     * 删除参与者，同时删除其参与的授权信息
     */
    @Override
    public void remove() {
        for (Authorization authorization : Authorization.findByActor(this)) {
            authorization.remove();
        }
        for (UserGroup group : getParentGroups()) {
            group.removeMember(this);
        }
        super.remove();
    }

    /**
     * 根据名字获取某种类型的Actor
     * @param actorClass Actor的类
     * @param name 名称
     * @param <T> Actor的类型
     * @return 如果找到，返回该Actor，否则返回null
     */
    public static <T extends Actor> T getByName(Class<T> actorClass, String name) {
        return AbstractEntity.getByProperty(actorClass, "name", name);
    }
}
