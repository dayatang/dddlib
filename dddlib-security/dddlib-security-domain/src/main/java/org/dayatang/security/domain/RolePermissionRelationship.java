package org.dayatang.security.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;

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

    public List<RolePermissionRelationship> findByRole(Role role) {
        return getRepository().createCriteriaQuery(RolePermissionRelationship.class)
                .eq("role", role)
                .list();
    }
}
