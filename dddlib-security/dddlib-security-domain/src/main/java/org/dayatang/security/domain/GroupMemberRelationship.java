package org.dayatang.security.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;

/**
 * 用户组关系。记录用户与用户组、以及用户组之间的归属关系
 * Created by yyang on 15/8/14.
 */
@Entity
@Table(name = "group_member_relationship")
class GroupMemberRelationship extends AbstractEntity {

    @ManyToOne
    private UserGroup group;

    @ManyToOne
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

    /**
     * 根据上级查找
     * @param group 上级
     * @return
     */
    public static List<GroupMemberRelationship> findByGroup(UserGroup group) {
        return getRepository().createCriteriaQuery(GroupMemberRelationship.class)
                .eq("group", group)
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
                .list();
    }

    /**
     * 查找指定用户组的成员
     * @param group
     * @return
     */
    public static List<Actor> findMembersOf(UserGroup group) {
        String jpql = "select o.member from GroupMemberRelationship o where o.group = :group";
        return getRepository().createJpqlQuery(jpql).addParameter("group", group).list();
    }

    /**
     * 查找actor直属的组
     * @param actor 用户
     * @return 包含actor作为其直属成员的用户组
     */
    public static List<UserGroup> getGroupsOfMember(Actor actor) {
        String jpql = "select o.group from GroupMemberRelationship o where o.member = :actor";
        return getRepository().createJpqlQuery(jpql).addParameter("actor", actor).list();
    }


    /**
     * 查找用户直属的组
     * @param user 用户
     * @return 包含用户作为其直属成员的用户组
     */
    public static List<UserGroup> getGroupsOf(User user) {
        String jpql = "select o.group from GroupMemberRelationship o where o.member = :user";
        return getRepository().createJpqlQuery(jpql).addParameter("user", user).list();
    }

    /**
     * 查找用户直属的组
     * @param group 用户
     * @return 包含用户作为其直属成员的用户组
     */
    public static UserGroup getParentOf(UserGroup group) {
        String jpql = "select o.group from GroupMemberRelationship o where o.member = :group";
        return getRepository().createJpqlQuery(jpql).addParameter("group", group).singleResult();
    }
}
