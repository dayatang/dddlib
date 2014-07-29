package org.dayatang.cache.redis;

import java.io.Serializable;

/**
 * Created by lingen on 14-7-15.
 */
public class Person implements Serializable {

    private String name;

    private int age;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
