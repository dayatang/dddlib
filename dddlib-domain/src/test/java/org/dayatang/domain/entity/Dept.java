package org.dayatang.domain.entity;

import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Dept)) {
			return false;
		}
		Dept that = (Dept) o;
		return Objects.equals(getName(), that.getName()) && Objects.equals(getLevel(), that.getLevel());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName(), getLevel());
	}
}