package org.dayatang.ioc.tapestry.factory;

import org.apache.tapestry5.ioc.ServiceBinder;
import org.dayatang.ioc.test.*;

public class TapestryModule {

	public static void bind(ServiceBinder binder) {
		binder.bind(Service1.class, MyService1.class).withId("service1");
		binder.bind(Service2.class, MyService21.class).withId("service21");
		binder.bind(Service2.class, MyService22.class).withId("service22");
		binder.bind(Service2.class, MyService23.class).withMarker(TheAnnotation.class);
        binder.bind(Service3.class, MyService3.class);
	}

}
