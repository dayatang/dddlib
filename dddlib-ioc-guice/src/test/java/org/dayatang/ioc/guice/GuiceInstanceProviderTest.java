package org.dayatang.ioc.guice;

import com.google.inject.*;
import com.google.inject.name.Names;
import org.dayatang.domain.InstanceProvider;
import org.dayatang.test.ioc.AbstractInstanceProviderTest;
import org.dayatang.test.ioc.MyService1;
import org.dayatang.test.ioc.MyService2;
import org.dayatang.test.ioc.Service;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GuiceInstanceProviderTest extends AbstractInstanceProviderTest {

	@Test
	public void testConstructorFromModule() {
		assertEquals("I am Service 1", provider.getInstance(Service.class).sayHello());
	}

	@Test
	public void testConstructorFromInjector() {
		provider = new GuiceInstanceProvider(createInjector());
		assertEquals("I am Service 1", provider.getInstance(Service.class).sayHello());
	}

	private Injector createInjector() {
		return Guice.createInjector(createModule());
	}
	

	private Module createModule() {
		return new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bind(Service.class).to(MyService1.class).in(Scopes.SINGLETON);
				binder.bind(Service.class).annotatedWith(Names.named("service2")).to(MyService2.class).in(Scopes.SINGLETON);
				binder.bind(Service.class).annotatedWith(Names.named("service1")).to(MyService1.class).in(Scopes.SINGLETON);
				binder.bind(Service.class).annotatedWith(Names.named("service3")).to(MyService2.class).in(Scopes.SINGLETON);
			}
		};
	}

	@Override
	protected InstanceProvider createInstanceProvider() {
		return new GuiceInstanceProvider(createModule());
	}
}
