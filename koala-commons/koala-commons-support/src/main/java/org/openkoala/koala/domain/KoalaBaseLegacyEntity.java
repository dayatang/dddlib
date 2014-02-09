package org.openkoala.koala.domain;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.dayatang.domain.Entity;

/**
 * 抽象实体类，可作为所有遗留系统领域实体的基类。
 * 
 */
@MappedSuperclass
public abstract class KoalaBaseLegacyEntity implements Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 871428741460277125L;

	/**
	 * 
	 * @return
	 */
	public boolean isNew() {
		return getId() == null;
	}

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object arg0);

	@Override
	public String toString() {
		// return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
		// .append(recordId).append(tariffNo).toString();
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE).toString();
	}
}

