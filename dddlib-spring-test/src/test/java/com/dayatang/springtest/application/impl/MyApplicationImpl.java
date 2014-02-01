package com.dayatang.springtest.application.impl;

import java.util.List;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.springtest.application.MyApplication;
import com.dayatang.springtest.domain.MyEntity;

public class MyApplicationImpl implements MyApplication {

	private static QueryChannelService service;

	public static QueryChannelService getService() {
		if (service == null) {
			service = InstanceFactory.getInstance(QueryChannelService.class);
		}
		return service;
	}

	public static void setService(QueryChannelService service) {
		MyApplicationImpl.service = service;
	}

	@Override
	public List<MyEntity> getMyEntityList() {
		List<MyEntity> lists = getService().queryResult("from MyEntity",
				new Object[] {});

		for (MyEntity entity : lists) {
			entity.setName("aaaaaaaaaa");
		}

		return lists;
	}
	
	@Override
	public List<MyEntity> getMyTrueEntityList() {
		List<MyEntity> lists = getService().queryResult("from MyEntity",
				new Object[] {});

		return lists;
	}
}
