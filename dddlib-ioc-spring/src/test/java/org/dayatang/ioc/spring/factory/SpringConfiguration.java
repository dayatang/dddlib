package org.dayatang.ioc.spring.factory;

import org.dayatang.test.ioc.MyService1;
import org.dayatang.test.ioc.MyService2;
import org.dayatang.test.ioc.MyService3;
import org.dayatang.test.ioc.Service;
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
