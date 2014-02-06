package org.dayatang.ioc.tapestry.factory;

import org.dayatang.domain.InstanceFactory;
import org.dayatang.domain.InstanceProvider;

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
