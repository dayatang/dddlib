package org.dayatang.security.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 缺省授权范围。在数据库中记录范围的包含与被包含关系。
 * Created by yyang on 15/8/16.
 */
@Entity
@DiscriminatorValue("DEFAULT")
public class DefaultAuthorityScope extends AuthorityScope {

    @ManyToOne
    @JoinTable(name = "security_authorization_scope_relationship",
        joinColumns = @JoinColumn(name = "parent_id"),
        inverseJoinColumns = @JoinColumn(name = "child_id"))
    private DefaultAuthorityScope parent;

    @OneToMany(mappedBy = "parent")
    private Set<DefaultAuthorityScope> children = new HashSet<DefaultAuthorityScope>();

    public DefaultAuthorityScope() {
    }

    public DefaultAuthorityScope(String name) {
        super(name);
    }

    @Override
    public AuthorityScope getParent() {
        return parent;
    }

    @Override
    public Set<DefaultAuthorityScope> getChildren() {
        return children;
    }

    public void addChildren(DefaultAuthorityScope... scopes) {
        for (DefaultAuthorityScope scope : scopes) {
            if (contains(scope)) {
                continue;
            }
            scope.parent = this;
            scope.save();
            children.add(scope);
        }
    }

    public void removeChildren(DefaultAuthorityScope... scopes) {
        Date now = new Date();
        for (DefaultAuthorityScope scope : scopes) {
            if (!children.contains(scope)) {
                continue;
            }
            scope.remove();
            children.remove(scope);
        }
    }

    public DefaultAuthorityScope createChild(String name) {
        DefaultAuthorityScope child = new DefaultAuthorityScope(name);
        addChildren(child);
        return child;
    }

    public static DefaultAuthorityScope create(String scopeName) {
        DefaultAuthorityScope scope = new DefaultAuthorityScope(scopeName);
        scope.save();
        return scope;
    }
}
