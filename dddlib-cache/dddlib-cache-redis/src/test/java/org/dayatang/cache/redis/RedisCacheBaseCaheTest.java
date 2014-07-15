package org.dayatang.cache.redis;

import org.dayatang.cache.Cache;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by lingen on 14-7-15.
 */
public class RedisCacheBaseCaheTest {

    private Cache cache = new RedisCacheBaseCahe("127.0.0.1",6379);

    @Test
    public void test(){
        cache.put("abc", "abc");
        assertTrue(cache.get("abc").equals("abc"));
    }

    @Test
    public void test2(){
        Person person = new Person();
        person.setName("lingen");
        person.setAge(12);

        cache.put("A",person);
        Person person1 = (Person) cache.get("A");
        assertTrue(person1.getName().equals(person.getName()));
        assertTrue(person1.getAge()==person.getAge());
    }

    @Test
    public void testRemoe(){
        String key ="ABC";
        cache.put(key,"AAA");
        assertTrue(cache.get(key).equals("AAA"));
        cache.remove(key);
        assertTrue(cache.containsKey(key)==false);
    }

    @Test
    public void testExpired() throws InterruptedException {
        String key = "ABC";
        cache.put(key,"afdsa",5);
        assertTrue(cache.get(key)!=null);
        Thread.sleep(6*1000);
        assertTrue(cache.get(key)==null);

    }

}
