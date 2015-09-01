package org.dddlib.organisation.security.domain;

import org.dayatang.security.domain.User;
import org.dddlib.organisation.domain.Employee;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Created by yyang on 15/7/10.
 */
@Entity
@DiscriminatorValue("EMP_USER")
public class EmployeeUser extends User {

    @NotNull
    @ManyToOne
    private Employee employee;

    protected EmployeeUser() {
    }

    public EmployeeUser(String name, String password, Employee employee) {
        super(name, password);
        this.employee = employee;
    }

    public EmployeeUser(Employee employee, String password) {
        super(employee.getSn(), password);
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeUser)) {
            return false;
        }
        EmployeeUser that = (EmployeeUser) o;
        return Objects.equals(getEmployee(), that.getEmployee());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmployee());
    }
}
