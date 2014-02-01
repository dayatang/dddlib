package com.dayatang.rule.examples;

public class Person {

	private Long id;
	private String name;
	private String gender;
	private int retireAge;

	public Person() {
	}

	public Person(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Person(Long id, String name, String gender) {
		this.id = id;
		this.name = name;
		this.gender = gender;
	}

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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getRetireAge() {
		return retireAge;
	}

	public void setRetireAge(int retireAge) {
		this.retireAge = retireAge;
	}

	@Override
	public String toString() {

		return "id = " + id + " name = " + name;
	}
}
