package org.dayatang.security.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
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
    @JoinTable(name = "security_role_permission",
            joinColumns = @JoinColumn(name = "permission_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Permission> permissions = new HashSet<Permission>();

    protected Role() {
    }

    public Role(String name) {
        super(name);
    }

    public Role(String name, Set<Permission> permissions) {
        super(name);
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public void setPermissions(Permission... permissions) {
        this.permissions = new HashSet<Permission>(Arrays.asList(permissions));
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

    public static Role create(String name) {
        Role role = new Role(name);
        role.save();
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        Role that = (Role) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return "Role{" + getName() + "}";
    }
}
