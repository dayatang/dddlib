package org.dayatang.domain.entity;

public class Dept extends Organization {

	private int level;

	public Dept(String name, int level) {
		super(name);
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public String[] businessKeys() {
		return new String[] { "name", "level" };
	}
}