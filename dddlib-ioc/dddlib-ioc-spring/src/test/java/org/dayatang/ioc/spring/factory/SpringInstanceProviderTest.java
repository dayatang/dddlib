package org.dayatang.ioc.spring.factory;

import org.dayatang.domain.InstanceProvider;
import org.dayatang.ioc.test.AbstractInstanceProviderTest;

public class SpringInstanceProviderTest extends AbstractInstanceProviderTest {
	
	private SpringInstanceProvider instance;

	@Override
	protected InstanceProvider createInstanceProvider() {
		return new SpringInstanceProvider(SpringConfiguration.class);
	}

}

