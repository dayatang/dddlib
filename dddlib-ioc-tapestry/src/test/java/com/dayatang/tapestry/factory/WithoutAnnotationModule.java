package com.dayatang.tapestry.factory;

import org.apache.tapestry5.ioc.ServiceBinder;

import com.dayatang.commons.ioc.MyService1;
import com.dayatang.commons.ioc.Service;

public class WithoutAnnotationModule {
	public static void bind(ServiceBinder binder) {
		binder.bind(Service.class, MyService1.class);
	}
}
