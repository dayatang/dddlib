package org.dayatang.security.domain;

import org.dayatang.springtest.AbstractSpringIntegrationTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@Ignore
public class UserIntegrationTest extends AbstractSpringIntegrationTest {

    private SecuritySeedDataHelper helper = new SecuritySeedDataHelper();

    @Before
    public void setUp() throws Exception {
        helper.authorizeFunctionToUser("addUser", "user1")
                .authorizeRoleToUser("role1", "user1")
                .authorizeRoleToUser("role1", "user2")
                .authorizeFunctionToRole("removeUser", "role1");
    }

    @After
    public void tearDown() throws Exception {
        //helper.clear();
    }

    @Test
    public void testHasPermission() throws Exception {
        User user = User.getByName("user1");
        FunctionalPermission addUser = FunctionalPermission.getByFunction("addUser");
        FunctionalPermission removeUser = FunctionalPermission.getByFunction("removeUser");
        assertThat(user.hasPermission(addUser), is(true));
        assertThat(user.hasPermission(removeUser), is(true));
    }

    @Test
    public void testHasRole() throws Exception {
        User user = User.getByName("user1");
        Role role = Role.getByName("role1");
        assertTrue(user.hasRole(role));
    }

    @Test
    public void testRemove() throws Exception {
        User user = User.getByName("user2");
        int permissionCount1 = Authorization.findAll(Authorization.class).size();
        user.remove();
        int permissionCount2 = Authorization.findAll(Authorization.class).size();
        assertThat(permissionCount1 - permissionCount2, is(1));
    }

    @Test
    public void testLock() throws Exception {

    }

    @Test
    public void testUnlock() throws Exception {

    }

    @Test
    public void testGetByName() throws Exception {

    }
}