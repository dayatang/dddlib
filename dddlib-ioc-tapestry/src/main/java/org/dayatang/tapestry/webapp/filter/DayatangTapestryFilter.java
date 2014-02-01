package org.dayatang.tapestry.webapp.filter;

import javax.servlet.ServletException;

import org.apache.tapestry5.TapestryFilter;
import org.apache.tapestry5.ioc.Registry;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.tapestry.factory.TapestryInstanceProvider;

public class DayatangTapestryFilter extends TapestryFilter {

	public void init(Registry registry) throws ServletException {
		super.init(registry);
		InstanceFactory.setInstanceProvider(new TapestryInstanceProvider(registry));
	}

}
