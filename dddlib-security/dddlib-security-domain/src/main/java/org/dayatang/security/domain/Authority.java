package org.dayatang.security.domain;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import java.util.Date;

/**
 * 权限，是许可Permission与角色Role的共同基类。可以将Authority授予给参与者Actor（是用户User和用户组UserGroup的共同基类）
 * Created by yyang on 15/1/19.
 */
@Entity
@Table(name = "security_authorities")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Authority extends AbstractEntity {

    private String name;

    protected Authority() {
    }

    public Authority(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 失效Permission，同时失效与其有关的授权信息
     * @param date 失效日期
     */
    @Override
    public void disable(Date date) {
        for (Authorization authorization : Authorization.findByAuthority(this)) {
            authorization.disable(date);
        }
        super.disable(date);
    }

    /**
     * 删除权限，同时删除与其有关的授权信息
     */
    @Override
    public void remove() {
        for (Authorization authorization : Authorization.findByAuthority(this)) {
            authorization.remove();
        }
        super.remove();
    }
}
