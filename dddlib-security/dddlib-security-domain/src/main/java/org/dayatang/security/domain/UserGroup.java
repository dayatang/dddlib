package org.dayatang.security.domain;

import com.google.common.collect.ImmutableSet;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

    public UserGroup() {
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
}
