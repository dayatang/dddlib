package org.dayatang.ioc.spring.factory;

import org.dayatang.configuration.Configuration;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.domain.InstanceProvider;
import org.dayatang.test.ioc.AbstractInstanceProviderTest;
import org.dayatang.test.ioc.MyService1;
import org.dayatang.test.ioc.Service;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertTrue;

public class SpringInstanceProviderTest extends AbstractInstanceProviderTest {
	
	private SpringInstanceProvider instance;

	@Override
	protected InstanceProvider createInstanceProvider() {
		return new SpringInstanceProvider(SpringConfiguration.class);
	}

}

