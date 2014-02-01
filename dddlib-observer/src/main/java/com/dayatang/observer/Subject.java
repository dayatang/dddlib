package com.dayatang.observer;

import java.io.Serializable;
import java.util.List;

public interface Subject extends Serializable {

	public String getSubjectKey();

	@SuppressWarnings("rawtypes")
	public List<Observer> getObservers();

	public void notifyObservers();

}
