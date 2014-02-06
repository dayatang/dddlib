/**
 *
 */
package org.dayatang.persistence.hibernate;

import org.dayatang.domain.CriteriaQuery;
import org.dayatang.domain.Criterions;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.domain.QueryCriterion;
import org.dayatang.test.domain.Dictionary;
import org.dayatang.test.domain.DictionaryCategory;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * 
 * @author yang
 */
public class CriteriaQueryTest extends AbstractIntegrationTest {

	private CriteriaQuery instance;

    private CriteriaQuery instance2;

	private DictionaryCategory gender;

	private DictionaryCategory education;

	private Dictionary male;

	private Dictionary female;
	
	private Dictionary unknownGender;

	private Dictionary undergraduate;
	
	private Criterions criterions = InstanceFactory.getInstance(Criterions.class);


	
	@Before
	public void setUp() throws Exception {
        super.setUp();
		instance = new CriteriaQuery(repository, Dictionary.class);
        instance2 = new CriteriaQuery(repository, DictionaryCategory.class);
		gender = createCategory("gender", 1);
		education = createCategory("education", 2);
		male = createDictionary("01", "男", gender, 100, "01");
		female = createDictionary("02", "女", gender, 150, "01");
		unknownGender = createDictionary("03", "未知", gender, 160, "01");
		undergraduate = createDictionary("01", "本科", education, 200, "05");
	}

