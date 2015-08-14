package org.dayatang.security.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 许可基类
 * Created by yyang on 15/1/13.
 */
@Entity
@DiscriminatorValue("PERM")
public class Permission extends Authority {
    protected Permission() {
    }

    public Permission(String name) {
        super(name);
    }

    @Override
    public void remove() {
        for (RolePermissionRelationship each : RolePermissionRelationship.findByPermission(this)) {
            each.remove();
        }
        super.remove();
    }

    @Override
    public void disable(Date date) {
        for (RolePermissionRelationship each : RolePermissionRelationship.findByPermission(this)) {
            each.remove();
        }
        super.disable(date);
    }

    public static Permission create(String name) {
        Permission permission = new Permission(name);
        permission.save();
        return permission;
    }

    public static Permission get(String id) {
        return get(Permission.class, id);
    }

    public static Permission getByName(String name) {
        return getByName(Permission.class, name);
    }

    public static List<Permission> list() {
        return findAll(Permission.class);
    }
}
