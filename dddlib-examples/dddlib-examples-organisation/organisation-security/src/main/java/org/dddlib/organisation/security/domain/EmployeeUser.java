package org.dddlib.organisation.security.domain;

import org.dayatang.security.domain.User;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by yyang on 15/7/10.
 */
@Entity
@DiscriminatorValue("EMP_USER")
public class EmployeeUser extends User {
}
