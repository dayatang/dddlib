/**
 *
 */
package org.dayatang.persistence.jpa;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ValidationException;

import org.dayatang.test.domain.Dictionary;
import org.dayatang.test.domain.DictionaryCategory;
import org.dayatang.domain.ArrayParameters;
import org.dayatang.domain.MapParameters;
import org.dayatang.domain.QueryParameters;
import org.dayatang.domain.QuerySettings;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author yang
 */
public class RepositoryJpaTest extends AbstractIntegrationTest {

    private DictionaryCategory gender;

    private DictionaryCategory education;

    private Dictionary male;

    private Dictionary undergraduate;

    private Dictionary graduate;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        gender = createCategory("gender", 1);
        education = createCategory("education", 2);
        male = createDictionary("01", "男", gender, 100, "01");
        undergraduate = createDictionary("01", "本科", education, 100, "05");
        graduate = createDictionary("02", "研究生", education, 200, "05");
    }

    @Test
    public void testAddAndRemove() {
        Dictionary dictionary = new Dictionary("2001", "双硕士", gender);
        assertFalse(dictionary.existed());
        Dictionary dictionary1 = repository.save(dictionary);
        assertTrue(dictionary.existed());
        assertNotNull(dictionary.getId());
        assertNotNull(dictionary1.getId());
        repository.remove(dictionary1);
        assertNull(repository.get(Dictionary.class, dictionary1.getId()));
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
    public void testGetUnmodified() {
        male.setText("xyz");
        Dictionary unmodified = repository.getUnmodified(Dictionary.class, male);
        assertEquals("男", unmodified.getText());
        assertEquals("xyz", male.getText());
    }

    @Test
    public void testFindQueryStringArrayParams() {
        String queryString = "select o from  Dictionary o where o.category = ?";
        QueryParameters params = ArrayParameters.create(gender);
        List<Dictionary> results = repository.find(queryString, params);
        assertTrue(results.contains(male));
        assertFalse(results.contains(undergraduate));
    }

    @Test
    public void testFindQueryStringMapParams() {
        String queryString = "select o from  Dictionary o where o.category = :category";
        MapParameters params = MapParameters.create().add("category", gender);
        List<Dictionary> results = repository.find(queryString, params);
        assertTrue(results.contains(male));
        assertFalse(results.contains(undergraduate));
    }

    @Test
    public void testFindNamedQueryArrayParams() {
        QueryParameters params = ArrayParameters.create(gender, "01");
        List<Dictionary> results = repository.findByNamedQuery("findByCategoryAndCode", params);
        assertTrue(results.contains(male));
        assertFalse(results.contains(undergraduate));
    }

    @Test
    public void testFindNamedQueryMapParams() {
        MapParameters params = MapParameters.create().add("category", gender);
        List<Dictionary> results = repository.findByNamedQuery("findByCategory", params);
        assertTrue(results.contains(male));
        assertFalse(results.contains(undergraduate));
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
        QueryParameters params = ArrayParameters.create(gender, "01");
        Dictionary dictionary = repository.getSingleResult(queryString, params);
        assertEquals(male, dictionary);
    }

    @Test
    public void testGetSingleResultMap() {
        String queryString = "select o from  Dictionary o where o.category = :category and o.code = :code";
        MapParameters params = MapParameters.create().add("category", gender).add("code", "01");
        Dictionary dictionary = repository.getSingleResult(queryString, params);
        assertEquals(male, dictionary);
    }

    @Test
    public void testExecuteUpdateArrayParams() {
        String description = "abcd";
        String queryString = "update Dictionary o set o.description = ? where o.category = ?";
        QueryParameters params = ArrayParameters.create(description, gender);
        repository.executeUpdate(queryString, params);
        entityManager.clear();
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
        MapParameters params = MapParameters.create().add("category", gender).add("description", description);
        repository.executeUpdate(queryString, params);
        entityManager.clear();
        QuerySettings<Dictionary> settings = QuerySettings.create(Dictionary.class).eq("category", gender);
        List<Dictionary> results = repository.find(settings);
        assertTrue(results.size() > 0);
        for (Dictionary dictionary : results) {
            assertEquals(description, dictionary.getDescription());
        }
    }

    @Test
    public void testQuery() {
        List<Dictionary> results = repository.createCriteriaQuery(Dictionary.class).eq("category", gender).list();
        assertTrue(results.contains(male));
        assertFalse(results.contains(undergraduate));
    }

    private DictionaryCategory createCategory(String name, int sortOrder) {
        DictionaryCategory category = new DictionaryCategory();
        category.setName(name);
        category.setSortOrder(sortOrder);
        entityManager.persist(category);
        return category;
    }

    private Dictionary createDictionary(String code, String text, DictionaryCategory category, int sortOrder,
            String parentCode) {
        Dictionary dictionary = new Dictionary(code, text, category);
        dictionary.setSortOrder(sortOrder);
        dictionary.setParentCode(parentCode);
        entityManager.persist(dictionary);
        return dictionary;
    }
}
