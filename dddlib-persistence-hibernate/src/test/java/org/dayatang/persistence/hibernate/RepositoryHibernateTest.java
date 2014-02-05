/**
 *
 */
package org.dayatang.persistence.hibernate;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ValidationException;

import org.dayatang.domain.*;
import org.dayatang.test.domain.Dictionary;
import org.dayatang.test.domain.DictionaryCategory;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author yang
 */
public class RepositoryHibernateTest extends AbstractIntegrationTest {

    private DictionaryCategory gender;

    private DictionaryCategory education;

    private Dictionary male;

    private Dictionary undergraduate;

    private Dictionary graduate;

    private Dictionary associate;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        gender = createCategory("gender", 1);
        education = createCategory("education", 2);
        male = createDictionary("01", "男", gender, 100, "01");
        undergraduate = createDictionary("01", "本科", education, 100, "05");
        graduate = createDictionary("02", "研究生", education, 200, "05");
        associate = createDictionary("03", "专科", education, 300, "05");
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
    public void testGetSingleResultSettings() {
        Dictionary dictionary = repository.createCriteriaQuery(Dictionary.class).eq("category", gender)
                .eq("code", "01").singleResult();
        assertEquals(male, dictionary);
    }

    @Test
    public void testFindQueryStringMapParams() {
        String queryString = "select o from  Dictionary o where o.category = :category";
        List<Dictionary> results = repository.createJpqlQuery(queryString).addParameter("category", gender).list();
        assertTrue(results.contains(male));
        assertFalse(results.contains(undergraduate));
    }

    @Test
    public void testFindNamedQueryArrayParams() {
        List<Dictionary> results = repository.createNamedQuery("findByCategoryAndCode").setParameters(gender, "01").list();
        assertTrue(results.contains(male));
        assertFalse(results.contains(undergraduate));
    }

    @Test
    public void testFindNamedQueryMapParams() {
        List<Dictionary> results = repository.createNamedQuery("findByCategory").addParameter("category", gender).list();
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
    public void testGetSingleResultArray() {
        String queryString = "select o from  Dictionary o where o.category = ? and o.code = ?";
        Dictionary dictionary = repository.createJpqlQuery(queryString).setParameters(gender, "01").singleResult();
        assertEquals(male, dictionary);
    }

    @Test
    public void testGetSingleResultMap() {
        String queryString = "select o from  Dictionary o where o.category = :category and o.code = :code";
        Dictionary dictionary = repository.createJpqlQuery(queryString)
                .addParameter("category", gender).addParameter("code", "01").singleResult();
        assertEquals(male, dictionary);
    }

    @Test
    public void testExecuteUpdateArrayParams() throws Exception {
        String description = "abcd";
        String queryString = "update Dictionary o set o.description = ? where o.category = ?";
        new JpqlQuery(repository, queryString).setParameters(description, gender).executeUpdate();
        sessionFactory.getCurrentSession().clear();
        //session.clear();
        CriteriaQuery criteriaQuery = new CriteriaQuery(repository, Dictionary.class).eq("category", gender);
        List<Dictionary> results = criteriaQuery.list();
        assertTrue(results.size() > 0);
        for (Dictionary dictionary : results) {
            assertEquals(description, dictionary.getDescription());
        }
    }

    @Test
    public void testExecuteUpdateMapParams() {
        String description = "abcd";
        String queryString = "update Dictionary set description = :description where category = :category";
        new JpqlQuery(repository, queryString).addParameter("description", description)
                .addParameter("category", gender).executeUpdate();
        sessionFactory.getCurrentSession().clear();
        //session.clear();
        CriteriaQuery criteriaQuery = new CriteriaQuery(repository, Dictionary.class).eq("category", gender);
        List<Dictionary> results = criteriaQuery.list();
        assertTrue(results.size() > 0);
        for (Dictionary dictionary : results) {
            assertEquals(description, dictionary.getDescription());
        }
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
