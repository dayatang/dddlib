package org.dayatang.security.domain;

import org.dayatang.domain.EntityRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by yyang on 15/8/14.
 */
public class UserTest {

    private User user;

    @Mock
    private EntityRepository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        user = new MyUser("zhang", "123");
    }

    @Test
    public void constructor() {
        user = new MyUser("zhang", "123");
        assertThat(user.getName(), is("zhang"));
        assertThat(user.getPassword(), is("123"));
    }


}
