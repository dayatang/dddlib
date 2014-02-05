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
 * 一个基于Spring的集成测试基类。Spring的Bean装配文件放置在类路径下的spring子目录下。
 * 测试子类可以通过@Inject或@Autowire标注注入Bean实例。所有的测试方法都在一个数据库事
 * 务中执行。每个测试方法结束时，数据库将回滚，以保证各个测试方法不会因为共享数据库而相互
 * 影响。
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

    /**
     * 获取Spring应用上下文。
     * @return Spring应用上下文。
     */
    protected ApplicationContext getCtx() {
        return ctx;
    }

    /**
     * 根据Bean类型获取Spring中的Bean实例。 相比而言，通过@Inject注入Bean实例是更方便的方法。
     * @param beanClass Bean所属或继承的类
     * @param <T> Bean的类型
     * @return 类型为T的Bean实例
     */
    public <T> T getBean(Class<T> beanClass) {
        return ctx.getBean(beanClass);
    }

    /**
     * 根据Bean的ID或名称获取Spring中的Bean实例。 相比而言，通过@Inject和@Named注入Bean实例
     * 是更方便的方法。
     * @param beanName Bean的ID或名称
     * @param <T> Bean的类型
     * @return ID或名称为参数beanName的Bean实例
     */
    public <T> T getBean(String beanName) {
        return (T) ctx.getBean(beanName);
    }

}
