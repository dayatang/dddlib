package org.openkoala.bpm.KoalaBPM.web.context;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.openkoala.bpm.application.BusinessSupportApplication;
import org.openkoala.bpm.processdyna.core.DynaProcessTemplate;
import org.openkoala.organisation.application.OrganizationApplication;
import org.openkoala.organisation.domain.Company;
import org.openkoala.organisation.domain.Organization;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ProcessDynaListener implements ServletContextListener {
	
	private final String twoColumnsTemplate = "<#if params??><#assign i=0><#list params?keys as idKey><#if (i+1)%2==1><div class=\"form-group row\"></#if><div class=\"col-lg-6 form-group\"><label class=\"col-lg-4 control-label\">${params[idKey].keyName}:</label><div class=\"col-lg-8\">${params[idKey].widget}</div></div><#if (i+1)%2==0></div></#if><#if (i+1)==size && (i+1)%2==1></div></#if><#assign i=i+1></#list></#if>";
	private final String singleColumnTemplate = "";

	public void contextInitialized(ServletContextEvent event) {
		initDynaProcessTemplateDatas(event);
		initTopOrganizationIfNecessary(event);
	}
	
	private void initDynaProcessTemplateDatas(ServletContextEvent event) {
		System.out.println("--------------------------------");
		BusinessSupportApplication businessSupportApplication = (BusinessSupportApplication) WebApplicationContextUtils
				.getWebApplicationContext(event.getServletContext()).getBean(
						"businessSupportApplication");
		
		List<DynaProcessTemplate> dynaProcessTemplates = this.getInitDynaProcessTemplateDatas();
		businessSupportApplication.initDynaProcessTemplateDatas(dynaProcessTemplates);
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

	private List<DynaProcessTemplate> getInitDynaProcessTemplateDatas(){
		List<DynaProcessTemplate> dynaProcessTemplates = new ArrayList<DynaProcessTemplate>();
		DynaProcessTemplate twoColumnsTemplateBean = this.getTwoColumnsTemplate();
		dynaProcessTemplates.add(twoColumnsTemplateBean);
		return dynaProcessTemplates;
	}
	
	private DynaProcessTemplate getSingleColumnTemplate() {
		DynaProcessTemplate singleColumnTemplateBean = new DynaProcessTemplate();
		singleColumnTemplateBean.setTemplateName("一列模板");
		singleColumnTemplateBean.setTemplateData(singleColumnTemplate);
		singleColumnTemplateBean.setTemplateDescription("一列模板");
		return singleColumnTemplateBean;
	}

	private DynaProcessTemplate getTwoColumnsTemplate() {
		DynaProcessTemplate twoColumnsTemplateBean = new DynaProcessTemplate();
		twoColumnsTemplateBean.setTemplateName("两列模板");
		twoColumnsTemplateBean.setTemplateData(twoColumnsTemplate);
		twoColumnsTemplateBean.setTemplateDescription("两列模板");
		return twoColumnsTemplateBean;
	}
	
	public void contextDestroyed(ServletContextEvent sce) {

	}

}
