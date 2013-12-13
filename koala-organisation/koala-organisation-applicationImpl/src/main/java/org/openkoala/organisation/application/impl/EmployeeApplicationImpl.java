package org.openkoala.organisation.application.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.apache.commons.lang3.StringUtils;
import org.openkoala.organisation.EmployeeMustHaveAtLeastOnePostException;
import org.openkoala.organisation.application.EmployeeApplication;
import org.openkoala.organisation.application.dto.EmployeeDTO;
import org.openkoala.organisation.application.dto.ResponsiblePostDTO;
import org.openkoala.organisation.domain.Employee;
import org.openkoala.organisation.domain.EmployeePostHolding;
import org.openkoala.organisation.domain.Organization;
import org.openkoala.organisation.domain.Post;
import org.springframework.transaction.annotation.Transactional;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

/**
 * 员工应用实现层类
 *
 */
@Named
@Transactional
@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
@Stateless(name = "EmployeeApplication")
@Remote
public class EmployeeApplicationImpl implements EmployeeApplication {


	private QueryChannelService queryChannel;
	
	private QueryChannelService getQueryChannelService(){
		if(queryChannel ==null){
			queryChannel = InstanceFactory.getInstance(QueryChannelService.class,"queryChannel_org");
		}
		return queryChannel;
	}

	@Override
	public Organization getOrganizationOfEmployee(Employee employee, Date date) {
		return employee.getOrganization(date);
	}

	@Override
	public Page<EmployeeDTO> pagingQueryEmployees(EmployeeDTO example, int currentPage, int pagesize) {
		List<Object> conditionVals = new ArrayList<Object>();

		StringBuilder jpql = new StringBuilder("select _employee from Employee _employee where _employee.createDate <= ?1 and _employee.terminateDate > ?2");
		Date now = new Date();
		conditionVals.add(now);
		conditionVals.add(now);

		return queryResult(example, jpql, "_employee", conditionVals, currentPage, pagesize);
	}

	@Override
	public void createEmployeeWithPost(Employee employee, Post post) {
		employee.saveWithPost(post);
	}

	@Override
	public Page<EmployeeDTO> pagingQueryEmployeesByOrganization(EmployeeDTO example, Organization organization, int currentPage, int pagesize) {
		List<Object> conditionVals = new ArrayList<Object>();

		StringBuilder jpql = new StringBuilder("select distinct _holding.responsible from EmployeePostHolding _holding "
				+ "where _holding.commissioner in"
				+ " (select p from Post p where p.organization = ?1 and p.createDate <= ?2 and p.terminateDate > ?3)"
				+ " and _holding.fromDate <= ?4 and _holding.toDate > ?5");
		Date now = new Date();
		conditionVals.add(organization);
		conditionVals.add(now);
		conditionVals.add(now);
		conditionVals.add(now);
		conditionVals.add(now);

		return queryResult(example, jpql, "_holding.responsible", conditionVals, currentPage, pagesize);
	}

	@Override
	public Page<EmployeeDTO> pagingQueryEmployeesByOrganizationAndChildren(EmployeeDTO example, Organization organization, int currentPage, int pagesize) {
		List<Object> conditionVals = new ArrayList<Object>();

		StringBuilder jpql = new StringBuilder("select distinct _holding.responsible from EmployeePostHolding _holding "
				+ "where _holding.commissioner in"
				+ " (select p from Post p where p.organization in ?1 and p.createDate <= ?2 and p.terminateDate > ?3)"
				+ " and _holding.fromDate <= ?4 and _holding.toDate > ?5");
		Date now = new Date();
		List<Organization> organizations = new ArrayList<Organization>();
		organizations.add(organization);
		organizations.addAll(organization.getAllChildren(now));
		
		conditionVals.add(organizations);
		conditionVals.add(now);
		conditionVals.add(now);
		conditionVals.add(now);
		conditionVals.add(now);

		return queryResult(example, jpql, "_holding.responsible", conditionVals, currentPage, pagesize);
	}

	@Override
	public Page<EmployeeDTO> pagingQueryEmployeesWhoNoPost(EmployeeDTO example, int currentPage, int pagesize) {
		List<Object> conditionVals = new ArrayList<Object>();

		StringBuilder jpql = new StringBuilder("select _employee from Employee _employee"
				+ " where _employee.createDate <= ?1 and _employee.terminateDate > ?2");
		Date now = new Date();
		conditionVals.add(now);
		conditionVals.add(now);

		jpql.append(" and _employee Not In"
				+ " (select _holding.responsible from EmployeePostHolding _holding where _holding.fromDate <= ?3 and _holding.toDate > ?4)");
		conditionVals.add(now);
		conditionVals.add(now);
		
		return queryResult(example, jpql, "_employee", conditionVals, currentPage, pagesize);
	}

