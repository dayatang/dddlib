package org.dayatang.security.domain;

import org.dayatang.springtest.AbstractSpringIntegrationTest;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by yyang on 15/8/14.
 */
public class GroupMemberRelationshipIntegrationTest extends AbstractSpringIntegrationTest {

    private User user1, user2;
    private UserGroup group1, group2;

    @Before
    public void setUp() throws Exception {
        user1 = new User("zhang", "123");
        user1.save();
        user2 = new User("li", "456");
        user2.save();
        group1 = new UserGroup("group1");
        group1.save();
        group2 = new UserGroup("group2");
        group2.save();
        new GroupMemberRelationship(group1, user1).save();
        new GroupMemberRelationship(group2, group1).save();
        new GroupMemberRelationship(group2, user2).save();
    }

    @Test
    public void testFindChildrenOf() throws Exception {
        List<Actor> children = GroupMemberRelationship.findMembersOf(group2);
        assertThat(children, hasItem(user2));
        assertThat(children, not(hasItem(user1)));
    }

    @Test
    public void testGetGroupsOf() throws Exception {
        List<UserGroup> groups = GroupMemberRelationship.getGroupsOf(user1);
        assertThat(groups, hasItem(group1));
        assertThat(groups, not(hasItem(group2)));
    }

    @Test
    public void testGetParentOf() throws Exception {
        assertThat(GroupMemberRelationship.getParentOf(group1), is(group2));
    }
}