	@Test
	public void testEq() {
		instance.eq("category", gender);
		List<Dictionary> results = repository.find(instance);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testNotEq() {
		instance.notEq("category", gender);
		List<Dictionary> results = repository.find(instance);
		Dictionary dictionary = results.get(0);
		assertEquals(education, dictionary.getCategory());
	}

	@Test
	public void testGe() {
		instance.ge("sortOrder", 150);
		List<Dictionary> results = repository.find(instance);
		assertFalse(results.contains(male));
		assertTrue(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testGt() {
		instance.gt("sortOrder", 150);
		List<Dictionary> results = repository.find(instance);
		assertFalse(results.contains(male));
		assertFalse(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testLe() {
		instance.le("sortOrder", 150);
		List<Dictionary> results = repository.find(instance);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testLt() {
		instance.lt("sortOrder", 150);
		List<Dictionary> results = repository.find(instance);
		assertTrue(results.contains(male));
		assertFalse(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testEqProp() {
		instance.eqProp("code", "parentCode");
		List<Dictionary> results = repository.find(instance);
		assertTrue(results.contains(male));
		assertFalse(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testNotEqProp() {
		instance.notEqProp("code", "parentCode");
		List<Dictionary> results = repository.find(instance);
		assertFalse(results.contains(male));
		assertTrue(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testGtProp() {
		instance.gtProp("code", "parentCode");
		List<Dictionary> results = repository.find(instance);
		assertFalse(results.contains(male));
		assertTrue(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testGeProp() {
		instance.geProp("code", "parentCode");
		List<Dictionary> results = repository.find(instance);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testLtProp() {
		instance.ltProp("code", "parentCode");
		List<Dictionary> results = repository.find(instance);
		assertFalse(results.contains(male));
		assertFalse(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testLeProp() {
		instance.leProp("code", "parentCode");
		List<Dictionary> results = repository.find(instance);
		assertTrue(results.contains(male));
		assertFalse(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testSizeEq() {
        instance2.sizeEq("dictionaries", 3);
		List<DictionaryCategory> results = repository.find(instance2);
		assertTrue(results.contains(gender));
		assertFalse(results.contains(education));
	}

	@Test
	public void testSizeNotEq() {
        instance2.sizeNotEq("dictionaries", 3);
		List<DictionaryCategory> results = repository.find(instance2);
		assertFalse(results.contains(gender));
		assertTrue(results.contains(education));
	}

	@Test
	public void testSizeGt() {
        instance2.sizeGt("dictionaries", 1);
		List<DictionaryCategory> results = repository.find(instance2);
		assertTrue(results.contains(gender));
		assertFalse(results.contains(education));
	}

	@Test
	public void testSizeGe() {
        instance2.sizeGe("dictionaries", 3);
		List<DictionaryCategory> results = repository.find(instance2);
		assertTrue(results.contains(gender));
		assertFalse(results.contains(education));
	}

	@Test
	public void testSizeLt() {
        instance2.sizeLt("dictionaries", 3);
		List<DictionaryCategory> results = repository.find(instance2);
		assertFalse(results.contains(gender));
		assertTrue(results.contains(education));
	}

	@Test
	public void testSizeLe() {
        instance2.sizeLe("dictionaries", 3);
		List<DictionaryCategory> results = repository.find(instance2);
		assertTrue(results.contains(gender));
		assertTrue(results.contains(education));
	}

	@Test
	public void testIsEmpty() {
		DictionaryCategory empty = createCategory("a", 3);
        instance2.isEmpty("dictionaries");
		List<DictionaryCategory> results = repository.find(instance2);
		assertTrue(results.contains(empty));
		assertFalse(results.contains(gender));
		assertFalse(results.contains(education));
	}

	@Test
	public void testNotEmpty() {
		DictionaryCategory empty = createCategory("a", 3);
        instance2.notEmpty("dictionaries");
		List<DictionaryCategory> results = repository.find(instance2);
		assertFalse(results.contains(empty));
		assertTrue(results.contains(gender));
		assertTrue(results.contains(education));
	}

	@Test
	public void testContainsText() {
		instance.containsText("text", "科");
		List<Dictionary> results = repository.find(instance);
		assertTrue(results.contains(undergraduate));
		assertFalse(results.contains(male));
		assertFalse(results.contains(female));
	}

	@Test
	public void testStartsWithText() {
		instance.startsWithText("text", "本");
		List<Dictionary> results = repository.find(instance);
		assertTrue(results.contains(undergraduate));
		
		instance =  new CriteriaQuery(repository, Dictionary.class).startsWithText("text", "科");
		results = repository.find(instance);
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testInEntity() {
		Set<DictionaryCategory> params = new HashSet<DictionaryCategory>();
		params.add(education);
		params.add(gender);
		instance.in("category", params);
		List<Dictionary> results = repository.find(instance);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testInString() {
		Set<String> params = new HashSet<String>();
		params.add("男");
		params.add("女");
		instance.in("text", params);
		List<Dictionary> results = repository.find(instance);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testInNull() {
		Collection<Object> value = null;
		instance.in("id", value);
		List<Dictionary> results = repository.find(instance);
		assertTrue(results.isEmpty());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testInEmpty() {
		instance.in("id", Collections.EMPTY_LIST);
		List<Dictionary> results = repository.find(instance);
		assertTrue(results.isEmpty());
	}

	@Test
	public void testNotInEntity() {
		Set<Long> params = new HashSet<Long>();
		params.add(male.getId());
		params.add(female.getId());
		instance.notIn("id", params);
		List<Dictionary> results = repository.find(instance);
		assertFalse(results.contains(male));
		assertFalse(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testNotInString() {
		Set<String> params = new HashSet<String>();
		params.add("男");
		params.add("女");
		instance.notIn("text", params);
		List<Dictionary> results = repository.find(instance);
		assertFalse(results.contains(male));
		assertFalse(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testNotInNull() {
		Collection<Object> value = null;
		instance.notIn("id", value);
		List<Dictionary> results = repository.find(instance);
		assertFalse(results.isEmpty());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testNotInEmpty() {
		instance.notIn("id", Collections.EMPTY_LIST);
		List<Dictionary> results = repository.find(instance);
		assertFalse(results.isEmpty());
	}

	@Test
	public void testIsNull() {
		instance.isNull("description");
		List<Dictionary> results = repository.find(instance);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testNotNull() {
		instance.notNull("text");
		List<Dictionary> results = repository.find(instance);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testBetween() {
		instance.between("parentCode", "01", "02");
		List<Dictionary> results = repository.find(instance);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testAnd() {
		QueryCriterion or = criterions.or(criterions.eq("code", "01"), criterions.eq("code", "02"));
		instance.and(or, criterions.eq("category", gender));
		List<Dictionary> results = repository.find(instance);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertFalse(results.contains(unknownGender));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testOr() {
		QueryCriterion and = criterions.and(criterions.eq("code", "01"), criterions.eq("category", gender));
		instance.or(and, criterions.eq("category", education));
		List<Dictionary> results = repository.find(instance);
		assertTrue(results.contains(male));
		assertFalse(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testNot() {
		instance.not(criterions.eq("code", "01"));
		List<Dictionary> results = repository.find(instance);
		assertFalse(results.contains(male));
		assertTrue(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testFindPaging() {
		createDictionary("08", "xyz", education, 150, "01");
		createDictionary("09", "xyy", education, 160, "02");
		instance.setFirstResult(1).setMaxResults(2);
		List<Dictionary> results = repository.find(instance);
		assertEquals(2, results.size());
	}

	@Test
	public void testFindOrder() {
		instance.asc("sortOrder");
		List<Dictionary> results = repository.find(instance);
		assertTrue(results.indexOf(male) < results.indexOf(female));
		assertTrue(results.indexOf(female) < results.indexOf(undergraduate));

		instance = new CriteriaQuery(repository, Dictionary.class).desc("sortOrder");
		results = repository.find(instance);
		assertTrue(results.indexOf(male) > results.indexOf(female));
		assertTrue(results.indexOf(female) > results.indexOf(undergraduate));
	}

	@Test
	public void testAlias() {
		List<Dictionary> results = repository.find(instance.eq("category.name", "education"));
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
