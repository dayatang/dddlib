package com.dayatang.provider;

import com.dayatang.commons.ioc.AbstractInstanceProviderTest;
import com.dayatang.domain.InstanceProvider;

public class Jdk6InstanceProviderTest extends AbstractInstanceProviderTest {

	@Override
	protected InstanceProvider createInstanceProvider() {
		return new Jdk6InstanceProvider();
	}

}
