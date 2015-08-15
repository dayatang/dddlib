package org.dayatang.security.domain;

import org.dayatang.springtest.AbstractSpringIntegrationTest;
import org.junit.BeforeClass;

/**
 * Created by yyang on 15/8/16.
 */
public class SecurityIntegrationTest extends AbstractSpringIntegrationTest {

    @BeforeClass
    public static void beforeAllTest() {
        SecuritySeedDataHelper helper = new SecuritySeedDataHelper();
        helper.createGroup("grand")
                .createGroup("parent");

    }

}
