package org.openkoala.jbpm.core;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.dayatang.domain.AbstractEntity;

@Entity
@Table
public class JoinAssign extends AbstractEntity {

	private static final long serialVersionUID = 2974044399273194451L;

	private String name;// 会签名

	private String description;// 会签描述

	private String keyChoice;

	private String type;// 会签类型 "NumberJoin" or "PercentJoing"

	private String successResult;// 会签通过条件

	private String monitorVal;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSuccessResult() {
		return successResult;
	}

	public void setSuccessResult(String successResult) {
		this.successResult = successResult;
	}

	public String getKeyChoice() {
		return keyChoice;
	}

	public void setKeyChoice(String keyChoice) {
		this.keyChoice = keyChoice;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMonitorVal() {
		return monitorVal;
	}

	public void setMonitorVal(String monitorVal) {
		this.monitorVal = monitorVal;
	}

	/**
	 * 保存，不允许保存同名称的会签
	 */
	public void save() {
		String jpql = "from JoinAssign j where j.name = ?";
		List<JoinAssign> assigns = getRepository().find(jpql,
				new Object[] { this.name }, JoinAssign.class);
		if (assigns != null && assigns.size() > 0) {
			throw new RuntimeException("已存在的JoinAssign");
		}
		super.save();
	}

	public static JoinAssign getJoinAssignByName(String name) {
		JoinAssign joginAssign = null;
		String jpql = "from JoinAssign j where j.name = ? ";
		List<JoinAssign> assigns = getRepository().find(jpql,
				new Object[] { name }, JoinAssign.class);
		if (assigns == null || assigns.size() == 0) {
			return null;
		} else {
			joginAssign = assigns.get(0);
		}
		return joginAssign;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((keyChoice == null) ? 0 : keyChoice.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((successResult == null) ? 0 : successResult.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JoinAssign other = (JoinAssign) obj;
		if (keyChoice == null) {
			if (other.keyChoice != null)
				return false;
		} else if (!keyChoice.equals(other.keyChoice))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (successResult == null) {
			if (other.successResult != null)
				return false;
		} else if (!successResult.equals(other.successResult))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "JoinAssign [name=" + name + ", keyChoice=" + keyChoice
				+ ", type=" + type + ", successResult=" + successResult + "]";
	}

}
