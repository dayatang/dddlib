package org.dayatang.security.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 功能权限，每一个功能权限权限代表一个细粒度的系统功能，例如服务号消息群发、员工导入、应用下架……等等等等。
 * 粗粒度的系统功能聚合为角色
 * Created by yyang on 15/1/29.
 */
@Entity
@DiscriminatorValue("FUNC")
public class FunctionalPermission extends Permission {

    private String systemFunction;

    protected FunctionalPermission() {
    }

    public FunctionalPermission(String systemFunction) {
        this.systemFunction = systemFunction;
    }

    public String getSystemFunction() {
        return systemFunction;
    }

    public void setSystemFunction(String systemFunction) {
        this.systemFunction = systemFunction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FunctionalPermission)) return false;
        if (!super.equals(o)) return false;

        FunctionalPermission that = (FunctionalPermission) o;

        if (!systemFunction.equals(that.systemFunction)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + systemFunction.hashCode();
        return result;
    }

    public static FunctionalPermission getByFunction(String systemFunction) {
        return getRepository().createCriteriaQuery(FunctionalPermission.class)
                .eq("systemFunction", systemFunction).singleResult();
    }
}
