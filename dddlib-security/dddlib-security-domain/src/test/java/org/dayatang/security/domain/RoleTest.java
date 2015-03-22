package org.dayatang.security.domain;

import org.dayatang.springtest.AbstractSpringIntegrationTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class RoleTest extends AbstractSpringIntegrationTest {

    private SecuritySeedDataHelper helper = new SecuritySeedDataHelper();

    @Before
    public void setUp() throws Exception {
        helper.authorizeFunctionToRole("addUser", "role1");
    }

    @After
    public void tearDown() throws Exception {
        //helper.clear();
    }

    @Test
    public void testHasPermission() throws Exception {
        Role role = Role.getByName("role1");
        FunctionalPermission permission = FunctionalPermission.getByFunction("addUser");
        assertThat(role.hasPermission(permission), is(true));
    }

    @Test
    public void testRemove() throws Exception {
        helper.authorizeRoleToUser("role2", "user1");
        Role role = Role.getByName("role2");
        assertFalse(Authorization.findByAuthority(role).isEmpty());
        role.remove();
        assertTrue(Authorization.findByAuthority(role).isEmpty());
    }

    @Test
    public void testDisable() throws Exception {
        helper.authorizeRoleToUser("role2", "user1");
        Role role = Role.getByName("role2");
        assertFalse(Authorization.findByAuthority(role).isEmpty());
        role.disable(new Date());
        assertTrue(Authorization.findByAuthority(role).isEmpty());
    }

    @Test
    public void testGetPermissions() throws Exception {

    }

    @Test
    public void testGetByName() throws Exception {

    }
}