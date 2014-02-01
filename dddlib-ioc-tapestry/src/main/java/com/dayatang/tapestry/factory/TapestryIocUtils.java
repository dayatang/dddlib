package com.dayatang.tapestry.factory;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.domain.InstanceProvider;
import com.dayatang.tapestry.factory.TapestryInstanceProvider;

public class TapestryIocUtils {

	private static final ThreadLocal<TapestryInstanceProvider> tapestryProviderHolder = new ThreadLocal<TapestryInstanceProvider>();

	private TapestryIocUtils() {
		super();
	}

	public static void initInstanceProvider(Class<?>... iocModules) {
		InstanceFactory.setInstanceProvider(getInstanceProvider(iocModules));
	}

	private static InstanceProvider getInstanceProvider(Class<?>... iocModules) {
		TapestryInstanceProvider result = tapestryProviderHolder.get();
		if (result == null) {
			result = new TapestryInstanceProvider(iocModules);
			tapestryProviderHolder.set(result);
		}
		return result;
	}
}
