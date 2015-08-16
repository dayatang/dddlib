package org.dayatang.security.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 用户组关系。记录用户与用户组、以及用户组之间的归属关系
 * Created by yyang on 15/8/14.
 */
@Entity
@Table(name = "security_group_member_relationship")
class GroupMemberRelationship extends AbstractEntity {

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private UserGroup group;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Actor member;

    public GroupMemberRelationship() {
    }

    public GroupMemberRelationship(UserGroup group, Actor member) {
        this.group = group;
        this.member = member;
    }

    public UserGroup getGroup() {
        return group;
    }

    public Actor getMember() {
        return member;
    }

    @Override
    public String[] businessKeys() {
        return new String[] {"group", "member"};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GroupMemberRelationship)) {
            return false;
        }
        GroupMemberRelationship that = (GroupMemberRelationship) o;
        return Objects.equals(getGroup(), that.getGroup()) &&
                Objects.equals(getMember(), that.getMember());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGroup(), getMember());
    }

    @Override
    public String toString() {
        return "GroupMemberRelationship{" +
                "group=" + group +
                ", member=" + member +
                '}';
    }

    /**
     * 根据上级查找
     * @param group 上级
     * @return
     */
    public static List<GroupMemberRelationship> findByGroup(UserGroup group) {
        return getRepository().createCriteriaQuery(GroupMemberRelationship.class)
                .eq("group", group)
                .isFalse("disabled")
                .list();
    }

    /**
     * 根据下级查找
     * @param actor 下级
     * @return
     */
    public static List<GroupMemberRelationship> findByMember(Actor actor) {
        return getRepository().createCriteriaQuery(GroupMemberRelationship.class)
                .eq("member", actor)
                .isFalse("disabled")
                .list();
    }

    /**
     * 查找指定用户组的成员
     * @param group
     * @return
     */
    public static Set<Actor> getMembersOf(UserGroup group) {
        Set<Actor> results = new HashSet<Actor>();
        for (GroupMemberRelationship each : findByGroup(group)) {
            results.add(each.getMember());
        }
        return results;
    }

    /**
     * 查找actor直属的组
     * @param actor 用户
     * @return 包含actor作为其直属成员的用户组
     */
    public static Set<UserGroup> getGroupsOf(Actor actor) {
        Set<UserGroup> results = new HashSet<UserGroup>();
        for (GroupMemberRelationship each : findByMember(actor)) {
            results.add(each.getGroup());
        }
        return results;
    }

    /**
     * 查找用户直属的组
     * @param group 用户
     * @return 包含用户作为其直属成员的用户组
     */
    public static UserGroup getParentOf(UserGroup group) {
        GroupMemberRelationship relationship = getRepository().createCriteriaQuery(GroupMemberRelationship.class)
                .eq("member", group)
                .isFalse("disabled")
                .singleResult();
        return relationship == null ? null : relationship.getGroup();
    }
}
