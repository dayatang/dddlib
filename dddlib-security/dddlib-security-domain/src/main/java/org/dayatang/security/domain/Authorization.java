package org.dayatang.security.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.*;

/**
 * 授权信息，记录对参与者的权限授予
 * Created by yyang on 15/1/13.
 */
@Entity
@Table(name = "security_authorizations")
public class Authorization extends AbstractEntity {

    //参与者
    @ManyToOne
    private Actor actor;

    //权限
    @ManyToOne
    private Authority authority;

    //授权范围
    @ManyToOne
    private AuthorizationScope scope;

    protected Authorization() {
    }

    public Authorization(Actor actor, Authority authority) {
        this(actor, authority, GlobalAuthorizationScope.get());
    }

    public Authorization(Actor actor, Authority authority, AuthorizationScope scope) {
        this.actor = actor;
        this.authority = authority;
        this.scope = scope;
    }

    public Actor getActor() {
        return actor;
    }

    public Authority getAuthority() {
        return authority;
    }

    @Override
    public String[] businessKeys() {
        return new String[] {"actor", "authority", "scope"};
    }

    /**
     * 根据参与者查找授权信息
     * @param actor 参与者
     * @return 参与者的所有有效授权信息
     */
    static List<Authorization> findByActor(Actor actor) {
        return getRepository().createCriteriaQuery(Authorization.class)
                .eq("actor", actor)
                .eq("disabled", false)
                .list();
    }

    static List<Authorization> findByActor(Actor actor, AuthorizationScope scope) {
        return getRepository().createCriteriaQuery(Authorization.class)
                .eq("actor", actor)
                .eq("scope", scope)
                .eq("disabled", false)
                .list();
    }

    /**
     * 根据权限查找授权信息
     * @param authority 权限
     * @return 权限的所有有效授权信息
     */
    static List<Authorization> findByAuthority(Authority authority) {
        return getRepository().createCriteriaQuery(Authorization.class)
                .eq("authority", authority)
                .eq("disabled", false)
                .list();
    }

    /**
     * 查找指定范围内被授予指定参与者的指定类型的所有权力
     * @param actor 参与者
     * @param scope 授权范围
     * @param authorityClass 权力的类
     * @param <T> 权力的类型
     * @return 该参与者的权限集合
     */
    static <T extends Authority> Set<T> getAuthoritiesOfActor(Actor actor, AuthorizationScope scope, Class<T> authorityClass) {
        Set<T> results = new HashSet<T>();
        for (Authorization authorization : findByActor(actor, scope)) {
            Authority authority = authorization.getAuthority();
            if (authorityClass.isInstance(authority)) {
                results.add((T) authority);
            }
        }
        return results;
    }

    static Authorization get(Actor actor, Authority authority, AuthorizationScope scope) {
        return getRepository().createCriteriaQuery(Authorization.class)
                .eq("actor", actor)
                .eq("authority", authority)
                .eq("scope", scope)
                .eq("disabled", false)
                .singleResult();
    }

    static void grantAuthority(Actor actor, Authority authority, AuthorizationScope scope) {
        if (Authorization.get(actor, authority, scope) != null) {
            throw new DuplicateAuthorizationException();
        }
        new Authorization(actor, authority, scope).save();
    }

    static void withdrawAuthority(Actor actor, Authority authority, AuthorizationScope scope) {
        Authorization authorization = Authorization.get(actor, authority, scope);
        if (authorization != null) {
            authorization.remove();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Authorization)) {
            return false;
        }
        Authorization that = (Authorization) o;
        return Objects.equals(getActor(), that.getActor()) &&
                Objects.equals(getAuthority(), that.getAuthority()) &&
                Objects.equals(scope, that.scope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getActor(), getAuthority(), scope);
    }
}
