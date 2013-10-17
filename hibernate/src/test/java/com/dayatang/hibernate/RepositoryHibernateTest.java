/**
 *
 */
package com.dayatang.hibernate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.UserTransaction;
import javax.validation.ValidationException;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.commons.domain.Dictionary;
import com.dayatang.commons.domain.DictionaryCategory;
import com.dayatang.domain.AbstractEntity;
import com.dayatang.domain.ExampleSettings;
import com.dayatang.domain.InstanceFactory;
import com.dayatang.domain.QuerySettings;

/**
 * 
 * @author yang
 */
public class RepositoryHibernateTest extends AbstractIntegrationTest {
	
	
	private UserTransaction tx;

	private static EntityRepositoryHibernate repository;

	private DictionaryCategory gender;

	private DictionaryCategory education;

	private Dictionary male;

	private Dictionary undergraduate;
	
	private Dictionary graduate;
	
	private Dictionary associate;

	@Before
	public void setUp() throws Exception {
		tx = getTransaction();
		tx.begin();
		InstanceFactory.bind(SessionFactory.class, sessionFactory);
		repository = new EntityRepositoryHibernate();
		AbstractEntity.setRepository(repository);
		gender = createCategory("gender", 1);
		education = createCategory("education", 2);
		male = createDictionary("01", "男", gender, 100, "01");
		undergraduate = createDictionary("01", "本科", education, 100, "05");
		graduate = createDictionary("02", "研究生", education, 200, "05");
		associate = createDictionary("03", "专科", education, 300, "05");
	}

	@After
	public void tearDown() throws Exception {
		tx.rollback();
		AbstractEntity.setRepository(null);
	}

	@Test
	public void testAddAndRemove() {
		Dictionary dictionary = new Dictionary("2001", "双硕士", gender);
		assertFalse(dictionary.existed());
		dictionary = repository.save(dictionary);
		assertTrue(dictionary.existed());
		assertNotNull(dictionary.getId());
		repository.remove(dictionary);
		assertNull(repository.get(Dictionary.class, dictionary.getId()));
	}

	@Test
	public void testValidateFailure() {
		Dictionary dictionary = new Dictionary("", "", gender);
		try {
			dictionary.save();
			fail("应抛出异常！");
		} catch (ValidationException e) {
			System.out.println(e.getMessage());
			assertTrue(true);
		}
	}

	@Test
	public void testExistsById() {
		assertTrue(repository.exists(Dictionary.class, male.getId()));
		assertFalse(repository.exists(Dictionary.class, 1000L));
	}

	@Test
	public void testGet() {
		assertEquals(male, repository.get(Dictionary.class, male.getId()));
	}

	@Test
	public void testLoad() {
		assertEquals(male.getId(), repository.load(Dictionary.class, male.getId()).getId());
	}

	@Test
	public void testFindAll() {
		List<Dictionary> results = repository.findAll(Dictionary.class);
		assertTrue(results.containsAll(Arrays.asList(male, undergraduate, graduate, associate)));
	}
	
	
	@Test
	public void testGetUnmodified() {
		male.setText("xyz");
		Dictionary unmodified = repository.getUnmodified(Dictionary.class, male);
		assertEquals("男", unmodified.getText());
		assertEquals("xyz", male.getText());
	}

