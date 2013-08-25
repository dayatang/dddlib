/**
 *
 */
package com.dayatang.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.UserTransaction;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.commons.domain.Dictionary;
import com.dayatang.commons.domain.DictionaryCategory;
import com.dayatang.domain.AbstractEntity;
import com.dayatang.domain.Criterions;
import com.dayatang.domain.InstanceFactory;
import com.dayatang.domain.QueryCriterion;
import com.dayatang.domain.QuerySettings;

/**
 * 
 * @author yang
 */
public class QuerySettingsTest extends AbstractIntegrationTest {
	
	private UserTransaction tx;

	private EntityRepositoryHibernate repository;
	
	private QuerySettings<Dictionary> settings;

	private DictionaryCategory gender;

	private DictionaryCategory education;

	private Dictionary male;

	private Dictionary female;
	
	private Dictionary unknownGender;

	private Dictionary undergraduate;
	
	private Criterions criterions = Criterions.singleton();


	
	@Before
	public void setUp() throws Exception {
		tx = getTransaction();
		tx.begin();
		InstanceFactory.bind(SessionFactory.class, sessionFactory);
		repository = new EntityRepositoryHibernate();
		AbstractEntity.setRepository(repository);
		settings = QuerySettings.create(Dictionary.class);
		gender = createCategory("gender", 1);
		education = createCategory("education", 2);
		male = createDictionary("01", "男", gender, 100, "01");
		female = createDictionary("02", "女", gender, 150, "01");
		unknownGender = createDictionary("03", "未知", gender, 160, "01");
		undergraduate = createDictionary("01", "本科", education, 200, "05");
	}

	@After
	public void tearDown() throws Exception {
		tx.rollback();
		AbstractEntity.setRepository(null);
	}

