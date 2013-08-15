package com.dayatang.observer.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.dayatang.observer.Observer;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("2")
public class FatherObserver extends Observer<Baby> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6725506578728407946L;

	private Boolean startCar = false;

	public Boolean getStartCar() {
		return startCar;
	}

	public void setStartCar(Boolean startCar) {
		this.startCar = startCar;
	}

	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public void process(Baby baby) {
		System.out.println("宝宝哭了。。。启动汽车=true " + baby.getSubjectKey());
		setStartCar(true);
	}

}
