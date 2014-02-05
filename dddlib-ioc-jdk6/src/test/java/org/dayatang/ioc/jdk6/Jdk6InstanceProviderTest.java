package org.dayatang.ioc.jdk6;

import org.dayatang.test.ioc.AbstractInstanceProviderTest;
import org.dayatang.domain.InstanceProvider;

public class Jdk6InstanceProviderTest extends AbstractInstanceProviderTest {

	@Override
	protected InstanceProvider createInstanceProvider() {
		return new Jdk6InstanceProvider();
	}

}
