package org.openkoala.bpm.processdyna.core;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.dayatang.domain.AbstractEntity;

/**
 * 流程中表单自定义的 VALUE 值
 * @author lingen
 *
 */
@Entity
@Table(name = "DYNA_PROCESS_VALUE")
public class DynaProcessValue extends AbstractEntity {
	
	
	private static final long serialVersionUID = 5053473713859436953L;
	
	@Column(name="PROCESS_INSTANCE_ID")
	private long processInstanceId;
	
	@Column(name="KEY_VALUE")
	private String keyValue;
	
	@ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH }, optional = true,fetch = FetchType.EAGER)  
	@JoinColumn(name="KEY_ID")
	private DynaProcessKey dynaProcessKey;
	
	

	public DynaProcessValue() {
		super();
	}

	public DynaProcessValue(long processInstanceId,
			DynaProcessKey dynaProcessKey, String keyValue) {
		super();
		this.processInstanceId = processInstanceId;
		this.dynaProcessKey = dynaProcessKey;
		this.keyValue = keyValue;
	}


	@Override
	public int hashCode() {
        return new HashCodeBuilder(17, 31).append(processInstanceId).append(dynaProcessKey).append(keyValue).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof DynaProcessValue)) {
            return false;
        }
        DynaProcessValue that = (DynaProcessValue) other;
        return new EqualsBuilder().append(this.processInstanceId, that.processInstanceId)
                .append(this.dynaProcessKey, that.dynaProcessKey)
                .append(this.keyValue, that.keyValue)
                .isEquals();
	}

	@Override
	public String toString() {
		return "DynaProcessValue [processInstanceId=" + processInstanceId
				+ ", keyValue=" + keyValue + ", dynaProcessKey="
				+ dynaProcessKey + "]";
	}
	
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
	
	/*=================领域行为===================*/

	/**
	 * 根据流程实例查询出其对应的值
	 * @param processInstanceId
	 * @return
	 */
	public static List<DynaProcessValue> queryDynaProcessValueByProcessInstanceId(long processInstanceId){
		List<DynaProcessValue> results = DynaProcessValue.getRepository().findByNamedQuery("queryDynaProcessValueByProcessInstanceId", new Object[]{processInstanceId}, DynaProcessValue.class);
        return results;
	}
	
	/**
	 * 流程完结时，将其移动到历史记录中去
	 * @param processInstanceId
	 */
	public static void transferToHistory(long processInstanceId){
		List<DynaProcessValue> results = DynaProcessValue.getRepository().findByNamedQuery("queryDynaProcessValueByProcessInstanceId", new Object[]{processInstanceId}, DynaProcessValue.class);
		for(DynaProcessValue value:results){
			DynaProcessHistoryValue.copyDynaProcessValue(value);
		}
	}
}
