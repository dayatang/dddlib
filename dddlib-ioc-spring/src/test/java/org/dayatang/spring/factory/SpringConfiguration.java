package org.dayatang.spring.factory;

import org.dayatang.commons.ioc.MyService1;
import org.dayatang.commons.ioc.MyService2;
import org.dayatang.commons.ioc.MyService3;
import org.dayatang.commons.ioc.Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {
	
	@Bean(name = "service1")
	public Service service1() {
		return new MyService1();
	}
	
	@Bean(name = "service2")
	public Service service2() {
		return new MyService2();
	}
	
	@Bean(name = "service3")
	public Service service3() {
		return new MyService3();
	}
}
