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
@Table(name = "user_group_relationship")
class GroupMemberRelationship extends AbstractEntity {

    @ManyToOne
    private UserGroup parent;

    @ManyToOne
    private Actor child;

    public GroupMemberRelationship() {
    }

    public GroupMemberRelationship(UserGroup parent, Actor child) {
        this.parent = parent;
        this.child = child;
    }

    public UserGroup getParent() {
        return parent;
    }

    public Actor getChild() {
        return child;
    }

    /**
     * 根据上级查找
     * @param group 上级
     * @return
     */
    public static List<GroupMemberRelationship> findByParent(UserGroup group) {
        return getRepository().createCriteriaQuery(GroupMemberRelationship.class)
                .eq("parent", group)
                .list();
    }

    /**
     * 根据下级查找
     * @param actor 下级
     * @return
     */
    public static List<GroupMemberRelationship> findByChild(Actor actor) {
        return getRepository().createCriteriaQuery(GroupMemberRelationship.class)
                .eq("child", actor)
                .list();
    }

    /**
     * 查找指定用户组的成员
     * @param group
     * @return
     */
    public static List<Actor> findChildrenOf(UserGroup group) {
        String jpql = "select o.child from GroupMemberRelationship o where o.parent = :group";
        return getRepository().createJpqlQuery(jpql).addParameter("group", group).list();
    }

    /**
     * 查找actor直属的组
     * @param actor 用户
     * @return 包含actor作为其直属成员的用户组
     */
    public static List<UserGroup> getParentsOf(Actor actor) {
        String jpql = "select o.parent from GroupMemberRelationship o where o.child = :actor";
        return getRepository().createJpqlQuery(jpql).addParameter("actor", actor).list();
    }


    /**
     * 查找用户直属的组
     * @param user 用户
     * @return 包含用户作为其直属成员的用户组
     */
    public static List<UserGroup> getGroupsOf(User user) {
        String jpql = "select o.parent from GroupMemberRelationship o where o.child = :user";
        return getRepository().createJpqlQuery(jpql).addParameter("user", user).list();
    }

    /**
     * 查找用户直属的组
     * @param group 用户
     * @return 包含用户作为其直属成员的用户组
     */
    public static UserGroup getParentOf(UserGroup group) {
        String jpql = "select o.parent from GroupMemberRelationship o where o.child = :group";
        return getRepository().createJpqlQuery(jpql).addParameter("group", group).singleResult();
    }
}
