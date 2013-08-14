package com.dayatang.springtest;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.spring.factory.SpringInstanceProvider;

public abstract class PureSpringTestCase {

	private static final Logger logger = LoggerFactory.getLogger(PureSpringTestCase.class);

	private SpringInstanceProvider provider;

	protected String[] springXmlPath() {
		return new String[] { "classpath:spring/*.xml" };
	}

	@Before
	public void setup() {
		if (!(InstanceFactory.isInitialized())) {
			initIoc();
		}
	}

	private void initIoc() {
		if (logger.isDebugEnabled()) {
			logger.debug("初始化Spring上下文。");
		}
		provider = new SpringInstanceProvider(springXmlPath());
		InstanceFactory.setInstanceProvider(provider);
	}

	@After
	public void teardown() {
	}

	public <T> T getBean(Class<T> beanClass) {
		return InstanceFactory.getInstance(beanClass);
	}

	public <T> T getBean(String beanName) {
		return provider.getByBeanName(beanName);
	}
}
