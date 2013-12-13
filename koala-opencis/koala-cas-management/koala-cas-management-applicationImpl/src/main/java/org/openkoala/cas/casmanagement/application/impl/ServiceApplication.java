package org.openkoala.cas.casmanagement.application.impl;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Application;
import org.openkoala.cas.casmanagement.application.CasUserApplication;
import com.dayatang.domain.InstanceFactory;

/**
 * 从Spring容器中获取bean
 * @author zhuyuanbiao
 * @date 2013年12月9日 下午6:04:55
 *
 */
public class ServiceApplication extends Application {


	@Override
	public Set<Object> getSingletons() {
		CasUserApplication userApplication = InstanceFactory.getInstance(CasUserApplication.class);
		HashSet<Object> results = new HashSet<Object>();
		results.add(userApplication);
		return results;
	}

}
