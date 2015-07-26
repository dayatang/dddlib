package org.dayatang.domain.entity;

import java.util.Objects;
import java.util.Random;

import org.dayatang.domain.BaseEntity;

public abstract class Organization extends BaseEntity {

	private static final long serialVersionUID = -545941352163679365L;
	private Integer id;
	private String name;
	private boolean disabled = false;

	public Organization(String name) {
		id = new Random().nextInt();
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public void disable() {
		disabled = true;
	}

	public void enable() {
		disabled = false;
	}

	@Override
	public String[] businessKeys() {
		return new String[] {"name", "level"};
	}
}