package org.dayatang.ioc.tapestry.factory;

import org.apache.tapestry5.ioc.ServiceBinder;
import org.dayatang.test.ioc.*;

public class TapestryModule {

	public static void bind(ServiceBinder binder) {
		binder.bind(Service.class, MyService1.class).withId("service1");
		binder.bind(Service.class, MyService2.class).withId("service2");
		binder.bind(Service.class, MyService3.class).withId("service3").withMarker(TheAnnotation.class);
        binder.bind(Service2.class, MyService21.class).withId("service21");
	}

}
