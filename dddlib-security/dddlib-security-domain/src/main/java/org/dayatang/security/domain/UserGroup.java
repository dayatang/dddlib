package org.dayatang.security.domain;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
    @JoinTable(name = "security_group_member_relationship",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "member_id"))
    private Set<Actor> members = new HashSet<Actor>();

    protected UserGroup() {
        super();
    }

    public UserGroup(String name) {
        super(name);
    }

    public Set<Actor> getMembers() {
        return members;
    }

    public void addMember(Actor actor) {
        members.add(actor);
        actor.addParentGroup(this);
        save();
    }

    public void removeMember(Actor actor) {
        members.remove(actor);
        actor.removeParentGroup(this);
        save();
    }

    public Set<User> getUsers() {
        Set<User> results = new HashSet<User>();
        for (Actor actor : getMembers()) {
            if (actor instanceof User) {
                results.add((User) actor);
            }
        }
        return results;
    }

    public Set<UserGroup> getChildGroups() {
        Set<UserGroup> results = new HashSet<UserGroup>();
        for (Actor actor : getMembers()) {
            if (actor instanceof UserGroup) {
                results.add((UserGroup) actor);
            }
        }
        return results;
    }

    private boolean hasMember(Actor actor) {
        return getMembers().contains(actor);
    }

    @Override
    public void remove() {
        for (UserGroup each : getChildGroups()) {
            each.remove();
        }
        super.remove();
    }

    @Override
    public void disable(Date date) {
        for (UserGroup each : getChildGroups()) {
            each.disable(date);
        }
        super.disable(date);
    }

    public UserGroup createChild(String childGroupName) {
        UserGroup child = create(childGroupName);
        this.addMember(child);
        return child;
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 23).append(getName()).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof UserGroup)) {
            return false;
        }
        UserGroup that = (UserGroup) other;
        return new EqualsBuilder().append(this.getName(), that.getName()).isEquals();
    }

    @Override
    public String toString() {
        return getName();
    }

}
