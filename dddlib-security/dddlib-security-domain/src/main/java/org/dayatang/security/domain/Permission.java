package org.dayatang.security.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;
import java.util.Objects;

/**
 * 许可，代表对系统一项细粒度功能的访问权限。或者不太精确地说，代表一项具体的系统功能
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Permission)) {
            return false;
        }
        Permission that = (Permission) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return "Permission{" + getName() + "}";
    }
}
