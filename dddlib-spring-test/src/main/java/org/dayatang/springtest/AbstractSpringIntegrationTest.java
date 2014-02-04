package org.dayatang.springtest;

import org.dayatang.domain.InstanceFactory;
import org.dayatang.spring.factory.SpringInstanceProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by yyang on 14-2-2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/*.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class AbstractSpringIntegrationTest {

    @Inject
    private ApplicationContext ctx;

    @Before
    public void beforeTest() {
        InstanceFactory.setInstanceProvider(new SpringInstanceProvider(ctx));
    }

    @After
    public void afterTest() {
        InstanceFactory.setInstanceProvider(null);
    }

    protected ApplicationContext getCtx() {
        return ctx;
    }


    public <T> T getBean(Class<T> beanClass) {
        return ctx.getBean(beanClass);
    }

    public <T> T getBean(String beanName) {
        return (T) ctx.getBean(beanName);
    }

}
