package org.dayatang.utils;

public class Item {
	private int id;
	private String name;
	private boolean disabled;
	public Item(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Item(int id, String name, boolean disabled) {
		this.id = id;
		this.name = name;
		this.disabled = disabled;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
