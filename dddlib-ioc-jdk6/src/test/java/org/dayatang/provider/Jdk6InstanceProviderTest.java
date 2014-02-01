package org.dayatang.provider;

import org.dayatang.commons.ioc.AbstractInstanceProviderTest;
import org.dayatang.domain.InstanceProvider;
import org.dayatang.provider.Jdk6InstanceProvider;

public class Jdk6InstanceProviderTest extends AbstractInstanceProviderTest {

	@Override
	protected InstanceProvider createInstanceProvider() {
		return new Jdk6InstanceProvider();
	}

}
