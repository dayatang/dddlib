package org.openkoala.koala.auth.impl.jdbc;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * PasswordEncoder测试
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Aug 20, 2013 8:58:07 AM
 */
public class PasswordEncoderTest {
	
	private PasswordEncoder encoder = new PasswordEncoder("admin", "MD5");

	@Test
	public void testEncode() {
		String encodePassword = encoder.encode("123456");
		assertTrue(!"".equals(encodePassword));
	}

	@Test
	public void testIsPasswordValid() {
		String encodePassword = encoder.encode("123456");
		assertTrue(!"".equals(encodePassword));
		assertTrue(encoder.isPasswordValid(encodePassword, "123456"));
	}

}
