package com.dayatang.guice;

import static org.junit.Assert.*;

import org.junit.Test;

import com.dayatang.commons.ioc.AbstractInstanceProviderTest;
import com.dayatang.commons.ioc.MyService1;
import com.dayatang.commons.ioc.MyService2;
import com.dayatang.commons.ioc.Service;
import com.dayatang.domain.InstanceProvider;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.google.inject.name.Names;

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
