package com.dayatang.springtest.test;

import org.springframework.beans.factory.InitializingBean;

//@Service("customBean")
public class CustomBean implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("aaaaaaaaaaa");
	}

	public void bb() {
		System.out.println("oooooooooooooo");
		
	}

}
