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

    protected Role() {
    }

    public Role(String name) {
        super(name);
    }

    public Set<Permission> getPermissions() {
        return RolePermissionRelationship.getPermissionsOf(this);
    }

    public void assignPermissions(Set<Permission> permissions) {
        Set<Permission> currentPermissions = getPermissions();
        for (RolePermissionRelationship each : RolePermissionRelationship.findByRole(this)) {
            if (!permissions.contains(each.getPermission())) {
                each.remove();
            }
        }
        for (Permission each : permissions) {
            if (currentPermissions.contains(each)) {
                continue;
            }
            new RolePermissionRelationship(this, each).save();
        }
    }

    public void assignPermissions(Permission... permissions) {
        assignPermissions(new HashSet<Permission>(Arrays.asList(permissions)));
    }

    public boolean hasPermission(Permission permission) {
        return getPermissions().contains(permission);
    }

    @Override
    public void remove() {
        for (RolePermissionRelationship each : RolePermissionRelationship.findByRole(this)) {
            each.remove();
        }
        super.remove();
    }

    @Override
    public void disable(Date date) {
        for (RolePermissionRelationship each : RolePermissionRelationship.findByRole(this)) {
            each.remove();
        }
        super.disable(date);
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
