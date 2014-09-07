/*
 * Copyright 2014 Dayatang Open Source..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dayatang.persistence.hibernate;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import javax.validation.ValidationException;
import org.dayatang.domain.CriteriaQuery;
import org.dayatang.domain.ExampleSettings;
import org.dayatang.domain.JpqlQuery;
import org.dayatang.domain.NamedParameters;
import org.dayatang.domain.NamedQuery;
import org.dayatang.domain.SqlQuery;
import org.dayatang.persistence.test.domain.Dictionary;
import org.dayatang.persistence.test.domain.DictionaryCategory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author yyang
 */
public class EntityRepositoryHibernateTest extends AbstractIntegrationTest {

    private DictionaryCategory gender;

    private DictionaryCategory education;

    private Dictionary male;

    private Dictionary undergraduate;

    private Dictionary graduate;

    private Dictionary associate;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        gender = createCategory("gender", 1);
        education = createCategory("education", 2);
        male = createDictionary("01", "男", gender, 100, "01");
        undergraduate = createDictionary("01", "本科", education, 100, "05");
        graduate = createDictionary("02", "研究生", education, 200, "05");
        associate = createDictionary("03", "专科", education, 300, "05");
    }

    private DictionaryCategory createCategory(String name, int sortOrder) {
        DictionaryCategory category = new DictionaryCategory();
        category.setName(name);
        category.setSortOrder(sortOrder);
        repository.save(category);
        repository.flush();
        return category;
    }

    private Dictionary createDictionary(String code, String text, DictionaryCategory category, int sortOrder,
            String parentCode) {
        Dictionary dictionary = new Dictionary(code, text, category);
        dictionary.setSortOrder(sortOrder);
        dictionary.setParentCode(parentCode);
        repository.save(dictionary);
        repository.flush();
        return dictionary;
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
            repository.flush();
            fail("应抛出异常！");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            assertTrue(true);
        }
    }

    /**
     * Test of exists method
     */
    @Test
    public void testExists() {
        assertTrue(repository.exists(Dictionary.class, male.getId()));
        assertFalse(repository.exists(Dictionary.class, 1000L));
    }

    /**
     * Test of get method
     */
    @Test
    public void testGet() {
        assertEquals(male, repository.get(Dictionary.class, male.getId()));
    }

    /**
     * Test of load method
     */
    @Test
    public void testLoad() {
        assertEquals(male.getId(), repository.load(Dictionary.class, male.getId()).getId());
    }

    /**
     * Test of getUnmodified method
     */
    @Test
    public void testGetUnmodified() {
        male.setText("xyz");
        Dictionary unmodified = repository.getUnmodified(Dictionary.class, male);
        assertEquals("男", unmodified.getText());
        assertEquals("xyz", male.getText());
    }

    /**
     * Test of getByBusinessKeys method
     */
    @Test
    public void testGetByBusinessKeys() {
        NamedParameters params = NamedParameters.create()
                .add("category", education)
                .add("code", "02");
        Dictionary result = repository.getByBusinessKeys(Dictionary.class, params);
        assertEquals(graduate, result);
    }

    /**
     * Test of findAll method
     */
    @Test
    public void testFindAll() {
        List<Dictionary> results = repository.findAll(Dictionary.class);
        assertTrue(results.containsAll(Arrays.asList(male, undergraduate, graduate, associate)));
    }

    /**
     * Test of createCritriaQuery method
     */
    @Test
    public void testCreateCriteriaQuery() {
        CriteriaQuery query = repository.createCriteriaQuery(Dictionary.class);
        assertEquals(Dictionary.class, query.getEntityClass());
    }

    /**
     * Test of find method with CriteriaQuery as parameter
     */
    @Test
    public void testCriteriaQueryFind() {
        CriteriaQuery query = new CriteriaQuery(repository, Dictionary.class)
                .eq("category", education);
        List<Dictionary> results = repository.find(query);
        assertTrue(results.contains(graduate));
        assertTrue(results.contains(undergraduate));
    }

    /**
     * Test of getSingleResult method with CriteriaQuery as parameter
     */
    @Test
    public void testCriteriaQueryGetSingleResult() {
        CriteriaQuery query = new CriteriaQuery(repository, Dictionary.class)
                .eq("category", gender)
                .eq("code", "01");
        assertEquals(male, repository.getSingleResult(query));
    }

    /**
     * Test of createJpqlQuery method
     */
    @Test
    public void testCreateJpqlQuery() {
        String jpql = "select o from Dictionary o";
        JpqlQuery query = repository.createJpqlQuery(jpql);
        assertEquals(jpql, query.getJpql());
    }

    /**
     * Test of find method with JpqlQuery as parameter
     */
    @Test
    public void testJpqlQueryFindWithArrayParameters() {
        String queryString = "select o from  Dictionary o where o.category = ?";
        JpqlQuery query = new JpqlQuery(repository, queryString).setParameters(gender);
        List<Dictionary> results = repository.find(query);
        assertTrue(results.contains(male));
        assertFalse(results.contains(undergraduate));
    }

    /**
     * Test of find method with JpqlQuery as parameter
     */
    @Test
    public void testJpqlQueryFindWithMapParameters() {
        String queryString = "select o from  Dictionary o where o.category = :category";
        JpqlQuery query = new JpqlQuery(repository, queryString)
                .addParameter("category", gender);
        List<Dictionary> results = repository.find(query);
        assertTrue(results.contains(male));
        assertFalse(results.contains(undergraduate));
    }

    /**
     * Test of getSingleResult method with JpqlQuery as parameter
     */
    @Test
    public void testJpqlQueryGetSingleResult() {
        String queryString = "select o from  Dictionary o where o.category = :category and o.code = :code";
        JpqlQuery query = new JpqlQuery(repository, queryString)
                .addParameter("category", gender)
                .addParameter("code", "01");
        assertEquals(male, repository.getSingleResult(query));
    }

    /**
     * Test of getSingleResult method with JpqlQuery as parameter
     */
    @Test
    public void testJpqlQueryGetSingleResultCount() {
        String queryString = "select count(o) from  Dictionary o where o.category = :category and o.code = :code";
        JpqlQuery query = new JpqlQuery(repository, queryString)
                .addParameter("category", gender)
                .addParameter("code", "01");
        assertEquals(1L, repository.getSingleResult(query));
    }
    
    
    /**
     * Test of find method with JpqlQuery as parameter and scalar as result
     */
    @Test
    public void testJpqlQueryScalar() {
        String queryString = "select o.code, o.text from  Dictionary o where o.category = :category";
        JpqlQuery query = new JpqlQuery(repository, queryString)
                .addParameter("category", gender);
        List<Object[]> results = repository.find(query);
        for (Object[] row : results) {
            System.out.println(Arrays.toString(row));
        }
    }

    /**
     * Test of executeUpdate method with JpqlQuery as parameter
     */
    @Test
    public void testJpqlQueryExecuteUpdate() {
        String description = "abcd";
        String queryString = "update Dictionary set description = :description where category = :category";
        JpqlQuery query = new JpqlQuery(repository, queryString)
                .addParameter("description", description)
                .addParameter("category", gender);
        int count = repository.executeUpdate(query);
        assertTrue(count > 0);
        sessionFactory.getCurrentSession().clear();
        List<Dictionary> results = repository.createCriteriaQuery(Dictionary.class).eq("category", gender).list();
        assertTrue(results.size() > 0);
        for (Dictionary dictionary : results) {
            assertEquals(description, dictionary.getDescription());
        }
    }

    /**
     * Test of createNamedQuery method
     */
    @Test
    public void testCreateNamedQuery() {
        String queryName = "Dictionary.findByCategory";
        NamedQuery query = repository.createNamedQuery(queryName);
        assertEquals(queryName, query.getQueryName());
    }

    /**
     * Test of find method with NamedQuery as parameter
     */
    @Test
    public void testNamedQueryFindWithArrayParameters() {
        NamedQuery query = new NamedQuery(repository, "Dictionay.findByCategoryArrayParams")
                .setParameters(gender);
        List<Dictionary> results = repository.find(query);
        assertTrue(results.contains(male));
        assertFalse(results.contains(undergraduate));
    }

    /**
     * Test of find method with NamedQuery as parameter
     */
    @Test
    public void testNamedQueryFindWithMapParameters() {
        NamedQuery query = new NamedQuery(repository, "Dictionay.findByCategory")
                .addParameter("category", gender);
        List<Dictionary> results = repository.find(query);
        assertTrue(results.contains(male));
        assertFalse(results.contains(undergraduate));
    }

    /**
     * Test of getSingleResult method with NamedQuery as parameter
     */
    @Test
    public void testNamedQueryGetSingleResult() {
        NamedQuery query = new NamedQuery(repository, "Dictionay.findByCategoryAndCode")
                .setParameters(gender, "01");
        assertEquals(male, repository.getSingleResult(query));
    }

    /**
     * Test of getSingleResult method with JpqlQuery as parameter
     */
    @Test
    public void testNamedQueryGetSingleResultCount() {
        String queryName = "DictionaryCategory.getCount";
        NamedQuery query = new NamedQuery(repository, queryName)
                .addParameter("name", "gender");
        
        assertEquals(1L, repository.getSingleResult(query));
    }

    /**
     * Test of find method with NamedQuery as parameter and scalar as result
     */
    @Test
    public void testNamedQueryScalar() {
        NamedQuery query = new NamedQuery(repository, "Dictionay.findNameAndOrder")
                .addParameter("category", gender);
        List<Object[]> results = repository.find(query);
        for (Object[] row : results) {
            System.out.println(Arrays.toString(row));
        }
    }

    /**
     * Test of executeUpdate method with NamedQuery as parameter
     */
    @Test
    public void testNamedQueryExecuteUpdate() {
        String description = "abcd";
        NamedQuery query = new NamedQuery(repository, "Dictionay.updateDescription")
                .addParameter("description", description)
                .addParameter("category", gender);
        int count = repository.executeUpdate(query);
        assertTrue(count > 0);
        sessionFactory.getCurrentSession().clear();
        List<Dictionary> results = repository.createCriteriaQuery(Dictionary.class).eq("category", gender).list();
        assertTrue(results.size() > 0);
        for (Dictionary dictionary : results) {
            assertEquals(description, dictionary.getDescription());
        }
    }

    /**
     * Test of createSqlQuery method
     */
    @Test
    public void testCreateSqlQuery() {
        String sql = "select * from dictionaries";
        SqlQuery query = repository.createSqlQuery(sql);
        assertEquals(sql, query.getSql());
    }

    /**
     * Test of find method with SqlQuery as parameter
     */
    @Test
    public void testSqlQueryFindWithArrayParameters() {
        String sql = "select * from dictionaries as o where o.is_disabled = ? and o.category_id = ? order by o.sort_order";
        SqlQuery query = new SqlQuery(repository, sql)
                .setParameters(false, gender.getId())
                .setResultEntityClass(Dictionary.class);
        List<Dictionary> results = repository.find(query);
        assertTrue(results.contains(male));
        assertFalse(results.contains(undergraduate));
    }

    /**
     * Test of find method with SqlQuery as parameter
     */
    @Test
    public void testSqlQueryFindWithMapParameters() {
        String sql = "select * from dictionaries as o where o.is_disabled = :disabled "
                + "and o.category_id = :categoryId order by o.sort_order";
        SqlQuery query = new SqlQuery(repository, sql)
                .addParameter("categoryId", gender.getId())
                .addParameter("disabled", false)
                .setResultEntityClass(Dictionary.class);
        List<Dictionary> results = repository.find(query);
        assertTrue(results.contains(male));
        assertFalse(results.contains(undergraduate));
    }

    /**
     * Test of getSingleResult method with SqlQuery as parameter
     */
    @Test
    public void testSqlQueryGetSingleResult() {
        String sql = "select * from dictionaries as o where o.is_disabled = ? "
                + "and o.category_id = ? and o.code = ?";
        SqlQuery query = new SqlQuery(repository, sql)
                .setParameters(false, gender.getId(), "01")
                .setResultEntityClass(Dictionary.class);
        assertEquals(male, repository.getSingleResult(query));
    }

    /**
     * Test of getSingleResult method with JpqlQuery as parameter
     */
    @Test
    public void testSqlQueryGetSingleResultCount() {
        String queryString = "select count(*) from  categories o where o.name = :name";
        SqlQuery query = new SqlQuery(repository, queryString)
                .addParameter("name", "gender");
        assertEquals(1, ((Number)repository.getSingleResult(query)).longValue());
    }

    /**
     * Test of find method with SqlQuery as parameter and scalar as result
     */
    @Test
    public void testSqlQueryScalar() {
        String sql = "select o.code, o.text from  dictionaries o where o.category_id = :category";
        SqlQuery query = new SqlQuery(repository, sql)
                .addParameter("category", gender);
        List<Object[]> results = repository.find(query);
        for (Object[] row : results) {
            System.out.println(Arrays.toString(row));
        }
    }

    /**
     * Test of executeUpdate method with SqlQuery as parameter
     */
    @Test
    public void testSqlQueryExecuteUpdate() {
        String description = "abcd";
        String sql = "update dictionaries set description = :description where category_id = :categoryId";
        SqlQuery query = new SqlQuery(repository, sql)
                .addParameter("description", description)
                .addParameter("categoryId", gender.getId());
        int count = repository.executeUpdate(query);
        assertTrue(count > 0);
        sessionFactory.getCurrentSession().clear();
        List<Dictionary> results = repository.createCriteriaQuery(Dictionary.class).eq("category", gender).list();
        assertTrue(results.size() > 0);
        for (Dictionary dictionary : results) {
            assertEquals(description, dictionary.getDescription());
        }
    }
    /**
     * Test of findByExample method
     */
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

    /**
     * Test of findByProperty method
     */
    @Test
    public void testFindByProperty() {
        List<Dictionary> results = repository.findByProperty(Dictionary.class, "category", education);
        assertTrue(results.contains(undergraduate));
        assertTrue(results.contains(graduate));
        assertFalse(results.contains(male));
    }

    /**
     * Test of findByProperties method
     */
    @Test
    public void testFindByProperties() {
        NamedParameters params = NamedParameters.create()
                .add("category", education)
                .add("code", "02");
        List<Dictionary> results = repository.findByProperties(Dictionary.class, params);
        assertTrue(results.contains(graduate));
        assertFalse(results.contains(undergraduate));
        assertFalse(results.contains(male));
    }

    
    @Test
    public void testGetQueryStringOfNamedQuery() {
        assertEquals("select o.code, o.text from  Dictionary o where o.category = :category",
                repository.getQueryStringOfNamedQuery("Dictionay.findNameAndOrder"));
    }
    
    /**
     * Test of flush method
     */
    @Test
    public void testFlush() {
        String description = "Ha Ha Ha!";
        male.setDescription(description);
        repository.flush();
        Dictionary male1 = repository.get(Dictionary.class, male.getId());
        assertEquals(description, male1.getDescription());
    }

    /**
     * Test of refresh method
     */
    @Test
    public void testRefresh() {
        String text = "Ha Ha Ha!";
        male.setText(text);
        repository.refresh(male);
        assertEquals("男", male.getText());
    }

    /**
     * Test of clear method
     */
    @Test
    public void testClear() {
        repository.clear();
    }
    
}
