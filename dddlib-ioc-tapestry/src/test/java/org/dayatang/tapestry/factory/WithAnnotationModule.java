package org.dayatang.tapestry.factory;

import org.apache.tapestry5.ioc.ServiceBinder;
import org.dayatang.commons.ioc.MyService1;
import org.dayatang.commons.ioc.MyService2;
import org.dayatang.commons.ioc.MyService3;
import org.dayatang.commons.ioc.Service;

public class WithAnnotationModule {
	public static void bind(ServiceBinder binder) {
		binder.bind(Service.class, MyService1.class).withId("service1");
		binder.bind(Service.class, MyService2.class).withId("service2");
		binder.bind(Service.class, MyService3.class).withId("service3");
	}

}
