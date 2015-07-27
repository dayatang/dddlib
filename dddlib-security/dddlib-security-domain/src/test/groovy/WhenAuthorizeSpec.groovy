import org.dayatang.domain.InstanceFactory
import org.dayatang.ioc.spring.factory.SpringInstanceProvider
import org.dayatang.security.domain.Permission
import org.dayatang.security.domain.Role
import org.dayatang.security.domain.User
import org.junit.Ignore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.transaction.AfterTransaction
import org.springframework.test.context.transaction.BeforeTransaction
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification
/**
 * Created by yyang on 15/3/22.
 */
@ContextConfiguration(locations = "/spring/applicationContext.xml")
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
class WhenAuthorizeSpec extends Specification {

    @Autowired
    ApplicationContext ctx

    @BeforeTransaction
    def initTx() {
        InstanceFactory.setInstanceProvider(new SpringInstanceProvider(ctx))
    }

    @AfterTransaction
    def afterTx() {
        InstanceFactory.setInstanceProvider(null)
    }

    @Transactional
    def "用户应拥有直接分配给他的角色"() {
        given:
            User user1 = User.create("user1")
            Role role1 = Role.create("role1")
            Role role2 = Role.create("role2")
            Role role3 = Role.create("role3")

        when:
            user1.grantAuthorities(role1, role2)
        then:
            user1.hasRole(role3) == false
    }

    @Transactional
    def "用户应拥有直接分配给他的权限"() {
        given:
            User user1 = User.create("user1")
            Permission permission1 = Permission.create("permission1")
            Permission permission2 = Permission.create("permission2")
            Permission permission3 = Permission.create("permission3")
        when:
            user1.grantAuthorities(permission1, permission2)
        then:
            user1.hasPermission(permission3) == false
    }

    @Ignore
    @Transactional
    def "用户应拥有直接分配给他的权限和从角色继承而来的权限"() {
        given:
            User user1 = User.create("user1")
            Role role1 = Role.create("role1")
            Role role2 = Role.create("role2")
            Permission permission1 = Permission.create("permission1")
            Permission permission2 = Permission.create("permission2")
            Permission permission3 = Permission.create("permission3")
            Permission permission4 = Permission.create("permission4")

            role1.addPermissions(permission1)
            role2.addPermissions(permission2)
        when:
            user1.grantAuthorities(role1, role2, permission3)
        then:
            user1.hasPermission(permission1)
            user1.hasPermission(permission2)
            user1.hasPermission(permission3)
            user1.hasPermission(permission4) == false
    }
}
