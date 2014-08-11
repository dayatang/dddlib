package org.dddlib.organisation.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@DiscriminatorValue("Employee")
public class Employee extends Party {

	private static final long serialVersionUID = -7339118476080239701L;

	@ManyToOne
	@JoinColumn(name = "person_id")
	private Person person;

	protected Employee() {
	}

	public Employee(String name) {
		super(name);
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	public Set<Post> getPosts(Date date) {
		return new HashSet<Post>(PostHolding.findPostsOfEmployee(this, date));
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(getName()).build();
	}

}
