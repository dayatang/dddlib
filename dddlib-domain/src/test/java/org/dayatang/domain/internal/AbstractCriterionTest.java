package org.dayatang.domain.internal;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AbstractCriterionTest {
	
	private AbstractCriterion criterion1 = new AbstractCriterion() {

		@Override
		public String toQueryString() {
			return "";
		}};
	
	private AbstractCriterion criterion2 = new AbstractCriterion() {

		@Override
		public String toQueryString() {
			return "";
		}};

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAnd() {
		assertEquals(new AndCriterion(criterion1, criterion2), criterion1.and(criterion2));
	}

	@Test
	public void testOr() {
		assertEquals(new OrCriterion(criterion1, criterion2), criterion1.or(criterion2));
	}

	@Test
	public void testNot() {
		assertEquals(new NotCriterion(criterion1), criterion1.not());
	}
	
	

}
