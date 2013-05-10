package com.dayatang.observer.domain;

import com.dayatang.observer.AbstractSubjectEntity;


public class Baby extends AbstractSubjectEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7609647418687945848L;

	@Override
	public String getSubjectKey() {
		return "BABY-SUBJECT";
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
		// TODO Auto-generated method stub
		return null;
	}

	public void cry() {
		notifyObservers();
	}

}
