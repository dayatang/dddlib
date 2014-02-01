package com.dayatang.rule.time;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Foo {

	private Long id;

	private String name;

	private Date startDate;

	private Object result;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {

		this.result = result;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Foo == false) {
			return false;
		}
		if (this == o) {
			return true;
		}
		Foo rhs = (Foo) o;
		return new EqualsBuilder().append(getId(), rhs.getId()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", id).append("name", name).toString();
	}

	public String xx(){
		return "xx";
	}
	
	public String xxx(String str){
		return str;
	}
	
	public static String hello(String str) {
		return str;
	}
}
