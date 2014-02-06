package org.dayatang.springtest.application;

import org.dayatang.springtest.domain.MyEntity;

import java.util.List;

public interface MyApplication {

	public List<MyEntity> getMyEntityList();

	public List<MyEntity> getMyTrueEntityList();
}
