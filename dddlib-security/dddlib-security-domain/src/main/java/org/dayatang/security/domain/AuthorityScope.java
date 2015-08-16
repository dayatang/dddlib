package org.dayatang.security.domain;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import java.util.Set;

/**
 * 授权范围，指定用户或用户组的授权范围，例如用户张三拥有招聘权限，但仅限于广州分公司的招聘，“广州分公司”就是授权范围
 * Created by yyang on 15/1/23.
 */
@Entity
@Table(name = "security_authority_scopes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AuthorityScope extends AbstractEntity {

    private String name;

    protected AuthorityScope() {
    }

    public AuthorityScope(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String[] businessKeys() {
        return new String[] {"name"};
    }

    /**
     * 获取所属的上级范围
     * @return 如果找到，返回之，否则返回null
     */
    public AuthorityScope getParent() {
        return AuthorityScopeRelation.getParentOf(this);
    }

    /**
     * 获取下属范围
     * @return 下属范围。
     */
    public Set<AuthorityScope> getChildren() {
        return AuthorityScopeRelation.getChildrenOf(this);
    }
}
