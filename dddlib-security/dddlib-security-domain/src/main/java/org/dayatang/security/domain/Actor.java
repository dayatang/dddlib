package org.dayatang.security.domain;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public Set<Role> getRoles() {
        Set<Role> results = new HashSet<Role>();
        for (Authority authority : Authorization.findAuthoritiesByActor(this)) {
            if (authority instanceof Role) {
                results.add((Role) authority);
            }
        }
        return results;
    }

    public void grantAuthorities(Authority... authorities) {
        for (Authority authority : authorities) {
            new Authorization(this, authority).save();
        }
    }

    public void withdrawAuthorities(Authority... authorities) {
        for (Authority authority : authorities) {
            Authorization authorization = Authorization.get(this, authority);
            if (authorization != null) {
                authorization.remove();
            }
        }
    }

    public boolean hasRole(Role role) {
        return getRoles().contains(role);
    }

    public boolean hasAllRoles(Role... roles) {
        return hasAllRoles(Arrays.asList(roles));
    }

    public boolean hasAllRoles(Collection<Role> roles) {
        return getRoles().containsAll(roles);
    }

    /**
     * 判断是否拥有指定的权限
     * @param permission 权限
     * @return 如果拥有指定的权限则返回true，否则返回false
     */
    public boolean hasPermission(Permission permission) {
        return getPermissions().contains(permission);
    }

    public Set<Permission> getPermissions() {
        Set<Permission> results = new HashSet<Permission>();
        for (Authorization authorization : Authorization.findByActor(this)) {
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

    public boolean hasAllPermissions(Collection<Permission> permissions) {
        return getPermissions().containsAll(permissions);
    }

    public boolean hasAllPermissions(Permission... permissions) {
        return getPermissions().containsAll(Arrays.asList(permissions));
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
}
