package org.dayatang.security.domain;

import com.google.common.collect.ImmutableSet;

import javax.persistence.*;
import java.util.*;

/**
 * 用户组。用户组可以包含用户和其他用户组
 * Created by yyang on 15/1/24.
 */
@Entity
@DiscriminatorValue("GROUP")
public class UserGroup extends Actor {

    @ManyToMany
    @JoinTable(name = "security_group_user")
    private Set<User> users = new HashSet<User>();

    @ManyToOne
    private UserGroup parent;

    @ManyToMany(mappedBy = "parent")
    private Set<UserGroup> children = new HashSet<UserGroup>();

    protected UserGroup() {
        super();
    }

    public UserGroup(String name) {
        super(name);
    }

    public Set<User> getUsers() {
        return ImmutableSet.copyOf(users);
    }

    public void setUsers(Set<User> users) {
        this.users = new HashSet<User>(users);
    }

    public void addUser(User... users) {
        this.users.addAll(Arrays.asList(users));
    }

    public void removeUser(User... users) {
        this.users.removeAll(Arrays.asList(users));
    }

    public UserGroup getParent() {
        return parent;
    }

    public void setParent(UserGroup parent) {
        this.parent = parent;
    }

    public Set<UserGroup> getChildren() {
        return ImmutableSet.copyOf(children);
    }

    public void addChild(UserGroup... groups) {
        children.addAll(Arrays.asList(groups));
        for (UserGroup each : groups) {
            each.setParent(this);
        }
    }

    public void removeChild(UserGroup... groups) {
        children.removeAll(Arrays.asList(groups));
    }

    public void setChildren(Set<UserGroup> children) {
        this.children = new HashSet<UserGroup>(children);
        for (UserGroup each : children) {
            each.setParent(this);
        }
    }

    @Override
    public void remove() {
        for (UserGroup child : children) {
            child.remove();
        }
        super.remove();
    }

    @Override
    public void disable(Date date) {
        for (UserGroup child : children) {
            child.disable(date);
        }
        super.disable(date);
    }

    public static UserGroup create(String name) {
        UserGroup group = new UserGroup(name);
        group.save();
        return group;
    }

    /**
     * 根据ID获取用户组。
     * @param id 用户组ID
     * @return 如果找到指定组ID的用户则返回该用户组，否则返回null
     */
    public static UserGroup get(String id) {
        return get(UserGroup.class, id);
    }

    /**
     * 根据名称获取用户组。
     * @param name 名称
     * @return 如果找到指定名字的用户组则返回该用户组，否则返回null
     */
    public static UserGroup getByName(String name) {
        return getByName(UserGroup.class, name);
    }
}
