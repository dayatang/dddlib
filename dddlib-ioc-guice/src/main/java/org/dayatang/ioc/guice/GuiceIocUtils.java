package org.dayatang.ioc.guice;

import com.google.inject.Module;
import org.dayatang.domain.InstanceFactory;

public class GuiceIocUtils {

	private GuiceIocUtils() {
	}

	public static void initInstanceProvider(Module... modules) {
		InstanceFactory.setInstanceProvider(new GuiceInstanceProvider(modules));
	}
}
