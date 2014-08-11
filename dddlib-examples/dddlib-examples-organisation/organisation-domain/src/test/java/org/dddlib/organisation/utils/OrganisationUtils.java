package org.dddlib.organisation.utils;

import java.util.Date;

import org.dddlib.organisation.domain.*;

public class OrganisationUtils {
	
	public Company createCompany(String name, Date date) {
		Company result = new Company(name);
		result.setCreateDate(date);
		result.save();
		return result;
	}
	
	public Company createCompany(String name, Organization parent, Date date) {
		Company result = createCompany(name, date);
		new OrgLineMgmt(parent, result, date).save();
		return result;
	}
	
	public Department createDepartment(String name, Date date) {
		Department result = new Department(name);
		result.setCreateDate(date);
		result.save();
		return result;
	}
	
	public Department createDepartment(String name, Organization parent, Date date) {
		Department result = createDepartment(name, date);
		new OrgLineMgmt(parent, result, date).save();
		return result;
	}
	
	public Post createPost(String name, Organization organization, Date date) {
		Post result = new Post(name);
		result.setCreateDate(date);
		result.setOrganization(organization);
		result.save();
		return result;
	}
	
	public Employee createEmployee(String name, Date date) {
		Employee result = new Employee(name);
		result.setCreateDate(date);
		result.save();
		return result;
	}
	
	public Employee createEmployee(String name, Post post, Date date) {
		Employee result = createEmployee(name, date);
		new PostHolding(post, result, date).save();
		return result;
	}
}
