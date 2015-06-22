package org.dayatang.security.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

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
    private AuthorityScope scope;

    protected Authorization() {
    }

    public Authorization(Actor actor, Authority authority) {
        this.actor = actor;
        this.authority = authority;
    }

    public Actor getActor() {
        return actor;
    }

    public Authority getAuthority() {
        return authority;
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
     * 查找直接授予指定参与者的权限
     * @param actor 参与者
     * @return 参与者的权限
     */
    public static List<Authority> findAuthoritiesByActor(Actor actor) {
        List<Authority> results = new ArrayList<Authority>();
        for (Authorization authorization : findByActor(actor)) {
            results.add(authorization.getAuthority());
        }
        return results;
    }

    public static void when(ActorDisabledEvent event) {
        for (Authorization authorization : Authorization.findByActor(event.getActor())) {
            authorization.disable(event.occurredOn());
        }
    }

    public static Authorization get(Actor actor, Authority authority) {
        return getRepository().createCriteriaQuery(Authorization.class)
                .eq("actor", actor)
                .eq("authority", authority)
                .eq("disabled", false)
                .singleResult();
    }
}
