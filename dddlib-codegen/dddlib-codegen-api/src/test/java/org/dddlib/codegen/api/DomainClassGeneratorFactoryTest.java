package org.dddlib.codegen.api;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by yyang on 2016/12/21.
 */
public class DomainClassGeneratorFactoryTest {

    @Test
    public void getInstance() throws Exception {
        DomainClassGenerator generator = DomainClassGeneratorFactory.getInstance();
        assertThat(generator).isInstanceOf(MockDomainClassGenerator.class);
    }

}