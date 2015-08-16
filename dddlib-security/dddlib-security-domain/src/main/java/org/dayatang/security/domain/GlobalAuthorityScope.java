package org.dayatang.security.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by yyang on 15/7/27.
 */
@Entity
@DiscriminatorValue("GLOBAL")
public class GlobalAuthorityScope extends AuthorityScope {

    protected GlobalAuthorityScope() {
        super("GLOBAL");
    }

    @Override
    public String toString() {
        return "GlobalAuthorityScope{}";
    }

    @Override
    public AuthorityScope getParent() {
        return null;
    }

    @Override
    public Set<AuthorityScope> getChildren() {
        return new HashSet<AuthorityScope>();
    }

    public static final GlobalAuthorityScope get() {
        GlobalAuthorityScope result = getRepository().createCriteriaQuery(GlobalAuthorityScope.class).singleResult();
        if (result == null) {
            result = new GlobalAuthorityScope();
            result.save();
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GlobalAuthorityScope)) {
            return false;
        }
        GlobalAuthorityScope that = (GlobalAuthorityScope) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
