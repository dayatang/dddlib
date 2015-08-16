package org.dayatang.security.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 范围关系，记录大范围包含小范围这样的关系
 * Created by yyang on 15/8/16.
 */
@Entity
@Table(name = "security_scope_relationship")
public class ScopeRelation extends AbstractEntity {

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private AuthorityScope parent;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private AuthorityScope child;

    protected ScopeRelation() {
    }

    public ScopeRelation(AuthorityScope parent, AuthorityScope child) {
        this.parent = parent;
        this.child = child;
    }

    public AuthorityScope getParent() {
        return parent;
    }

    public AuthorityScope getChild() {
        return child;
    }

    /**
     * 根据上级查找
     * @param scope 上级
     * @return
     */
    public static List<ScopeRelation> findByParent(AuthorityScope scope) {
        return getRepository().createCriteriaQuery(ScopeRelation.class)
                .eq("parent", scope)
                .isFalse("disabled")
                .list();
    }

    /**
     * 根据下级查找
     * @param scope 下级
     * @return
     */
    public static ScopeRelation getByChild(AuthorityScope scope) {
        return getRepository().createCriteriaQuery(ScopeRelation.class)
                .eq("child", scope)
                .isFalse("disabled")
                .singleResult();
    }

    /**
     * 查找指定用户组的成员
     * @param scope
     * @return
     */
    public static Set<AuthorityScope> getChildrenOf(AuthorityScope scope) {
        Set<AuthorityScope> results = new HashSet<AuthorityScope>();
        for (ScopeRelation each : findByParent(scope)) {
            results.add(each.getChild());
        }
        return results;
    }

    /**
     * 查找指定用户组的成员
     * @param scope
     * @return
     */
    public static AuthorityScope getParentOf(AuthorityScope scope) {
        ScopeRelation relation = getByChild(scope);
        return relation == null ? null : relation.getParent();
    }

}
