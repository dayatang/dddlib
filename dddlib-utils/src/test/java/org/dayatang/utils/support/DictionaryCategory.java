package org.dayatang.utils.support;

import java.util.HashSet;
import java.util.Set;

public class DictionaryCategory {

    private static final long serialVersionUID = -2820088186350505379L;

    private Long id;

    private String name;

    private int sortOrder;

    private Set<Dictionary> dictionaries = new HashSet<Dictionary>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Set<Dictionary> getDictionaries() {
        return dictionaries;
    }

    public void setDictionaries(Set<Dictionary> dictionaries) {
        this.dictionaries = dictionaries;
    }

    @Override
    public String toString() {
        return name;
    }

}
