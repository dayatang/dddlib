package org.dayatang.ioc.guice;

import com.google.inject.Module;
import org.dayatang.domain.InstanceFactory;

public class GuiceIocUtils {

    private static final ThreadLocal<GuiceInstanceProvider> providerHolder = new ThreadLocal<GuiceInstanceProvider>();

	private GuiceIocUtils() {
	}

	public static void initInstanceProvider(Module... modules) {
		InstanceFactory.setInstanceProvider(getInstanceProvider(modules));
	}

    private static GuiceInstanceProvider getInstanceProvider(Module[] modules) {
        GuiceInstanceProvider result = providerHolder.get();
        if (result != null) {
            return result;
        }
        synchronized (GuiceIocUtils.class) {
            result = new GuiceInstanceProvider(modules);
            providerHolder.set(result);
            return result;
        }
    }
}
