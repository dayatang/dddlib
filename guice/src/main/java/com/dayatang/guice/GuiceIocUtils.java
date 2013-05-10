package com.dayatang.guice;

import com.dayatang.domain.InstanceFactory;
import com.google.inject.Module;

public class GuiceIocUtils {

	private GuiceIocUtils() {
	}

	public static void initInstanceProvider(Module... modules) {
		InstanceFactory.setInstanceProvider(new GuiceInstanceProvider(modules));
	}
}
