package org.dayatang.ioc.spring.factory;

import org.dayatang.ioc.spring.beans.ServiceFactory;
import org.dayatang.ioc.test.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {
	
	@Bean
	public Service1 service1() {
        try {
            return new ServiceFactory().getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}

	@Bean(name = "service21")
	public Service2 service21() {
		return new MyService21();
	}
	
	@Bean(name = "service22")
	public Service2 service22() {
		return new MyService22();
	}

	@Bean
	public Service2 service23() {
		return new MyService23();
	}

	@Bean
	public Service3 service3() {
		return new MyService3();
	}
}
