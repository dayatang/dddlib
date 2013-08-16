package org.openkoala.jbpm.application.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JoinAssignVO implements Serializable {

	private static final long serialVersionUID = -1675622734444870181L;

	private Long id;

	private String description;

	private String name;

	private String successResult;

	private String type;

	private String keyChoice;

	private String monitorVal;

	private Map<String, Integer> choiceMap;

	private int allCount;

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setSuccessResult(String successResult) {
		this.successResult = successResult;
	}

	public String getSuccessResult() {
		return this.successResult;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setKeyChoice(String keyChoice) {
		this.keyChoice = keyChoice;
	}

	public String getKeyChoice() {
		return this.keyChoice;
	}

	public void setMonitorVal(String monitorVal) {
		this.monitorVal = monitorVal;
	}

	public String getMonitorVal() {
		return this.monitorVal;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		JoinAssignVO other = (JoinAssignVO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public void addChoice(String value) {
		if (choiceMap == null) {
			choiceMap = new HashMap<String, Integer>();
		}
		int val = 0;
		if (choiceMap.containsKey(value)) {
			val = choiceMap.get(value);

		}
		val = val + 1;
		choiceMap.put(value, val);
	}

	public Map<String, Integer> getChoiceMap() {
		return choiceMap;
	}

	public void setChoiceMap(Map<String, Integer> choiceMap) {
		this.choiceMap = choiceMap;
	}

	public String queryIsSuccess() {

		Set<String> keys = choiceMap.keySet();

		for (String key : keys) {

			int val = choiceMap.get(key);

			boolean success = false;

			if (this.type.equals("Number")
					&& val >= Integer.parseInt(successResult)) {
				success = true;
			}

			if (type.equals("Percent")) {
				double percent = (double) val / this.allCount * 100;
				if (percent >= Integer.parseInt(successResult)) {
					success = true;
				}
			}

			if (success
					&& (this.monitorVal == null || (this.monitorVal != null && this.monitorVal
							.equals(key)))) {
				return key;
			}

		}

		return null;
	}

	public boolean queryIsFinished() {
		if (this.choiceMap.size() == this.allCount)
			return true;
		return false;
	}

	public int getAllCount() {
		return allCount;
	}

	public void setAllCount(int allCount) {
		this.allCount = allCount;
	}
}