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

    protected UserGroup() {
        super();
    }

    public UserGroup(String name) {
        super(name);
    }

    public UserGroup getParent() {
        return UserGroupRelationship.getParentOf(this);
    }

    public Set<Actor> getMembers() {
        return ImmutableSet.copyOf(UserGroupRelationship.findChildrenOf(this));
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

    public void addMembers(Actor... actors) {
        for (Actor actor : actors) {
            if (hasMember(actor)) {
                continue;
            }
            new UserGroupRelationship(this, actor).save();
        }
    }

    public void removeMembers(Actor... actors) {
        List<Actor> actorsToRemove = Arrays.asList(actors);
        for (UserGroupRelationship relationship : UserGroupRelationship.findByParent(this)) {
            if (actorsToRemove.contains(relationship.getChild())) {
                relationship.remove();
            }
        }
    }

    @Override
    public void remove() {
        for (UserGroup each : getChildGroups()) {
            each.remove();
        }
        for (UserGroupRelationship each : UserGroupRelationship.findByParent(this)) {
            each.remove();
        }
        for (UserGroupRelationship each : UserGroupRelationship.findByChild(this)) {
            each.remove();
        }
        super.remove();
    }

    @Override
    public void disable(Date date) {
        for (UserGroup each : getChildGroups()) {
            each.disable(date);
        }
        for (UserGroupRelationship each : UserGroupRelationship.findByParent(this)) {
            each.disable(date);
        }
        for (UserGroupRelationship each : UserGroupRelationship.findByChild(this)) {
            each.disable(date);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserGroup)) {
            return false;
        }
        UserGroup that = (UserGroup) o;
        if (!this.getName().equals(that.getName())) {
            return false;
        }
        return isEqual(this.getParent(), that.getParent());
    }

    private boolean isEqual(Object obj1, Object obj2) {
        if (obj1 == null) {
            return obj2 == null;
        }
        return obj1.equals(obj2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName());
    }
}
