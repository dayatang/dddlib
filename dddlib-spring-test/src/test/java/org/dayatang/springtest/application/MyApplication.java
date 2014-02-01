package org.dayatang.springtest.application;

import java.util.List;

import org.dayatang.springtest.domain.MyEntity;

public interface MyApplication {

	public List<MyEntity> getMyEntityList();

	public List<MyEntity> getMyTrueEntityList();
}
