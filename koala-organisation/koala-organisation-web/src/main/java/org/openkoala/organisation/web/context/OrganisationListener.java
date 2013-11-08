package org.openkoala.organisation.web.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.openkoala.organisation.application.OrganizationApplication;
import org.openkoala.organisation.domain.Company;
import org.openkoala.organisation.domain.Organization;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class OrganisationListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent event) {
		initTopOrganizationIfNecessary(event);
	}
	
	private void initTopOrganizationIfNecessary(ServletContextEvent event) {
		if (Organization.getTopOrganization() != null) {
			return;
		}
		
		System.out.println("--------------------------------");
		OrganizationApplication organizationApplication = (OrganizationApplication) WebApplicationContextUtils
				.getWebApplicationContext(event.getServletContext()).getBean(
						"organizationApplication");
		
		organizationApplication.createAsTopOrganization(getTopOrganization());
	}
	
	private Company getTopOrganization() {
		Company result = new Company("总公司", "COM-001");
		result.setDescription("总公司：所有机构的根");
		return result;
	}

	public void contextDestroyed(ServletContextEvent sce) {

	}

}
