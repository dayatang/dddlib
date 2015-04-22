package org.dayatang.security.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by yyang on 15/2/11.
 */
@Entity
@DiscriminatorValue("EMP_USER")
public class MyUser extends User {
    public MyUser() {
    }

    public MyUser(String name, String password) {
        super(name, password);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 23).append(getName()).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MyUser)) {
            return false;
        }
        MyUser that = (MyUser) other;
        return new EqualsBuilder().append(this.getName(), that.getName()).isEquals();
    }

    @Override
    public String toString() {
        return "[User]: " + getName();
    }

}
