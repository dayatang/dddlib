package org.openkoala.koala.auth.ss3adapter.password;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.openkoala.koala.auth.ss3adapter.password.SaltPasswordEncoder;

/**
 * PasswordEncoder测试
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Aug 20, 2013 8:58:07 AM
 */
public class PasswordEncoderTest {
	
	private SaltPasswordEncoder encoder;
	
	@Before
	public void setUp() {
		encoder  = new SaltPasswordEncoder();
		encoder.setAlgorithm("MD5");
	}

	@Test
	public void testEncode() {
		String encodePassword = encoder.encodePassword("123456", "");
		assertTrue(!"".equals(encodePassword));
	}

	@Test
	public void testIsPasswordValid() {
		String encodePassword = encoder.encodePassword("123456", "");
		assertTrue(!"".equals(encodePassword));
		assertTrue(encoder.isPasswordValid(encodePassword, "123456", ""));
	}

}
