package com.dayatang.observer;

import java.util.List;

import javax.persistence.MappedSuperclass;

import com.dayatang.domain.AbstractEntity;

@SuppressWarnings("unchecked")
@MappedSuperclass
public abstract class AbstractSubjectEntity extends AbstractEntity implements
		Subject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8696877724719553088L;

    /**
     * 获得主题标识。默认返回主题的类名。
     * @return
     */
	public String getSubjectKey() {
        return getClass().getName();
    }

	@Override
	public List<Observer> getObservers() {
		// 每次查找最新的观察者，将来考虑加入缓存机制
		return Observer.findBySubject(this);

	}

	@Override
	public void notifyObservers() {
		List<Observer> observers = getObservers();
		for (Observer observer : observers) {
			observer.process(this);
		}
	}

}
