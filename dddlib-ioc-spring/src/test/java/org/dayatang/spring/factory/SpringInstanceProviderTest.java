package org.dayatang.spring.factory;

import static org.junit.Assert.assertTrue;

import org.dayatang.commons.ioc.AbstractInstanceProviderTest;
import org.dayatang.commons.ioc.MyService1;
import org.dayatang.commons.ioc.Service;
import org.dayatang.configuration.Configuration;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.domain.InstanceProvider;
import org.dayatang.spring.factory.SpringInstanceProvider;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringInstanceProviderTest extends AbstractInstanceProviderTest {
	
	private SpringInstanceProvider instance;

	@Override
	protected InstanceProvider createInstanceProvider() {
		return new SpringInstanceProvider(new String[] {"classpath:applicationContext-multi-impl.xml"});
	}
	
	@Test
	public void testConstructorFromXmlPath() {
		instance = new SpringInstanceProvider(new String[] {"classpath:applicationContext-single-impl.xml"});
		InstanceFactory.setInstanceProvider(instance);
		Service service = instance.getInstance(Service.class);
		assertTrue(MyService1.class.isAssignableFrom(service.getClass()));
		
		Configuration configuration = instance.getInstance(Configuration.class);
		System.out.println(configuration.getString("log4j.rootLogger"));		
	}
	
	@Test
	public void testConstructorFromApplicationContext() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {"classpath:applicationContext-single-impl.xml"});
		instance = new SpringInstanceProvider(applicationContext);
		InstanceFactory.setInstanceProvider(instance);
		Service service = instance.getInstance(Service.class);
		assertTrue(MyService1.class.isAssignableFrom(service.getClass()));
	}
	
	@Test
	public void testConstructorFromConfigurationFiles() {
		instance = new SpringInstanceProvider(SpringConfiguration.class);
		InstanceFactory.setInstanceProvider(instance);
		Service service = instance.getInstance(Service.class, "service1");
		assertTrue(MyService1.class.isAssignableFrom(service.getClass()));
	}

}

