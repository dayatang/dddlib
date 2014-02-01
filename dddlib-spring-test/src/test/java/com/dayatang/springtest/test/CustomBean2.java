package com.dayatang.springtest.test;

import org.springframework.beans.factory.InitializingBean;

import com.dayatang.domain.InstanceFactory;

//@Service("customeBean2")
public class CustomBean2 implements InitializingBean {

	// @Autowired
	private static CustomBean customBean;

	public static CustomBean getCustomBean() {
		if (customBean == null) {
			return InstanceFactory.getInstance(CustomBean.class);
		}
		return customBean;
	}

	public static void setCustomBean(CustomBean customBean) {
		CustomBean2.customBean = customBean;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("bbbbbbbbbbb " + customBean);
	}

	public void aa() {
		getCustomBean().bb();
	}

}
