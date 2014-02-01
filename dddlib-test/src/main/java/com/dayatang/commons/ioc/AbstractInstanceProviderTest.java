package com.dayatang.commons.ioc;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.dayatang.domain.InstanceProvider;

/**
 * 公共的InstanceProvider测试
 * @author Administrator
 *
 */
public abstract class AbstractInstanceProviderTest {

	protected InstanceProvider provider;
	abstract protected InstanceProvider createInstanceProvider();
	
	@Before
	public void setUp() {
		provider = createInstanceProvider();
	}
	
	@Test
	public void testGetInstance() {
		Service service = provider.getInstance(Service.class);
		assertEquals("I am Service 1", service.sayHello());
	}

	@Test
	public void testGetInstanceWithName() {
		Service service = provider.getInstance(Service.class, "service2");
		assertEquals("I am Service 2", service.sayHello());
	}
}
