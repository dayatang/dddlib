package org.dayatang.ioc.guice;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.ioc.test.Service3;

/**
 * Created by yyang on 14-2-8.
 */
@MyBindingAnnotation
public class MyService31 implements Service3 {
    @Override
    public String name() {
        return "MyService31";
    }

    @Override
    public String sayHello() {
        return "I am Service 3";
    }

    /**
     * @return a hash code value for this object.
     * @see Object#equals(Object)
     * @see System#identityHashCode
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 43).append(name()).toHashCode();
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * @param other the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     * @see #hashCode()
     * @see java.util.HashMap
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MyService31)) {
            return false;
        }
        MyService31 that = (MyService31) other;
        return new EqualsBuilder().append(this.name(), that.name()).isEquals();
    }
}
