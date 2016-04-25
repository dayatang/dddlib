package org.dayatang.dsmonitor.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dayatang.domain.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "CommonsTestChild")
@Table(name = "CHILD")
public class Child extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3899776141651715689L;

	@Column(name = "NAME")
	private String name;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

    @Override
    public String[] businessKeys() {
        return new String[] {"name"};
    }

}
