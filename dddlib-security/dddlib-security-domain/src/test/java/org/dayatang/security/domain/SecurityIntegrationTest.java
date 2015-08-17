package org.dayatang.security.domain;

import org.dayatang.springtest.AbstractSpringIntegrationTest;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by yyang on 15/8/16.
 */
public class SecurityIntegrationTest extends AbstractSpringIntegrationTest {

    private UserGroup grandGroup;
    private UserGroup parentGroup;
    private UserGroup group1;
    private UserGroup group2;
    private User user1;

    private Role grandRole;
    private Role parentRole;
    private Role group1Role;
    private Role group2Role;
    private Role userRole;

    private Permission grandPermission;
    private Permission parentPermission;
    private Permission group1Permission;
    private Permission group2Permission;
    private Permission grandRolePermission;
    private Permission parentRolePermission;
    private Permission group1RolePermission;
    private Permission group2RolePermission;
    private Permission userRolePermission;
    private Permission userPermission;

    @Before
    public void setUp() {
        grandGroup = UserGroup.create("grand");
        parentGroup = grandGroup.createChild("parent");
        group1 = parentGroup.createChild("group1");
        group2 = parentGroup.createChild("group2");

        user1 = User.create("user1");

        group1.addMember(user1);
        group2.addMember(user1);


        grandRole = Role.create("grandRole");
        parentRole = Role.create("parentRole");
        group1Role = Role.create("group1Role");
        group2Role = Role.create("group2Role");
        userRole = Role.create("userRole");

        grandPermission = Permission.create("grandPermission");
        parentPermission = Permission.create("parentPermission");
        group1Permission = Permission.create("group1Permission");
        group2Permission = Permission.create("group2Permission");
        grandRolePermission = Permission.create("grandRolePermission");
        parentRolePermission = Permission.create("parentRolePermission");
        group1RolePermission = Permission.create("group1RolePermission");
        group2RolePermission = Permission.create("group2RolePermission");
        userRolePermission = Permission.create("userRolePermission");
        userPermission = Permission.create("userPermission");

        grandRole.addPermissions(grandRolePermission);
        parentRole.addPermissions(parentRolePermission);
        group1Role.addPermissions(group1RolePermission);
        group2Role.addPermissions(group2RolePermission);
        userRole.addPermissions(userRolePermission);
    }

    /**
     * 断言用户除了拥有直接分配给他的角色和权限之外，还拥有从用户组及各层级上级用户组继承而来的角色和权限
     */
    @Test
    public void userShouldInheritItsRoleAndGroupsPermission() {

        grandGroup.grantAuthorities(grandRole, grandPermission);
        parentGroup.grantAuthorities(parentRole, parentPermission);
        group1.grantAuthorities(group1Role, group1Permission);
        group2.grantAuthorities(group2Role, group2Permission);

        user1.grantAuthorities(userRole, userPermission);

        assertThat(user1.getAllRoles(), hasItems(
                grandRole, parentRole, group1Role, group2Role, userRole
        ));

        assertThat(user1.getAllPermissions(), hasItems(
                grandPermission,
                parentPermission,
                group1Permission,
                group2Permission,
                grandRolePermission,
                parentRolePermission,
                group1RolePermission,
                group2RolePermission,
                userRolePermission,
                userPermission
        ));
    }

    /**
     * 在授权范围内授权。断言如果范围A包含范围B，范围B包含范围C，如果用户在范围A内被授权X，
     * 在范围B内被授权Y，那么他在范围C内也具有权限X和Y。
     */
    public void scopedAuthorization() {

        DefaultAuthorityScope grandScope = DefaultAuthorityScope.create("grandScope");
        DefaultAuthorityScope parentScope = grandScope.createChild("parentScope");
        DefaultAuthorityScope childScope = parentScope.createChild("childScope");

        grandGroup.grantAuthorities(grandScope, grandRole, grandPermission);
        parentGroup.grantAuthorities(parentScope, parentRole, parentPermission);
        group1.grantAuthorities(childScope, group1Role, group1Permission);
        group2.grantAuthorities(grandScope, group2Role, group2Permission);
        user1.grantAuthorities(childScope, userRole, userPermission);

        Set<Role> roles = user1.getAllRoles(parentScope);

        assertThat(roles, hasItems(grandRole, parentRole, group2Role));
        assertThat(roles, not(hasItems(group1Role, userRole)));

        Set<Permission> permissions = user1.getAllPermissions();
        assertThat(permissions, hasItems(
                grandPermission,
                parentPermission,
                group2Permission,
                grandRolePermission,
                parentRolePermission,
                group2RolePermission
        ));

        assertThat(permissions, not(hasItems(
                group1Permission,
                group1RolePermission,
                userRolePermission,
                userPermission
        )));
    }
}