	private Page<EmployeeDTO> queryResult(EmployeeDTO example, StringBuilder jpql, String conditionPrefix, List<Object> conditionVals,
			int currentPage, int pagesize) {
		assembleJpqlAndConditionValues(example, jpql, conditionPrefix, conditionVals);
		Page<Employee> employeePage = getQueryChannelService().queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), currentPage, pagesize);
		
		return new Page<EmployeeDTO>(Page.getStartOfPage(currentPage, pagesize), 
				employeePage.getTotalCount(), pagesize, transformToDtos(employeePage.getResult()));
	}
	
	private void assembleJpqlAndConditionValues(EmployeeDTO example, StringBuilder jpql, String conditionPrefix, List<Object> conditionVals) {
		String andCondition = " and " + conditionPrefix;
		int initialConditionIndex = conditionVals.size();
		if (!StringUtils.isBlank(example.getName())) {
			jpql.append(andCondition);
			jpql.append(".name like ?" + ++initialConditionIndex);
			conditionVals.add(MessageFormat.format("%{0}%", example.getName()));
		}
		if (!StringUtils.isBlank(example.getSn())) {
			jpql.append(andCondition);
			jpql.append(".sn like ?" + ++initialConditionIndex);
			conditionVals.add(MessageFormat.format("%{0}%", example.getSn()));
		}
		if (!StringUtils.isBlank(example.getIdNumber())) {
			jpql.append(andCondition);
			jpql.append(".person.idNumber like ?" + ++initialConditionIndex);
			conditionVals.add(MessageFormat.format("%{0}%", example.getSn()));
		}
		if (!StringUtils.isBlank(example.getEmail())) {
			jpql.append(andCondition);
			jpql.append(".person.email like ?" + ++initialConditionIndex);
			conditionVals.add(MessageFormat.format("%{0}%", example.getEmail()));
		}
		if (!StringUtils.isBlank(example.getMobilePhone())) {
			jpql.append(andCondition);
			jpql.append(".person.mobilePhone like ?" + ++initialConditionIndex);
			conditionVals.add(MessageFormat.format("%{0}%", example.getMobilePhone()));
		}
		if (!StringUtils.isBlank(example.getFamilyPhone())) {
			jpql.append(andCondition);
			jpql.append(".person.familyPhone like ?" + ++initialConditionIndex);
			conditionVals.add(MessageFormat.format("%{0}%", example.getFamilyPhone()));
		}
	}
	
	private List<EmployeeDTO> transformToDtos(List<Employee> employees) {
		List<EmployeeDTO> results = new ArrayList<EmployeeDTO>();
		for (Employee employee : employees) {
			if (results.contains(employee)) {
				continue;
			}
			results.add(EmployeeDTO.generateDtoBy(employee));
		}
		return results;
	}
	
	@Override
	public void transformPost(Employee employee, Set<ResponsiblePostDTO> dots) {
		if (dots.isEmpty()) {
			throw new EmployeeMustHaveAtLeastOnePostException();
		}
		
		List<EmployeePostHolding> existHoldings = EmployeePostHolding.getByEmployee(employee, new Date());
		Set<Post> postsForOutgoing = employee.getPosts(new Date());
		Map<Post, Boolean> postsForAssign = new HashMap<Post, Boolean>();
		
		boolean existAccountsability = false;
		for (ResponsiblePostDTO dto : dots) {
			Post post = Post.get(Post.class, dto.getPostId());
			for (EmployeePostHolding ejHolding : existHoldings) {
				if (ejHolding.getCommissioner().equals(post)) {
					postsForOutgoing.remove(post);
					ejHolding.setPrincipal(dto.isPrincipal());
					ejHolding.save();
					existAccountsability = true;
					break;
				}
			}
			if (existAccountsability) {
				existAccountsability = false;
				continue;
			}
			postsForAssign.put(post, dto.isPrincipal());
		}
		
		employee.outgoingPosts(postsForOutgoing);
		
		for (Post post : postsForAssign.keySet()) {
			employee.assignPost(post, postsForAssign.get(post));
		}
	}

	@Override
	public List<ResponsiblePostDTO> getPostsByEmployee(Employee employee) {
		List<ResponsiblePostDTO> results = new ArrayList<ResponsiblePostDTO>();
		for (EmployeePostHolding holding : EmployeePostHolding.getByEmployee(employee, new Date())) {
			results.add(ResponsiblePostDTO.generateByPost(holding.getCommissioner(), holding.isPrincipal()));
		}
		return results;
	}
}
