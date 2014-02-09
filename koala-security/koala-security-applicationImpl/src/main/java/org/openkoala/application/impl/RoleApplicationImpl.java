package org.openkoala.application.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.apache.commons.lang3.StringUtils;
import org.openkoala.exception.extend.ApplicationException;
import org.openkoala.auth.application.RoleApplication;
import org.openkoala.auth.application.vo.QueryConditionVO;
import org.openkoala.auth.application.vo.ResourceVO;
import org.openkoala.auth.application.vo.RoleVO;
import org.openkoala.auth.application.vo.UserVO;
import org.openkoala.koala.auth.core.domain.IdentityResourceAuthorization;
import org.openkoala.koala.auth.core.domain.Resource;
import org.openkoala.koala.auth.core.domain.Role;
import org.openkoala.koala.auth.core.domain.RoleUserAuthorization;
import org.openkoala.koala.auth.core.domain.User;
import org.springframework.transaction.annotation.Transactional;

import com.dayatang.dsrouter.context.memory.ContextHolder;
import com.dayatang.querychannel.support.Page;

@Remote
@Named("roleApplication")
@Stateless(name = "RoleApplication")
@Transactional(value="transactionManager_security")
@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
public class RoleApplicationImpl extends BaseImpl implements RoleApplication {

	public RoleVO getRole(Long roleId) {
		Role role = Role.get(Role.class, roleId);
		RoleVO roleVO = new RoleVO();
		roleVO.domain2Vo(role);
		return roleVO;
	}

	public RoleVO saveRole(RoleVO roleVO) {
		String cx = ContextHolder.getContextType();
		ContextHolder.setContextType("security");
		Role role = new Role();
		roleVO.vo2Domain(role);
		
		if (isRoleExist(role)) {
			throw new ApplicationException("role.exist");
		}
		
		role.save();
		roleVO.setId(role.getId());
		ContextHolder.setContextType(cx);
		return roleVO;
	}

	private boolean isRoleExist(Role role) {
		if (role.isRoleExist()) {
			return true;
		}
		return false;
	}

	public void updateRole(RoleVO roleVO) {
		Role role = Role.load(Role.class, roleVO.getId());
		if (!roleVO.getName().equals(role.getName())) {
			Role target = new Role();
			target.setName(roleVO.getName());
			isRoleExist(target);
		}
		role.setName(roleVO.getName());
		role.setRoleDesc(roleVO.getRoleDesc());
	}

	public void removeRole(Long roleId) {
		removeRoles(new Long[] { roleId });
	}
	
	public void removeRoles(Long[] roleIds) {
		for (long roleId : roleIds) {
			Role role = Role.load(Role.class, roleId);
			removeUserAuthorizations(role);
			removeResourceAuthorizations(role);
			role.setAbolishDate(new Date());
		}
	}

	/**
	 * 删除与资源的授权信息
	 * @param role
	 */
	private void removeResourceAuthorizations(Role role) {
		for (IdentityResourceAuthorization each : role.getIdentityResourceAuthorizations()) {
			each.setAbolishDate(new Date());
		}
	}

	/**
	 * 删除与用户的关系
	 * @param role
	 */
	private void removeUserAuthorizations(Role role) {
		for (RoleUserAuthorization authorization : role.getRoleUserAuthorizations()) {
			authorization.setAbolishDate(new Date());
		}
	}

	public List<RoleVO> findAllRole() {
		List<RoleVO> results = new ArrayList<RoleVO>();
		List<Role> roles = Role.findAll(Role.class);
		for (Role each : roles) {
			RoleVO roleVO = new RoleVO();
			roleVO.domain2Vo(each);
			results.add(roleVO);
		}
		return results;
	}

	public List<ResourceVO> findResourceByRole(RoleVO roleVO) {
		List<ResourceVO> results = new ArrayList<ResourceVO>();
		List<Resource> resources = Resource.findResourceByRole(roleVO.getId());
		for (Resource each : resources) {
			results.add(ResourceApplicationImpl.domainObject2Vo(each));
		}
		return results;
	}

	public List<UserVO> findUserByRole(RoleVO roleVO) {
		return queryChannel().queryResult(
				"select new org.openkoala.auth.application.vo.UserVO(user.id,user.name,"
				+ "user.sortOrder,user.userAccount,user.userDesc,user.isValid,user.email,user.lastLoginTime) " 
				+ "from RoleUserAuthorization rua join rua.user user join rua.role role where role.id=? and rua.abolishDate>?",
				new Object[] { roleVO.getId(), new Date() });
	}
	
