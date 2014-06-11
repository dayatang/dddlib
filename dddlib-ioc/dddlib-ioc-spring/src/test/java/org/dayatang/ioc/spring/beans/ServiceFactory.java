package org.dayatang.ioc.spring.beans;


import org.dayatang.ioc.test.MyService1;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by yyang on 14-6-11.
 */
public class ServiceFactory implements FactoryBean<MyService1> {

    @Override
    public MyService1 getObject() throws Exception {
        return new MyService1();
    }

    @Override
    public Class<?> getObjectType() {
        return MyService1.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
