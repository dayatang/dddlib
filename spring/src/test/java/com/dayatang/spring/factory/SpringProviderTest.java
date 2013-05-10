package com.dayatang.spring.factory;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dayatang.commons.ioc.AbstractInstanceProviderTest;
import com.dayatang.commons.ioc.MyService1;
import com.dayatang.commons.ioc.Service;
import com.dayatang.configuration.Configuration;
import com.dayatang.domain.InstanceFactory;
import com.dayatang.domain.InstanceProvider;

@SuppressWarnings("deprecation")
public class SpringProviderTest extends AbstractInstanceProviderTest {
	
	private SpringProvider instance;

	@Override
	protected InstanceProvider createInstanceProvider() {
		return new SpringProvider(new String[] {"classpath:applicationContext-multi-impl.xml"});
	}
	
	@Test
	public void testConstructorFromXmlPath() {
		instance = new SpringProvider(new String[] {"classpath:applicationContext-single-impl.xml"});
		InstanceFactory.setInstanceProvider(instance);
		Service service = instance.getInstance(Service.class);
		assertTrue(MyService1.class.isAssignableFrom(service.getClass()));
		
		Configuration configuration = instance.getInstance(Configuration.class);
		System.out.println(configuration.getString("log4j.rootLogger"));		
	}
	
	@Test
	public void testConstructorFromApplicationContext() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {"classpath:applicationContext-single-impl.xml"});
		instance = new SpringProvider(applicationContext);
		InstanceFactory.setInstanceProvider(instance);
		Service service = instance.getInstance(Service.class);
		assertTrue(MyService1.class.isAssignableFrom(service.getClass()));
	}
	
	@Test
	public void testConstructorFromConfigurationFiles() {
		instance = new SpringProvider(SpringConfiguration.class);
		InstanceFactory.setInstanceProvider(instance);
		Service service = instance.getInstance(Service.class, "service1");
		assertTrue(MyService1.class.isAssignableFrom(service.getClass()));
	}

}

