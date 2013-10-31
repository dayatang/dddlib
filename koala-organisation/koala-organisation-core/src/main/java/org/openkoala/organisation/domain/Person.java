package org.openkoala.organisation.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openkoala.organisation.IdNumberIsExistException;

import com.dayatang.domain.AbstractEntity;
import com.dayatang.domain.QuerySettings;

/**
 * 人
 * @author xmfang
 *
 */
@Entity
@Table(name = "persons")
public class Person extends AbstractEntity {

	private static final long serialVersionUID = 4180083929142881138L;

	private String name;

	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	@Column(name = "id_number", unique = true)
	private String idNumber;
	
	@Column(name = "mobile_phone")
	private String mobilePhone;
	
	@Column(name = "family_phone")
	private String familyPhone;
	
	private String email;
	
	public Person() {
	}

	public Person(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getFamilyPhone() {
		return familyPhone;
	}

	public void setFamilyPhone(String familyPhone) {
		this.familyPhone = familyPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public void save() {
		if (getId() == null) {
			checkIdNumberExist();
		} else {
			Person person = get(Person.class, getId());
			if (!person.getIdNumber().equals(idNumber)) {
				checkIdNumberExist();
			}
		}
		super.save();
	}
	
	public void checkIdNumberExist() {
		if (StringUtils.isBlank(idNumber)) {
			return;
		}
		if (isExistIdNumber(idNumber, new Date())) {
			throw new IdNumberIsExistException();
		}
	}
	
	public static boolean isExistIdNumber(String sn, Date date) {
		List<Person> parties = getRepository().find(QuerySettings.create(Person.class)
				.eq("idNumber", sn));
		return parties.isEmpty() ? false : true;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(idNumber).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Person)) {
			return false;
		}
		Person that = (Person) other;
		return new EqualsBuilder().append(this.idNumber, that.idNumber)
				.isEquals();
	}

	@Override
	public String toString() {
		return name.toString();
	}

}
