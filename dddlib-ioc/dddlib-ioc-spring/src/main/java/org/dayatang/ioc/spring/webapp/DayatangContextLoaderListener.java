package org.dayatang.ioc.spring.webapp;

import org.dayatang.domain.InstanceFactory;
import org.dayatang.ioc.spring.factory.SpringInstanceProvider;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;

/**
 * 一个Web监听器，扩展并取代Spring的ContextLoaderListener，将SpringIoC整合到InstanceFactory中。
 * @author yyang
 */
public class DayatangContextLoaderListener extends ContextLoaderListener {

        @Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
		SpringInstanceProvider springProvider = new SpringInstanceProvider(applicationContext);
		InstanceFactory.setInstanceProvider(springProvider);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		InstanceFactory.setInstanceProvider(null);
		super.contextDestroyed(event);
	}
}
