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
		String encodedPassword = encoder.encode("ff");
		assertNotNull(encodedPassword);
		assertTrue(encoder.isPasswordValid(encodedPassword, "ff"));
		System.out.println(encodedPassword);
	}

}
