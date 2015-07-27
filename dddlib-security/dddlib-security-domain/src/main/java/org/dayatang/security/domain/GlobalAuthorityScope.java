package org.dayatang.security.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by yyang on 15/7/27.
 */
@Entity
@DiscriminatorValue("GLOBAL")
public class GlobalAuthorityScope extends AuthorityScope {

    protected GlobalAuthorityScope() {
        super("GLOBAL");
    }

    public static final GlobalAuthorityScope get() {
        GlobalAuthorityScope result = getRepository().createCriteriaQuery(GlobalAuthorityScope.class).singleResult();
        if (result == null) {
            result = new GlobalAuthorityScope();
            result.save();
        }
        return result;
    }
}
