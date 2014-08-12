package org.dddlib.organisation.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.dayatang.utils.Assert;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("Employee")
public class Employee extends Party {

    private static final long serialVersionUID = -7339118476080239701L;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    protected Employee() {
    }

    public Employee(String name, Person person) {
        super(name);
        Assert.notNull(person, "Person is null!");
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    public Set<Post> getPosts(Date date) {
        return new HashSet<Post>(PostHolding.findPostsOfEmployee(this, date));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(getName()).build();
    }

}
