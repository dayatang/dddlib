package org.dayatang.ioc.jdk6;

import org.dayatang.commons.ioc.AbstractInstanceProviderTest;
import org.dayatang.domain.InstanceProvider;
import org.dayatang.ioc.jdk6.Jdk6InstanceProvider;

public class Jdk6InstanceProviderTest extends AbstractInstanceProviderTest {

	@Override
	protected InstanceProvider createInstanceProvider() {
		return new Jdk6InstanceProvider();
	}

}
