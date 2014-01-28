package org.openkoala.bpm.processdyna.core;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dayatang.domain.AbstractEntity;

/**
 * 流程中表单自定义的 VALUE 值
 * 
 * @author lingen
 *
 * 
 */
@Entity
@Table(name = "DYNA_PROCESS_HISTORY_VALUE")
public class DynaProcessHistoryValue extends AbstractEntity {
	
	

	private static final long serialVersionUID = 5053473713859436953L;
	

	@Column(name = "PROCESS_INSTANCE_ID")
	private long processInstanceId;
	

	@Column(name = "KEY_VALUE")
	private String keyValue;
	

	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH }, optional = true)
	@JoinColumn(name = "KEY_ID")
	private DynaProcessKey dynaProcessKey;

	public long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public DynaProcessKey getDynaProcessKey() {
		return dynaProcessKey;
	}

	public void setDynaProcessKey(DynaProcessKey dynaProcessKey) {
		this.dynaProcessKey = dynaProcessKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dynaProcessKey == null) ? 0 : dynaProcessKey.hashCode());
		result = prime * result
				+ ((keyValue == null) ? 0 : keyValue.hashCode());
		result = prime * result
				+ (int) (processInstanceId ^ (processInstanceId >>> 32));
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
		DynaProcessHistoryValue other = (DynaProcessHistoryValue) obj;
		if (dynaProcessKey == null) {
			if (other.dynaProcessKey != null)
				return false;
		} else if (!dynaProcessKey.equals(other.dynaProcessKey))
			return false;
		if (keyValue == null) {
			if (other.keyValue != null)
				return false;
		} else if (!keyValue.equals(other.keyValue))
			return false;
		if (processInstanceId != other.processInstanceId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DynaProcessValue [processInstanceId=" + processInstanceId
				+ ", keyValue=" + keyValue + ", dynaProcessKey="
				+ dynaProcessKey + "]";
	}

	
	/* =================领域行为=================== */
	public static void copyDynaProcessValue(DynaProcessValue value) {
		DynaProcessHistoryValue dynaProcessHistoryValue = new DynaProcessHistoryValue();
		dynaProcessHistoryValue.dynaProcessKey = value.getDynaProcessKey();
		dynaProcessHistoryValue.keyValue = value.getKeyValue();
		dynaProcessHistoryValue.processInstanceId = value.getProcessInstanceId();
		dynaProcessHistoryValue.processInstanceId = value
				.getProcessInstanceId();
		dynaProcessHistoryValue.save();
		value.remove();
	}
	
	
	

	public static List<DynaProcessHistoryValue> querykeyValuesByProcessInstanceId(
			long processInstanceId) {
		List<DynaProcessHistoryValue> results = getRepository()
				.findByNamedQuery(
						"queryDynaProcessHistoryValuesByProcessInstanceId",
						new Object[] { processInstanceId },
						DynaProcessHistoryValue.class);
		return results;
	}

}
