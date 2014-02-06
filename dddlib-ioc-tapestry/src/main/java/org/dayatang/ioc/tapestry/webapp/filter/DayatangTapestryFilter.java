package org.dayatang.ioc.tapestry.webapp.filter;

import org.apache.tapestry5.TapestryFilter;
import org.apache.tapestry5.ioc.Registry;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.ioc.tapestry.factory.TapestryInstanceProvider;

import javax.servlet.ServletException;

public class DayatangTapestryFilter extends TapestryFilter {

	public void init(Registry registry) throws ServletException {
		super.init(registry);
		InstanceFactory.setInstanceProvider(new TapestryInstanceProvider(registry));
	}

}
