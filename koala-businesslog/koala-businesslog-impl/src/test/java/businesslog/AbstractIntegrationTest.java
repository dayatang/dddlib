package businesslog;

import com.dayatang.domain.InstanceFactory;

import com.dayatang.spring.factory.SpringInstanceProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:context.xml"})
public abstract class AbstractIntegrationTest {

	@Autowired
	private ApplicationContext applicationContext;



	@Before
	public void setUp() throws Exception {
		SpringInstanceProvider provider = new SpringInstanceProvider(applicationContext);
		InstanceFactory.setInstanceProvider(provider);
	}

	@After
	public void tearDown() throws Exception {
		InstanceFactory.setInstanceProvider(null);
	}

}
