package org.dayatang.security.domain;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
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
        grantAuthorities(GlobalAuthorityScope.get(), authorities);
    }

    /**
     * 撤销已有的权力
     * @param authorities 要撤销的权力（Role或Permission）
     */
    public void withdrawAuthorities(Authority... authorities) {
        withdrawAuthorities(GlobalAuthorityScope.get(), authorities);
    }

    /**
     * 在全局范围内设置直接授予Actor的角色。此前已经授予的角色如果不在此范围内则自动撤销
     * @param roles 要设置的权力
     */
    public void setRoles(Role... roles) {
        setRoles(GlobalAuthorityScope.get(), roles);
    }

    /**
     * 在全局范围内设置直接授予Actor的权限。此前已经授予的权限如果不在此范围内则自动撤销
     * @param permissions 要设置的权限
     */
    public void setPermissions(Set<Permission> permissions) {
        setPermissions(GlobalAuthorityScope.get(), permissions);
    }

    /**
     * 获取Actor在全局范围内拥有的所有角色，包括从用户组及用户组的所有上级用户组继承下来的角色。
     * @return 用户的全部角色
     */
    public Set<Role> getAllRoles() {
        return getAllRoles(GlobalAuthorityScope.get());
    }

    /**
     * 判断Actor在全局范围内是否拥有指定的角色
     * @param role 角色
     * @return 如果Actor拥有该角色，返回true；否则返回false
     */
    public boolean hasRole(Role role) {
        return getAllRoles(GlobalAuthorityScope.get()).contains(role);
    }

    /**
     * 获取全局范围内用户拥有的全部权限，包括从角色和用户组中继承下来的权限
     * @return 用户拥有的全部权限
     */
    public Set<Permission> getAllPermissions() {
        return getAllPermissions(GlobalAuthorityScope.get());
    }

    /**
     * 判断Actor在全局范围内是否拥有指定的权限
     * @param permission 权限
     * @return 如果拥有指定的权限则返回true，否则返回false
     */
    public boolean hasPermission(Permission permission) {
        return getAllPermissions(GlobalAuthorityScope.get()).contains(permission);
    }

    /*=====================================带范围的=============================*/

    public void grantAuthorities(AuthorityScope scope, Authority... authorities) {
        for (Authority authority : authorities) {
            Authorization.grantAuthority(this, authority, scope);
        }
    }

    public void withdrawAuthorities(AuthorityScope scope, Authority... authorities) {
        for (Authority authority : authorities) {
            Authorization.withdrawAuthority(this, authority, scope);
        }
    }

    public Set<Role> getAllRoles(AuthorityScope scope) {
        Set<Role> results = new HashSet<Role>();
        results.addAll(getRoles(scope));
        for (UserGroup group : GroupMemberRelationship.getGroupsOf(this)) {
            results.addAll(group.getAllRoles(scope));
        }
        return results;
    }

    public boolean hasRole(Role role, AuthorityScope scope) {
        return getAllRoles(scope).contains(role);
    }

    public Set<Role> getRoles(AuthorityScope scope) {
        return Authorization.getAuthoritiesOfActor(this, scope, Role.class);
    }

    public void setRoles(AuthorityScope scope, Role... roles) {
        setRoles(scope, new HashSet<Role>(Arrays.asList(roles)));
    }

    public void setRoles(AuthorityScope scope, Set<Role> roles) {
        for (Authorization authorization : Authorization.findByActor(this, scope)) {
            if (!roles.contains(authorization.getAuthority())) {
                authorization.remove();
            }
        }
        for (Role role : roles) {
            Authorization.grantAuthority(this, role, scope);
        }
    }

    public Set<Permission> getAllPermissions(AuthorityScope scope) {
        Set<Permission> results = new HashSet<Permission>();
        results.addAll(getPermissions(scope));
        for (Role role : getRoles(scope)) {
            results.addAll(role.getPermissions());
        }
        for (UserGroup group : GroupMemberRelationship.getGroupsOf(this)) {
            results.addAll(group.getAllPermissions(scope));
        }
        return results;
    }

    /**
     * 判断是否拥有指定的权限
     * @param permission 权限
     * @return 如果拥有指定的权限则返回true，否则返回false
     */
    public boolean hasPermission(Permission permission, AuthorityScope scope) {
        return getAllPermissions(scope).contains(permission);
    }

    public Set<Permission> getPermissions(AuthorityScope scope) {
        return Authorization.getAuthoritiesOfActor(this, scope, Permission.class);
    }

    public void setPermissions(AuthorityScope scope, Permission... permissions) {
        setPermissions(scope, new HashSet<Permission>(Arrays.asList(permissions)));
    }

    public void setPermissions(AuthorityScope scope, Set<Permission> permissions) {
        for (Authorization authorization : Authorization.findByActor(this, scope)) {
            if (!permissions.contains(authorization.getAuthority())) {
                authorization.remove();
            }
        }
        for (Permission permission : permissions) {
            Authorization.grantAuthority(this, permission, scope);
        }
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
