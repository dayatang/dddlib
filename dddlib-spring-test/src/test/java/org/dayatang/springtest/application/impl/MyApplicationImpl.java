package org.dayatang.springtest.application.impl;

import org.dayatang.springtest.application.MyApplication;
import org.dayatang.springtest.domain.MyEntity;

import java.util.ArrayList;
import java.util.List;

public class MyApplicationImpl implements MyApplication {

	@Override
	public List<MyEntity> getMyEntityList() {
        List<MyEntity> results = new ArrayList<MyEntity>();
        for (int i = 1; i < 5; i++) {

            MyEntity entity = new MyEntity();

            entity.setName("aaaaaaaaaa");
            //entity.setName("entity" + i);
            entity.setVersion(1);

            results.add(entity);
        }

		return results;
	}
	
	@Override
	public List<MyEntity> getMyTrueEntityList() {
        List<MyEntity> results = new ArrayList<MyEntity>();
        for (int i = 1; i < 5; i++) {

            MyEntity entity = new MyEntity();

            entity.setName("entity" + i);
            entity.setVersion(1);

            results.add(entity);
        }

        return results;
	}
}