	@Test
	public void testEq() {
		settings.eq("category", gender);
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testNotEq() {
		settings.notEq("category", gender);
		List<Dictionary> results = repository.find(settings);
		Dictionary dictionary = results.get(0);
		assertEquals(education, dictionary.getCategory());
	}

	@Test
	public void testGe() {
		settings.ge("sortOrder", 150);
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.contains(male));
		assertTrue(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testGt() {
		settings.gt("sortOrder", 150);
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.contains(male));
		assertFalse(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testLe() {
		settings.le("sortOrder", 150);
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testLt() {
		settings.lt("sortOrder", 150);
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertFalse(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testEqProp() {
		settings.eqProp("code", "parentCode");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertFalse(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testNotEqProp() {
		settings.notEqProp("code", "parentCode");
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.contains(male));
		assertTrue(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testGtProp() {
		settings.gtProp("code", "parentCode");
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.contains(male));
		assertTrue(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testGeProp() {
		settings.geProp("code", "parentCode");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testLtProp() {
		settings.ltProp("code", "parentCode");
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.contains(male));
		assertFalse(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testLeProp() {
		settings.leProp("code", "parentCode");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertFalse(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testSizeEq() {
		QuerySettings<DictionaryCategory> settings = QuerySettings.create(DictionaryCategory.class);
		settings.sizeEq("dictionaries", 3);
		List<DictionaryCategory> results = repository.find(settings);
		assertTrue(results.contains(gender));
		assertFalse(results.contains(education));
	}

	@Test
	public void testSizeNotEq() {
		QuerySettings<DictionaryCategory> settings = QuerySettings.create(DictionaryCategory.class);
		settings.sizeNotEq("dictionaries", 3);
		List<DictionaryCategory> results = repository.find(settings);
		assertFalse(results.contains(gender));
		assertTrue(results.contains(education));
	}

	@Test
	public void testSizeGt() {
		QuerySettings<DictionaryCategory> settings = QuerySettings.create(DictionaryCategory.class);
		settings.sizeGt("dictionaries", 1);
		List<DictionaryCategory> results = repository.find(settings);
		assertTrue(results.contains(gender));
		assertFalse(results.contains(education));
	}

	@Test
	public void testSizeGe() {
		QuerySettings<DictionaryCategory> settings = QuerySettings.create(DictionaryCategory.class);
		settings.sizeGe("dictionaries", 3);
		List<DictionaryCategory> results = repository.find(settings);
		assertTrue(results.contains(gender));
		assertFalse(results.contains(education));
	}

	@Test
	public void testSizeLt() {
		QuerySettings<DictionaryCategory> settings = QuerySettings.create(DictionaryCategory.class);
		settings.sizeLt("dictionaries", 3);
		List<DictionaryCategory> results = repository.find(settings);
		assertFalse(results.contains(gender));
		assertTrue(results.contains(education));
	}

	@Test
	public void testSizeLe() {
		QuerySettings<DictionaryCategory> settings = QuerySettings.create(DictionaryCategory.class);
		settings.sizeLe("dictionaries", 3);
		List<DictionaryCategory> results = repository.find(settings);
		assertTrue(results.contains(gender));
		assertTrue(results.contains(education));
	}

	@Test
	public void testIsEmpty() {
		DictionaryCategory empty = createCategory("a", 3);
		QuerySettings<DictionaryCategory> settings = QuerySettings.create(DictionaryCategory.class);
		settings.isEmpty("dictionaries");
		List<DictionaryCategory> results = repository.find(settings);
		assertTrue(results.contains(empty));
		assertFalse(results.contains(gender));
		assertFalse(results.contains(education));
	}

	@Test
	public void testNotEmpty() {
		DictionaryCategory empty = createCategory("a", 3);
		QuerySettings<DictionaryCategory> settings = QuerySettings.create(DictionaryCategory.class);
		settings.notEmpty("dictionaries");
		List<DictionaryCategory> results = repository.find(settings);
		assertFalse(results.contains(empty));
		assertTrue(results.contains(gender));
		assertTrue(results.contains(education));
	}

	@Test
	public void testContainsText() {
		settings.containsText("text", "科");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(undergraduate));
		assertFalse(results.contains(male));
		assertFalse(results.contains(female));
	}

	@Test
	public void testStartsWithText() {
		settings.startsWithText("text", "本");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(undergraduate));
		
		settings = QuerySettings.create(Dictionary.class).startsWithText("text", "科");
		results = repository.find(settings);
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testInEntity() {
		Set<DictionaryCategory> params = new HashSet<DictionaryCategory>();
		params.add(education);
		params.add(gender);
		settings.in("category", params);
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testInString() {
		Set<String> params = new HashSet<String>();
		params.add("男");
		params.add("女");
		settings.in("text", params);
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testInNull() {
		Collection<Object> value = null;
		settings.in("id", value);
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.isEmpty());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testInEmpty() {
		settings.in("id", Collections.EMPTY_LIST);
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.isEmpty());
	}

	@Test
	public void testNotInEntity() {
		Set<Long> params = new HashSet<Long>();
		params.add(male.getId());
		params.add(female.getId());
		settings.notIn("id", params);
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.contains(male));
		assertFalse(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testNotInString() {
		Set<String> params = new HashSet<String>();
		params.add("男");
		params.add("女");
		settings.notIn("text", params);
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.contains(male));
		assertFalse(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testNotInNull() {
		Collection<Object> value = null;
		settings.notIn("id", value);
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.isEmpty());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testNotInEmpty() {
		settings.notIn("id", Collections.EMPTY_LIST);
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.isEmpty());
	}

	@Test
	public void testIsNull() {
		settings.isNull("description");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testNotNull() {
		settings.notNull("text");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testBetween() {
		settings.between("parentCode", "01", "02");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testAnd() {
		QueryCriterion or = criterions.or(criterions.eq("code", "01"), criterions.eq("code", "02"));
		settings.and(or, criterions.eq("category", gender));
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertFalse(results.contains(unknownGender));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testOr() {
		QueryCriterion and = criterions.and(criterions.eq("code", "01"), criterions.eq("category", gender));
		settings.or(and, criterions.eq("category", education));
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertFalse(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testNot() {
		settings.not(criterions.eq("code", "01"));
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.contains(male));
		assertTrue(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testFindPaging() {
		createDictionary("08", "xyz", education, 150, "01");
		createDictionary("09", "xyy", education, 160, "02");
		settings.setFirstResult(1).setMaxResults(2);
		List<Dictionary> results = repository.find(settings);
		assertEquals(2, results.size());
	}

	@Test
	public void testFindOrder() {
		settings.asc("sortOrder");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.indexOf(male) < results.indexOf(female));
		assertTrue(results.indexOf(female) < results.indexOf(undergraduate));

		settings = QuerySettings.create(Dictionary.class).desc("sortOrder");
		results = repository.find(settings);
		assertTrue(results.indexOf(male) > results.indexOf(female));
		assertTrue(results.indexOf(female) > results.indexOf(undergraduate));
	}

	@Test
	public void testAlias() {
		List<Dictionary> results = repository.find(QuerySettings.create(Dictionary.class).eq("category.name", "education"));
		assertTrue(results.contains(undergraduate));
	}

	private DictionaryCategory createCategory(String name, int sortOrder) {
		DictionaryCategory category = new DictionaryCategory();
		category.setName(name);
		category.setSortOrder(sortOrder);
		repository.save(category);
		return category;
	}

	private Dictionary createDictionary(String code, String text, DictionaryCategory category, int sortOrder,
			String parentCode) {
		Dictionary dictionary = new Dictionary(code, text, category);
		dictionary.setSortOrder(sortOrder);
		dictionary.setParentCode(parentCode);
		repository.save(dictionary);
		return dictionary;
	}
}
