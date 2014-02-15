package org.dayatang.ioc.test;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.inject.Named;

@Named("service3")
@TheAnnotation
public class MyService23 implements Service2 {

    private int id = 23;

    @Override
	public String sayHello() {
		return "I am Service 23";
	}

    /**
     * @return a hash code value for this object.
     * @see Object#equals(Object)
     * @see System#identityHashCode
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 43).append(id).toHashCode();
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
        if (!(other instanceof MyService23)) {
            return false;
        }
        MyService23 that = (MyService23) other;
        return new EqualsBuilder().append(this.id, that.id).isEquals();
    }
}
