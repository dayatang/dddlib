package org.dayatang.security.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.*;

/**
 * 用户角色。角色是许可Permission的集合。如果一批Permission总是作为一个整体授予某个
 * 或某些Actor，则为这批Permission定义一个Role并命名之，然后可以将这个role授予Actor。
 * Created by yyang on 15/1/13.
 */
@Entity
@DiscriminatorValue("ROLE")
public class Role extends Authority {

    @ManyToMany
    @JoinTable(name = "security_role_permission")
    private Set<Permission> permissions = new HashSet<Permission>();

    protected Role() {
    }

    public Role(String name) {
        super(name);
    }

    public Set<Permission> getPermissions() {
        return new HashSet<Permission>(permissions);
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = new HashSet<Permission>(permissions);
    }

    public void addPermissions(Permission... permissions) {
        this.permissions.addAll(Arrays.asList(permissions));
        save();
    }

    public void removePermissions(Permission... permissions) {
        this.permissions.removeAll(Arrays.asList(permissions));
    }

    public void clearPermissions() {
        permissions.clear();
    }

    public boolean hasPermission(Permission permission) {
        return getPermissions().contains(permission);
    }

    public static Role get(String id) {
        return get(Role.class, id);
    }

    public static Role getByName(String name) {
        return getByName(Role.class, name);
    }

    public static List<Role> list() {
        return findAll(Role.class);
    }

    @Override
    public String toString() {
        return getName();
    }

    public static Role create(String name) {
        Role role = new Role(name);
        role.save();
        return role;
    }
}
