package org.dddlib.organisation.domain;

import org.dayatang.domain.ValueObject;

import javax.persistence.Embeddable;

/**
 * Created by yyang on 14-3-5.
 */
@Embeddable
public class Address implements ValueObject {
    private String province;
    private String city;
    private String detail;


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
