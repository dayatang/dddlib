package org.openkoala.organisation.application.impl.utils;

import java.util.Date;

import org.openkoala.organisation.domain.*;

public class OrganisationUtils {
	
	public Company createTopOrganization(String name, String sn, Date date) {
		Company result = new Company(name, sn);
		result.setCreateDate(date);
		result.createAsTopOrganization();
		return result;
	}
	
	public Company createCompany(String name, String sn, Date date) {
		Company result = new Company(name, sn);
		result.setCreateDate(date);
		result.save();
		return result;
	}
	
	public Company createCompany(String name, String sn, Organization parent, Date date) {
		Company result = createCompany(name, sn, date);
		new OrganizationLineManagement(parent, result, date).save();
		return result;
	}
	
	public Department createDepartment(String name, String sn, Date date) {
		Department result = new Department(name, sn);
		result.setCreateDate(date);
		result.save();
		return result;
	}
	
	public Department createDepartment(String name, String sn, Organization parent, Date date) {
		Department result = createDepartment(name, sn, date);
		new OrganizationLineManagement(parent, result, date).save();
		return result;
	}
	
	public Job createJob(String name, String sn, Date date) {
		Job result = new Job(name, sn);
		result.setCreateDate(date);
		result.save();
		return result;
	}
	
	public Post createPost(String name, String sn, Job job, Organization organization, Date date) {
		Post result = new Post(name, sn, job, organization);
		result.setCreateDate(date);
		result.save();
		return result;
	}
	
	public Employee createEmployee(String name, String idNumber, String sn, Date date) {
		Person person = new Person(name);
		person.setIdNumber(idNumber);
		
		Employee result = new Employee(person, sn, date);
		result.setCreateDate(date);
		result.save();
		return result;
	}
	
	public Employee createEmployee(String name, String idNumber, String sn, Post post, Date date) {
		Employee result = createEmployee(name, idNumber, sn, date);
		new EmployeePostHolding(post, result, true, date).save();
		return result;
	}
}