	public Page<UserVO> pageQueryUserByRole(RoleVO roleVO, int currentPage, int pageSize) {
		StringBuilder jpql = new StringBuilder("select new org.openkoala.auth.application.vo.UserVO"
				+ "(user.id,user.name,user.sortOrder,user.userAccount,user.userDesc,user.isValid,"
				+ "user.email,user.lastLoginTime) from RoleUserAuthorization rua join rua.user "
				+ "user join rua.role role where role.id=? and rua.abolishDate>? ");
		
		List<Object> conditions = new ArrayList<Object>();
		conditions.add(roleVO.getId());
		conditions.add(new Date());
		
		if (!StringUtils.isEmpty(roleVO.getUseraccount())) {
			jpql.append(" and user.userAccount like ? ");
			conditions.add("%" + roleVO.getUseraccount() + "%");
		}
		
		if (!StringUtils.isEmpty(roleVO.getName())) {
			jpql.append(" and user.name like ? ");
			conditions.add("%" + roleVO.getName() + "%");
		}
		
		Page<UserVO> pages = queryChannel().queryPagedResultByPageNo(
				jpql.toString(), conditions.toArray(),
				currentPage, pageSize);
		
		return new Page<UserVO>(pages.getCurrentPageNo(), pages.getTotalCount(), pages.getPageSize(), pages.getResult());
	}

	public List<ResourceVO> findMenuByRole(RoleVO roleVO) {
		List<ResourceVO> result = new ArrayList<ResourceVO>();
		List<Resource> queryResult = Resource.findResourceByRole(roleVO.getId());
		for (Resource res : queryResult) {
			ResourceVO resourceVO = new ResourceVO();
			resourceVO.domain2Vo(res);
			result.add(resourceVO);
		}
		return result;
	}

	public List<ResourceVO> findAllResourceByRole(RoleVO roleVO) {
		List<ResourceVO> result = new ArrayList<ResourceVO>();
		List<Resource> queryResult = Resource.findResourceByRole(roleVO.getId());
		for (Resource res : queryResult) {
			ResourceVO resourceVO = new ResourceVO();
			resourceVO.domain2Vo(res);
			result.add(resourceVO);
		}
		return result;
	}

	public Page<RoleVO> pageQueryRole(int currentPage, int pageSize) {
		List<RoleVO> results = new ArrayList<RoleVO>();
		Page<Role> pages = queryChannel().queryPagedResultByPageNo("select m from Role m where m.abolishDate>?", //
				new Object[] { new Date() }, //
				currentPage, pageSize);
		for (Role each : pages.getResult()) {
			RoleVO roleVO = new RoleVO();
			roleVO.domain2Vo(each);
			results.add(roleVO);
		}
		return new Page<RoleVO>(pages.getCurrentPageNo(), pages.getTotalCount(), pages.getPageSize(), results);
	}
	
	public Page<RoleVO> pageQueryRoleByUseraccount(int currentPage, int pageSize, String useraccount) {
		List<RoleVO> results = new ArrayList<RoleVO>();
		Page<Role> pages = queryChannel().queryPagedResultByPageNo(
				"select role from RoleUserAuthorization rau " +
				"join rau.role role join rau.user user where user.userAccount=? " +
				"and rau.abolishDate>?", 
				new Object[] { useraccount, new Date() }, 
				currentPage, pageSize);
		for (Role each : pages.getResult()) {
			RoleVO roleVO = new RoleVO();
			roleVO.domain2Vo(each);
			results.add(roleVO);
		}
		return new Page<RoleVO>(pages.getCurrentPageNo(), pages.getTotalCount(), pages.getPageSize(), results);
	}

	public Page<RoleVO> pageQueryByRoleCustom(int currentPage, int pageSize, QueryConditionVO query) {
		List<RoleVO> result = new ArrayList<RoleVO>();
		Page<Role> pages = queryChannel().queryPagedResultByPageNo(genQueryCondition(query), new Object[] { new Date() },
				currentPage, pageSize);
		for (Role role : pages.getResult()) {
			RoleVO roleVO = new RoleVO();
			roleVO.domain2Vo(role);
			result.add(roleVO);
		}
		return new Page<RoleVO>(pages.getCurrentPageNo(), pages.getTotalCount(), pages.getPageSize(), result);
	}
	
	public Page<RoleVO> pageQueryNotAssignRoleByUseraccount(int currentPage, int pageSize, 
			String useraccount, RoleVO roleVO) {
		
		List<Object> conditions = new ArrayList<Object>();
		conditions.add(useraccount);
		conditions.add(new Date());
		conditions.add(new Date());
		
		StringBuilder jpql = new StringBuilder("select new org.openkoala.auth.application.vo.RoleVO" //
				+ "(role.id, role.name, role.roleDesc) from Role role " //
				+ "where role not in (select role from RoleUserAuthorization rua " //
				+ "join rua.role role join rua.user user where user.userAccount=? " //
				+ "and rua.abolishDate>?) and role.abolishDate>?");
		
		if (!StringUtils.isEmpty(roleVO.getName())) {
			jpql.append(" and role.name like ? ");
			conditions.add("%" + roleVO.getName() + "%");
		}
		
		Page<RoleVO> pages = queryChannel().queryPagedResultByPageNo( //
				jpql.toString(), //
				conditions.toArray(), //
				currentPage, pageSize);
		
		return new Page<RoleVO>(pages.getCurrentPageNo(), pages.getTotalCount(), //
				pages.getPageSize(), pages.getResult());
	}

