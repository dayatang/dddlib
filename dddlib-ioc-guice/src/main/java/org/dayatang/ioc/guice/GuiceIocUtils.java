package org.dayatang.ioc.guice;

import org.dayatang.domain.InstanceFactory;

import com.google.inject.Module;

public class GuiceIocUtils {

	private GuiceIocUtils() {
	}

	public static void initInstanceProvider(Module... modules) {
		InstanceFactory.setInstanceProvider(new GuiceInstanceProvider(modules));
	}
}
