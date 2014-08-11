package org.dddlib.organisation.domain;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.*;

import org.dayatang.domain.AbstractEntity;

@Entity
@Table(name = "persons")
public class Person extends AbstractEntity {

    private static final long serialVersionUID = 4180083929142881138L;

    @Embedded
    private PersonName name;

    private String idNumber;

    @ElementCollection
    @CollectionTable(name = "person_emails", joinColumns = @JoinColumn(name = "person_id"))
    private Set<Email> emails = new HashSet<Email>();

    @ElementCollection
    @CollectionTable(name = "person_ims", joinColumns = @JoinColumn(name = "person_id"))
    @MapKeyColumn(name = "im_type")
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "im_address")
    private Map<ImType, String> ims = new HashMap<ImType, String>();

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "province", column = @Column(name = "home_province")),
        @AttributeOverride(name = "city", column = @Column(name = "home_city")),
        @AttributeOverride(name = "detail", column = @Column(name = "home_detail"))
    })
    private Address homeAddress;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "province", column = @Column(name = "mail_province")),
        @AttributeOverride(name = "city", column = @Column(name = "mail_city")),
        @AttributeOverride(name = "detail", column = @Column(name = "mail_detail"))
    })
    private Address mailAddress;

    public PersonName getName() {
        return name;
    }

    public void setName(PersonName name) {
        this.name = name;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Set<Email> getEmails() {
        return Collections.unmodifiableSet(emails);
    }

    public void setEmails(Set<Email> emails) {
        if (emails == null) {
            return;
        }
        this.emails = new HashSet<Email>(emails);
    }

    public void addEmail(Email email) {
        emails.add(email);
    }

    public void removeEmail(Email email) {
        emails.remove(email);
    }

    public Map<ImType, String> getIms() {
        return Collections.unmodifiableMap(ims);
    }

    public void setIms(Map<ImType, String> ims) {
        if (ims == null) {
            return;
        }
        this.ims = new EnumMap<ImType, String>(ims);
    }

    public void setIm(ImType imType, String imAddress) {
        ims.put(imType, imAddress);
    }

    public void removeIm(ImType imType) {
        ims.remove(imType);
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Address getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(Address mailAddress) {
        this.mailAddress = mailAddress;
    }

    @Override
    public String[] businessKeys() {
        return new String[]{"name", "idNumber"};
    }

    @Override
    public String toString() {
        return name.toString();
    }

}
