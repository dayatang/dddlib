package com.dayatang.commons.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.dayatang.domain.AbstractEntity;
import com.dayatang.domain.QuerySettings;

@Entity
@Table(name = "categories")
public class DictionaryCategory extends AbstractEntity {

	private static final long serialVersionUID = -2820088186350505379L;

	public static final String GENDER = "gender";
	
	public static final String NATIONALITY = "nationality";
	
	public static final String EDUCATION = "education";

	public static final String DEGREE = "degree";
	
	public static final String MARITAL_STATUS = "maritalStatus";
	
	public static final String HEALTH_STATUS = "healthStatus";
	
	public static final String POLITICAL_AFFILIATION = "politicalAffiliation";
	
	public static final String EMPLOYMENT_STATUS = "employmentStatus";

	public static final String JOB_SERIES = "jobSeries";

	public static final String JOB_GRADE = "jobGrade";
	
	public static final String JOB_CLASS = "jobClass";

	private String name;
	
	@Column(name = "sort_order")
	private int sortOrder;

	@OneToMany(mappedBy = "category")
	private Set<Dictionary> dictionaries = new HashSet<Dictionary>();
	
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

	public static DictionaryCategory getByName(String name) {
		List<DictionaryCategory> results = getRepository().find(QuerySettings.create(DictionaryCategory.class).eq("name", name));
		return results.isEmpty() ? null : results.get(0);
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof DictionaryCategory)) {
			return false;
		}
		DictionaryCategory castOther = (DictionaryCategory) other;
		return new EqualsBuilder().append(name, castOther.name).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(name).toHashCode();
	}

	@Override
	public String toString() {
		return name;
	}
	
}
