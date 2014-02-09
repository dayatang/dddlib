package org.openkoala.koala.auth.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.openkoala.koala.auth.AuthDataService;
import org.openkoala.koala.auth.UserDetails;
import org.openkoala.koala.auth.core.domain.IdentityResourceAuthorization;
import org.openkoala.koala.auth.core.domain.Role;
import org.openkoala.koala.auth.core.domain.User;
import org.openkoala.koala.auth.vo.DefaultUserDetailsImpl;

/**
 * 鉴权实现
 * @author zhuyuanbiao
 * @date Dec 24, 2013 2:22:18 PM
 *
 */
@Remote
@Stateless
@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
public class AuthDataServiceImpl implements AuthDataService {
	
	@Override
	public List<String> getAttributes(String res) {
		return retrieveResourceAndRoles().get(res);
	}
	
	private Map<String, List<String>> retrieveResourceAndRoles() {
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		for (IdentityResourceAuthorization each : IdentityResourceAuthorization.findAllReourcesAndRoles()) {
			Set<String> roles = new HashSet<String>();
			roles.add(String.valueOf(each.getIdentity().getId()));
			if (result.containsKey(each.getResource().getIdentifier())) {
				result.get(each.getResource().getIdentifier()).addAll(roles);
			} else {
				result.put(each.getResource().getIdentifier(), new ArrayList<String>(roles));
			}
		}
		return result;
	}

	@Override
	public UserDetails loadUserByUseraccount(String useraccount) {
		
		User user = User.findByUserAccount(useraccount);
		
		if (user == null) {
			user = User.findByEmail(useraccount);
		}
		
		if (user == null) {
			throw new UseraccountNotExistException(String.format("%s is not existed.", useraccount)); 
		}
		
		return getUserDetails(user.getUserAccount(), user.getUserPassword(), user.getUserDesc(),
				user.getEmail(), user.getName(), user.getName(), user.isValid(), user.isSuper());
	}
	
	@Override
	public List<String> getUserRoles(String accountName) {
		if (isSuperUser(accountName)) {
			return findAllRoles();
		}
		return findRoleByUseraccount(accountName);
	}

	private boolean isSuperUser(String accountName) {
		return User.findByUserAccount(accountName).isSuper();
	}

	private List<String> findRoleByUseraccount(String accountName) {
		List<String> results = new ArrayList<String>(); 
		for (Role each : Role.findRoleByUserAccount(accountName)) {
			results.add(String.valueOf(each.getId()));
		}
		return results;
	}

	private List<String> findAllRoles() {
		List<String> roles = new ArrayList<String>();
		for (Role each : Role.findAllRoles()) {
			roles.add(String.valueOf(each.getId()));
		}
		return roles;
	}

	@Override
	public Map<String, List<String>> getAllReourceAndRoles() {
		return retrieveResourceAndRoles();
	}
	
	private DefaultUserDetailsImpl getUserDetails(String useraccount, String password, String desc, 
			String email, String name, String realName, boolean isValid, boolean isSuper) {
		DefaultUserDetailsImpl userDetails = new DefaultUserDetailsImpl();
		userDetails.setUseraccount(useraccount);
		userDetails.setPassword(password);
		userDetails.setDescription(desc);
		userDetails.setEmail(email);
		userDetails.setRealName(name);
		userDetails.setSuper(isSuper);
		userDetails.setAuthorities(getUserRoles(useraccount));
		userDetails.setEnabled(isValid);
		userDetails.setRealName(realName);
		return userDetails;
	}

}
