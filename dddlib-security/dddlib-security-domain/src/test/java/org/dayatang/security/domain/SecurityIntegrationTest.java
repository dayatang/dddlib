package org.dayatang.security.domain;

import org.dayatang.springtest.AbstractSpringIntegrationTest;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

/**
 * Created by yyang on 15/8/16.
 */
public class SecurityIntegrationTest extends AbstractSpringIntegrationTest {

    /**
     * 用户应用有直接分配给他的角色和权限，以及从用户组及上级用户组继承而来的角色和权限
     */
    @Test
    public void userShouldInheritItsRoleAndGroupsPermission() {
        UserGroup grandGroup = UserGroup.create("grand");
        UserGroup parentGroup = grandGroup.createChild("parent");
        UserGroup group1 = parentGroup.createChild("group1");
        UserGroup group2 = parentGroup.createChild("group2");

        User user1 = User.create("user1");

        Role grandRole = Role.create("grandRole");
        Role parentRole = Role.create("parentRole");
        Role group1Role = Role.create("group1Role");
        Role group2Role = Role.create("group2Role");
        Role userRole = Role.create("userRole");

        Permission grandPermission = Permission.create("grandPermission");
        Permission parentPermission = Permission.create("parentPermission");
        Permission group1Permission = Permission.create("group1Permission");
        Permission group2Permission = Permission.create("group2Permission");
        Permission grandRolePermission = Permission.create("grandRolePermission");
        Permission parentRolePermission = Permission.create("parentRolePermission");
        Permission group1RolePermission = Permission.create("group1RolePermission");
        Permission group2RolePermission = Permission.create("group2RolePermission");
        Permission userRolePermission = Permission.create("userRolePermission");
        Permission userPermission = Permission.create("userPermission");

        group1.addMember(user1);
        group2.addMember(user1);

        grandRole.addPermissions(grandRolePermission);
        parentRole.addPermissions(parentRolePermission);
        group1Role.addPermissions(group1RolePermission);
        group2Role.addPermissions(group2RolePermission);
        userRole.addPermissions(userRolePermission);

        grandGroup.grantAuthorities(grandRole, grandPermission);
        parentGroup.grantAuthorities(parentRole, parentPermission);
        group1.grantAuthorities(group1Role, group1Permission);
        group2.grantAuthorities(group2Role, group2Permission);

        user1.grantAuthorities(userRole, userPermission);

        assertThat(user1.getAllRoles(), hasItems(
                grandRole, parentRole, group2Role, group2Role, userRole
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

}
