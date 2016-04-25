/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dddlib.organisation.domain;

import org.dayatang.domain.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 *
 * @author yyang
 */
@Entity
public class EmployeeArchieve extends AbstractEntity {
    
    
    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String[] businessKeys() {
        return new String[] {"employee"};
    }
    
}
