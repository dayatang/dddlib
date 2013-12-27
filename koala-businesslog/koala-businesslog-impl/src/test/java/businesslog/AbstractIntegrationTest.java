package businesslog;

import com.dayatang.domain.InstanceFactory;

import com.dayatang.spring.factory.SpringInstanceProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.logicalcobwebs.proxool.ProxoolFacade;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:context.xml"})
@TransactionConfiguration(transactionManager = "transactionManager_businessLog",defaultRollback = false)
public abstract class AbstractIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Before
	public void setUp() throws Exception {
        SpringInstanceProvider provider = new SpringInstanceProvider(applicationContext);
		InstanceFactory.setInstanceProvider(provider);
	}

	@After
	public void tearDown() throws Exception {
		InstanceFactory.setInstanceProvider(null);
        //ProxoolFacade.shutdown(0);
	}

}
