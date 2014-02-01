package com.dayatang.utils;

public class Item {
	private int id;
	private String name;
	private boolean disabled;
	public Item(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Item(int id, String name, boolean disabled) {
		super();
		this.id = id;
		this.name = name;
		this.disabled = disabled;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
