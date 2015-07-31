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

    public Set<Role> getRoles() {
        return getRoles(GlobalAuthorityScope.get());
    }

    public boolean hasRole(Role role) {
        return getRoles(GlobalAuthorityScope.get()).contains(role);
    }

    public void grantAuthorities(Authority... authorities) {
        grantAuthorities(GlobalAuthorityScope.get(), authorities);
    }

    public void withdrawAuthorities(Authority... authorities) {
        withdrawAuthorities(GlobalAuthorityScope.get(), authorities);
    }

    /**
     * 判断是否拥有指定的权限
     * @param permission 权限
     * @return 如果拥有指定的权限则返回true，否则返回false
     */
    public boolean hasPermission(Permission permission) {
        return getPermissions(GlobalAuthorityScope.get()).contains(permission);
    }

    public Set<Permission> getPermissions() {
        return getPermissions(GlobalAuthorityScope.get());
    }

    /*=====================================带范围的=============================*/

    public Set<Role> getRoles(AuthorityScope scope) {
        Set<Role> results = new HashSet<Role>();
        for (Authority authority : Authorization.findAuthoritiesByActor(this, scope)) {
            if (authority instanceof Role) {
                results.add((Role) authority);
            }
        }
        return results;
    }

    public boolean hasRole(Role role, AuthorityScope scope) {
        return getRoles(scope).contains(role);
    }

    /**
     * 判断是否拥有指定的权限
     * @param permission 权限
     * @return 如果拥有指定的权限则返回true，否则返回false
     */
    public boolean hasPermission(Permission permission, AuthorityScope scope) {
        return getPermissions(scope).contains(permission);
    }

    public Set<Permission> getPermissions(AuthorityScope scope) {
        Set<Permission> results = new HashSet<Permission>();
        for (Authorization authorization : Authorization.findByActor(this, scope)) {
            Authority authority = authorization.getAuthority();
            if (authority instanceof Permission) {
                results.add((Permission) authority);
            }
            if (authority instanceof Role) {
                results.addAll(((Role) authority).getPermissions());
            }
        }
        return results;
    }

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
