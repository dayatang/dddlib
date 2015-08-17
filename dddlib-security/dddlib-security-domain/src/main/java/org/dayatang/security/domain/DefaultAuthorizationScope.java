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
public class DefaultAuthorizationScope extends AuthorizationScope {

    @ManyToOne
    @JoinTable(name = "security_authorization_scope_relationship",
        joinColumns = @JoinColumn(name = "parent_id"),
        inverseJoinColumns = @JoinColumn(name = "child_id"))
    private DefaultAuthorizationScope parent;

    @OneToMany(mappedBy = "parent")
    private Set<DefaultAuthorizationScope> children = new HashSet<DefaultAuthorizationScope>();

    public DefaultAuthorizationScope() {
    }

    public DefaultAuthorizationScope(String name) {
        super(name);
    }

    @Override
    public AuthorizationScope getParent() {
        return parent;
    }

    @Override
    public Set<DefaultAuthorizationScope> getChildren() {
        return children;
    }

    public void addChildren(DefaultAuthorizationScope... scopes) {
        for (DefaultAuthorizationScope scope : scopes) {
            if (contains(scope)) {
                continue;
            }
            scope.parent = this;
            scope.save();
            children.add(scope);
        }
    }

    public void removeChildren(DefaultAuthorizationScope... scopes) {
        Date now = new Date();
        for (DefaultAuthorizationScope scope : scopes) {
            if (!children.contains(scope)) {
                continue;
            }
            scope.remove();
            children.remove(scope);
        }
    }

    public DefaultAuthorizationScope createChild(String name) {
        DefaultAuthorizationScope child = new DefaultAuthorizationScope(name);
        addChildren(child);
        return child;
    }

    public static DefaultAuthorizationScope create(String scopeName) {
        DefaultAuthorizationScope scope = new DefaultAuthorizationScope(scopeName);
        scope.save();
        return scope;
    }
}
