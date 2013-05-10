package com.dayatang.springtest.application;

import java.util.List;

import com.dayatang.springtest.domain.MyEntity;

public interface MyApplication {

	public List<MyEntity> getMyEntityList();

	public List<MyEntity> getMyTrueEntityList();
}