	@Test
	public void testFindQueryStringArrayParams() {
		String queryString = "select o from  Dictionary o where o.category = ?";
		Object[] params = new Object[] { gender };
		List<Dictionary> results = repository.find(queryString, params, Dictionary.class);
		assertTrue(results.contains(male));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testFindQueryStringMapParams() {
		String queryString = "select o from  Dictionary o where o.category = :category";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("category", gender);
		List<Dictionary> results = repository.find(queryString, params, Dictionary.class);
		assertTrue(results.contains(male));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testFindNamedQueryArrayParams() {
		Object[] params = new Object[] { gender, "01" };
		List<Dictionary> results = repository.findByNamedQuery("findByCategoryAndCode", params, Dictionary.class);
		assertTrue(results.contains(male));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testFindNamedQueryMapParams() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("category", gender);
		List<Dictionary> results = repository.findByNamedQuery("findByCategory", params, Dictionary.class);
		assertTrue(results.contains(male));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testFindByExample() {
		Dictionary example = new Dictionary(null, "本", null);
		List<Dictionary> dictionaries = repository.findByExample(example, ExampleSettings.create(Dictionary.class).excludeZeroes());
		assertFalse(dictionaries.contains(male));
		assertFalse(dictionaries.contains(undergraduate));
		dictionaries = repository.findByExample(example, ExampleSettings.create(Dictionary.class).excludeZeroes().enableLike());
		assertTrue(dictionaries.contains(undergraduate));
		assertFalse(dictionaries.contains(male));
	}

	
	@Test
	public void testFindByProperty() {
		List<Dictionary> results = repository.findByProperty(Dictionary.class, "category", education);
		assertTrue(results.contains(undergraduate));
		assertTrue(results.contains(graduate));
		assertFalse(results.contains(male));
	}

	
	@Test
	public void testFindByProperties() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("category", education);
		params.put("code", "02");
		List<Dictionary> results = repository.findByProperties(Dictionary.class, params);
		assertTrue(results.contains(graduate));
		assertFalse(results.contains(undergraduate));
		assertFalse(results.contains(male));
	}
	
	
	@Test
	public void testGetSingleResultSettings() {
		QuerySettings<Dictionary> settings = QuerySettings.create(Dictionary.class).eq("category", gender)
				.eq("code", "01");
		Dictionary dictionary = repository.getSingleResult(settings);
		assertEquals(male, dictionary);
	}

	@Test
	public void testGetSingleResultArray() {
		String queryString = "select o from  Dictionary o where o.category = ? and o.code = ?";
		Object[] params = new Object[] { gender, "01" };
		Dictionary dictionary = (Dictionary) repository.getSingleResult(queryString, params, Dictionary.class);
		assertEquals(male, dictionary);
	}

	@Test
	public void testGetSingleResultMap() {
		String queryString = "select o from  Dictionary o where o.category = :category and o.code = :code";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("category", gender);
		params.put("code", "01");
		Dictionary dictionary = (Dictionary) repository.getSingleResult(queryString, params, Dictionary.class);
		assertEquals(male, dictionary);
	}

	@Test
	public void testExecuteUpdateArrayParams() throws Exception {
		String description = "abcd";
		String queryString = "update Dictionary o set o.description = ? where o.category = ?";
		repository.executeUpdate(queryString, new Object[] { description, gender });
		sessionFactory.getCurrentSession().clear();
		//session.clear();
		QuerySettings<Dictionary> settings = QuerySettings.create(Dictionary.class).eq("category", gender);
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.size() > 0);
		for (Dictionary dictionary : results) {
			assertEquals(description, dictionary.getDescription());
		}
	}

	@Test
	public void testExecuteUpdateMapParams() {
		String description = "abcd";
		String queryString = "update Dictionary set description = :description where category = :category";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("category", gender);
		params.put("description", description);
		repository.executeUpdate(queryString, params);
		sessionFactory.getCurrentSession().clear();
		//session.clear();
		QuerySettings<Dictionary> settings = QuerySettings.create(Dictionary.class).eq("category", gender);
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.size() > 0);
		for (Dictionary dictionary : results) {
			assertEquals(description, dictionary.getDescription());
		}
	}

	@Test
	public void testFindAllDataPage() {
		DataPage<Dictionary> results = repository.findAll(Dictionary.class, 1, 2);
		assertThat(1, is(results.getPageIndex()));
		assertThat(2, is(results.getPageSize()));
		assertThat(2, is(results.getPageCount()));
		assertThat(4L, is(results.getResultCount()));
		assertThat(2, is(results.getPageData().size()));
	}
	
	
	@Test
	public void testFindQueryStringArrayParamsDataPage() {
		String queryString = "select o from  Dictionary o where o.category = ?";
		Object[] params = new Object[] { education };
		DataPage<Dictionary> results = repository.find(queryString, params, 1, 2, Dictionary.class);
		assertThat(1, is(results.getPageIndex()));
		assertThat(2, is(results.getPageSize()));
		assertThat(2, is(results.getPageCount()));
		assertThat(3L, is(results.getResultCount()));
		assertThat(2, is(results.getPageData().size()));
		assertTrue(results.getPageData().contains(undergraduate));
		assertTrue(results.getPageData().contains(graduate));
		assertFalse(results.getPageData().contains(associate));
	}

	@Test
	public void testFindQueryStringMapParamsDataPage() {
		String queryString = "select o from  Dictionary o where o.category = :category";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("category", education);
		DataPage<Dictionary> results = repository.find(queryString, params, 2, 2, Dictionary.class);
		assertThat(2, is(results.getPageIndex()));
		assertThat(2, is(results.getPageSize()));
		assertThat(2, is(results.getPageCount()));
		assertThat(3L, is(results.getResultCount()));
		assertThat(1, is(results.getPageData().size()));
		assertFalse(results.getPageData().contains(undergraduate));
		assertFalse(results.getPageData().contains(graduate));
		assertTrue(results.getPageData().contains(associate));
	}


	@Test
	public void testFindNamedQueryArrayParamsDataPage() {
		DataPage<Dictionary> results = repository.findByNamedQuery("findByCategoryArrayParams", new Object[] {education}, 1, 2, Dictionary.class);
		assertThat(1, is(results.getPageIndex()));
		assertThat(2, is(results.getPageSize()));
		assertThat(2, is(results.getPageCount()));
		assertThat(3L, is(results.getResultCount()));
		assertThat(2, is(results.getPageData().size()));
		assertTrue(results.getPageData().contains(undergraduate));
		assertTrue(results.getPageData().contains(graduate));
		assertFalse(results.getPageData().contains(associate));
	}

	@Test
	public void testFindNamedQueryMapParamsDataPage() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("category", education);
		DataPage<Dictionary> results = repository.findByNamedQuery("findByCategory", params, 2, 2, Dictionary.class);
		assertThat(2, is(results.getPageIndex()));
		assertThat(2, is(results.getPageSize()));
		assertThat(2, is(results.getPageCount()));
		assertThat(3L, is(results.getResultCount()));
		assertThat(1, is(results.getPageData().size()));
		assertFalse(results.getPageData().contains(undergraduate));
		assertFalse(results.getPageData().contains(graduate));
		assertTrue(results.getPageData().contains(associate));
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
