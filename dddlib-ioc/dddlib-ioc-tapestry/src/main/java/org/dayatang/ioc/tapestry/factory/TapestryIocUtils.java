package org.dayatang.ioc.tapestry.factory;

import org.dayatang.domain.InstanceFactory;
import org.dayatang.domain.InstanceProvider;

public class TapestryIocUtils {

	private static final ThreadLocal<TapestryInstanceProvider> providerHolder = new ThreadLocal<TapestryInstanceProvider>();

	private TapestryIocUtils() {
		super();
	}

	public static void initInstanceProvider(Class<?>... iocModules) {
		InstanceFactory.setInstanceProvider(getInstanceProvider(iocModules));
	}

	private static InstanceProvider getInstanceProvider(Class<?>... iocModules) {
		TapestryInstanceProvider result = providerHolder.get();
		if (result != null) {
            return result;
        }
        synchronized (TapestryIocUtils.class) {
			result = new TapestryInstanceProvider(iocModules);
			providerHolder.set(result);
            return result;
		}
	}
}