	public void assignUser(RoleVO roleVO, UserVO userVO) {
		User us = new User();
		us.setId(Long.valueOf(userVO.getId()));
		Role role = new Role();
		role.setId(Long.valueOf(roleVO.getId()));
		role.assignUser(us);
	}

	/**
	 * 为角色分配菜单资源
	 */
	public void assignMenuResource(RoleVO roleVO, ResourceVO menuResVO) {
		Resource res = new Resource();
		res.setId(Long.valueOf(menuResVO.getId()));
		Role role = new Role();
		role.setId(Long.valueOf(roleVO.getId()));
		role.assignResource(res);
	}

	public void assignMenuResource(RoleVO roleVO, List<ResourceVO> menuResVOs) {
		for (ResourceVO mv : menuResVOs) {
			assignMenuResource(roleVO, mv);
		}
	}

	public void abolishResource(RoleVO roleVO, List<ResourceVO> resources) {
		for (ResourceVO resource : resources) {
			Resource res = new Resource();
			res.setId(Long.valueOf(resource.getId()));
			Role role = new Role();
			role.setId(Long.valueOf(roleVO.getId()));
			role.abolishResource(res);
		}
	}

	public void abolishMenu(RoleVO roleVO, List<ResourceVO> menus) {
		for (ResourceVO mv : menus) {
			Resource menu = new Resource();
			menu.setId(Long.valueOf(mv.getId()));
			Role role = new Role();
			role.setId(Long.valueOf(roleVO.getId()));
			role.abolishResource(menu);
		}
	}

	public void abolishUser(RoleVO roleVO, List<UserVO> users) {
		for (UserVO userVO : users) {
			User user = new User();
			user.setId(Long.valueOf(userVO.getId()));
			Role role = new Role();
			role.setId(Long.valueOf(roleVO.getId()));
			role.abolishUser(user);
		}
	}

	/**
	 * 为角色分配URL资源
	 */
	public void assignResource(RoleVO roleVO, ResourceVO urlResVO) {
		Resource res = new Resource();
		res.setId(Long.valueOf(urlResVO.getId()));
		Role role = new Role();
		role.setId(Long.valueOf(roleVO.getId()));
		role.assignResource(res);
	}

	public void assignURLResource(RoleVO roleVO, List<ResourceVO> urlResVOs) {
		for (ResourceVO ur : urlResVOs) {
			assignResource(roleVO, ur);
		}
	}

	public List<RoleVO> findRoleByUserAccount(String userAccount) {
		List<RoleVO> results = new ArrayList<RoleVO>();
		List<Role> roles = Role.findRoleByUserAccount(userAccount);
		for (Role each : roles) {
			RoleVO roleVO = new RoleVO();
			roleVO.domain2Vo(each);
			results.add(roleVO);
		}
		return results;
	}

	public Page<UserVO> pageQueryNotAssignUserByRole(int currentPage, int pageSize, UserVO userVO, RoleVO roleVO) {
		List<UserVO> result = new ArrayList<UserVO>();
		List<Object> conditions = new ArrayList<Object>();
		conditions.add(roleVO.getId());
		conditions.add(new Date());
		conditions.add(new Date());
		
		StringBuilder jpql = new StringBuilder("select user from User user where user not in("
				+ "select user from RoleUserAuthorization auth" //
				+ " join auth.user user join auth.role role where role.id=? and auth.abolishDate>?)" //
				+ " and user.abolishDate>?");
		
		if (userVO != null && !StringUtils.isEmpty(userVO.getName())) {
			jpql.append(" and user.name like ? ");
			conditions.add("%" + userVO.getName() + "%");
		}
		
		if (userVO != null && !StringUtils.isEmpty(userVO.getUserAccount())) {
			jpql.append(" and user.userAccount like ? ");
			conditions.add("%" + userVO.getUserAccount() + "%");
		}
		
		Page<User> pages = queryChannel().queryPagedResultByPageNo( //
				jpql.toString(), //
				conditions.toArray(), 
				currentPage, pageSize);
		
		for (User user : pages.getResult()) {
			UserVO vo = new UserVO();
			vo.domain2Vo(user);
			result.add(vo);
		}
		
		return new Page<UserVO>(pages.getCurrentPageNo(), pages.getTotalCount(), 
				pages.getPageSize(), result);
	}
}
