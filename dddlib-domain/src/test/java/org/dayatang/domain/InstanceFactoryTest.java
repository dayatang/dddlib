package org.dayatang.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InstanceFactoryTest {

	private InstanceProvider instanceProvider;
    private TheImpl1 impl = new TheImpl1();

	@Before
	public void setUp() throws Exception {
		instanceProvider = mock(InstanceProvider.class);
        when(instanceProvider.getInstance(TheInterface.class)).thenReturn(impl);
		InstanceFactory.setInstanceProvider(instanceProvider);
	}

	@After
	public void tearDown() throws Exception {
        InstanceFactory.setInstanceProvider(null);
	}

    private interface TheInterface {

    }

    private class TheImpl1 implements TheInterface {

    }

    private class TheImpl2 implements TheInterface {

    }

    /**
     * 设置了InstanceProvider，并且
     */
    @Test
    public void GetInstanceByInstanceFactory() {
        InstanceFactory.setInstanceProvider(instanceProvider);
        assertSame(impl, InstanceFactory.getInstance(TheInterface.class));
    }


}
