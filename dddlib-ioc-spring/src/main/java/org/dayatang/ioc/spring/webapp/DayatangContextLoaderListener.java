package org.dayatang.ioc.spring.webapp;

import javax.servlet.ServletContextEvent;

import org.dayatang.domain.InstanceFactory;
import org.dayatang.ioc.spring.factory.SpringInstanceProvider;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class DayatangContextLoaderListener extends ContextLoaderListener {

	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
		SpringInstanceProvider springProvider = new SpringInstanceProvider(applicationContext);
		InstanceFactory.setInstanceProvider(springProvider);
	}

}
