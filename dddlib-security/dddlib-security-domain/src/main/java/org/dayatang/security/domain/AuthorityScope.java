package org.dayatang.security.domain;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * 授权范围，指定用户或用户组的授权范围，例如用户张三拥有招聘权限，但仅限于广州分公司的招聘，“广州分公司”就是授权范围
 * Created by yyang on 15/1/23.
 */
@Entity
@Table(name = "security_authority_scopes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AuthorityScope extends AbstractEntity {
}
