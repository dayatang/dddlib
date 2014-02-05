package org.dayatang.persistence.hibernate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.transaction.UserTransaction;

import org.dayatang.commons.domain.Dictionary;
import org.dayatang.commons.domain.DictionaryCategory;
import org.dayatang.domain.AbstractEntity;
import org.dayatang.domain.InstanceFactory;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TheAbstractEntityTest extends AbstractIntegrationTest {

	private DictionaryCategory gender;

	@Before
	public void setUp() throws Exception {
        super.setUp();
		gender = createCategory("gender", 1);
	}

	private DictionaryCategory createCategory(String name, int sortOrder) {
		DictionaryCategory category = new DictionaryCategory();
		category.setName(name);
		category.setSortOrder(sortOrder);
		repository.save(category);
		return category;
	}

	@Test
	public void existed() {
		Dictionary dictionary = new Dictionary("2001", "双硕士", gender);
		assertFalse(dictionary.existed());
		dictionary = repository.save(dictionary);
		assertTrue(dictionary.existed());
		repository.remove(dictionary);
		repository.flush();
		assertFalse(dictionary.existed());
	}

	@Test
	public void propertyValueExisted() throws Exception {
		Dictionary dictionary = new Dictionary("2001", "双硕士", gender);
		assertFalse(dictionary.existed("text", "双硕士"));
		dictionary = repository.save(dictionary);
		assertTrue(dictionary.existed("text", "双硕士"));
		repository.remove(dictionary);
		repository.flush();
		assertFalse(dictionary.existed("text", "双硕士"));
	}

}
