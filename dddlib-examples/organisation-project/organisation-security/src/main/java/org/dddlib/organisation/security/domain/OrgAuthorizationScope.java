package org.dddlib.organisation.security.domain;

import org.dayatang.security.domain.AuthorizationScope;
import org.dddlib.organisation.domain.Organization;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 机构授权范围。由一个机构代表的授权范围。机构间的直线管理关系映射到授权范围的包含与被包含关系
 * Created by yyang on 15/8/17.
 */
@Entity
@DiscriminatorValue("ORG")
public class OrgAuthorizationScope extends AuthorizationScope {

    @NotNull
    @ManyToOne
    private Organization organization;

    protected OrgAuthorizationScope() {
    }

    public OrgAuthorizationScope(Organization organization) {
        super(organization.getName());
        this.organization = organization;
    }

    public OrgAuthorizationScope(String name, Organization organization) {
        super(name);
        this.organization = organization;
    }

    public Organization getOrganization() {
        return organization;
    }

    @Override
    public OrgAuthorizationScope getParent() {
        Date now = new Date();
        Organization parent = organization.getParent(now);
        if (parent == null) {
            return null;
        }
        return of(parent);
    }

    @Override
    public Set<OrgAuthorizationScope> getChildren() {
        Date now = new Date();
        Set<OrgAuthorizationScope> results = new HashSet<OrgAuthorizationScope>();
        for (Organization child : organization.getChildren(now)) {
            results.add(of(child));
        }
        return results;
    }

    /**
     * 根据机构获取机构授权范围。
     * @param organization 机构
     * @return 机构授权范围。如果不存在则返回null
     */
    public static OrgAuthorizationScope getByOrganization(Organization organization) {
        return createCriteriaQuery(OrgAuthorizationScope.class)
                .eq("organization", organization)
                .isFalse("disabled")
                .singleResult();
    }

    /**
     * 根据代表指定机构的机构授权范围。如果不存在则创建一个
     * @param organization 机构
     * @return 代表该机构的机构授权范围
     */
    public static OrgAuthorizationScope of(Organization organization) {
        OrgAuthorizationScope scope = createCriteriaQuery(OrgAuthorizationScope.class)
                .eq("organization", organization)
                .isFalse("disabled")
                .singleResult();
        if (scope == null) {
            scope = new OrgAuthorizationScope(organization);
            scope.save();
        }
        return scope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrgAuthorizationScope)) {
            return false;
        }
        OrgAuthorizationScope that = (OrgAuthorizationScope) o;
        return Objects.equals(getOrganization(), that.getOrganization());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrganization());
    }
}
