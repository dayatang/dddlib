package org.dayatang.security.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 记录角色与权限的对应关系
 * Created by yyang on 15/8/14.
 */
@Entity
@Table(name = "role_perm_relationship")
class RolePermissionRelationship extends AbstractEntity {

    @ManyToOne
    private Role role;

    @ManyToOne
    private Permission permission;

    protected RolePermissionRelationship() {
    }

    public RolePermissionRelationship(Role role, Permission permission) {
        this.role = role;
        this.permission = permission;
    }

    public Role getRole() {
        return role;
    }

    public Permission getPermission() {
        return permission;
    }

    public static List<RolePermissionRelationship> findByRole(Role role) {
        return getRepository().createCriteriaQuery(RolePermissionRelationship.class)
                .eq("role", role)
                .list();
    }

    public static List<RolePermissionRelationship> findByPermission(Permission permission) {
        return getRepository().createCriteriaQuery(RolePermissionRelationship.class)
                .eq("permission", permission)
                .list();
    }

    public static Set<Permission> getPermissionsOf(Role role) {
        Set<Permission> results = new HashSet<Permission>();
        for (RolePermissionRelationship each : findByRole(role)) {
            results.add(each.getPermission());
        }
        return results;
    }

    public static Set<Role> getRolesOf(Permission permission) {
        Set<Role> results = new HashSet<Role>();
        for (RolePermissionRelationship each : findByPermission(permission)) {
            results.add(each.getRole());
        }
        return results;
    }
}
