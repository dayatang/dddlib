package org.openkoala.koala.auth.impl.jdbc;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PasswordEncoderTest {
	
	private PasswordEncoder encoder;
	
	@Before
	public void setUp() {
		encoder = new PasswordEncoder("", "MD5");
	}

	@Test
	public void testIsPasswordValid() {
		String encodedPassword = encoder.encode("123456");
		assertNotNull(encodedPassword);
		assertTrue(encoder.isPasswordValid(encodedPassword, "123456"));
		System.out.println(encodedPassword);
	}

}
