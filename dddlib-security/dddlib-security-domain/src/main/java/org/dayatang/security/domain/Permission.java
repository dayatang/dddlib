package org.dayatang.security.domain;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * 许可基类
 * Created by yyang on 15/1/13.
 */
@Entity
@Table(name = "security_permissions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Permission extends Authority {
}
