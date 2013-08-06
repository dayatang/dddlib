package org.openkoala.koala.auth.core.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.dayatang.utils.DateUtils;

/**
 * 角色实体类
 * 
 * @author lingen
 * 
 */
@Entity
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
public class Role extends Identity {

	private static final long serialVersionUID = -8345993710464457036L;

	@Column(name = "ROLE_DESC")
	private String roleDesc;
	//
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
	private List<RoleUserAuthorization> roleUsers;

	public List<RoleUserAuthorization> getRoleUsers() {
		return roleUsers;
	}

	public void setRoleUsers(List<RoleUserAuthorization> roleUsers) {
		this.roleUsers = roleUsers;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean equals(Object other) {
		// TODO Auto-generated method stub
		return false;
	}

	public void assignUser(User user) {
		RoleUserAuthorization roleUserAssignment = new RoleUserAuthorization();
		roleUserAssignment.setCreateDate(new Date());
		roleUserAssignment.setAbolishDate(DateUtils.MAX_DATE);
		roleUserAssignment.setRole(this);
		roleUserAssignment.setUser(user);
		roleUserAssignment.save();
	}

	public void assignResource(Resource res) {
		IdentityResourceAuthorization idres = new IdentityResourceAuthorization();
		idres.setIdentity(this);
		idres.setResource(res);
		idres.setAbolishDate(DateUtils.MAX_DATE);
		idres.setCreateDate(new Date());
		idres.setScheduledAbolishDate(new Date());
		idres.save();
	}

	public void abolishResource(Resource res) {
		List<IdentityResourceAuthorization> authorizations = IdentityResourceAuthorization.getRepository().find( //
				"select m from IdentityResourceAuthorization m where m.identity.id=? and " //
				+ "m.resource.id=? and m.abolishDate>?", new Object[] { this.getId(), res.getId(), new Date() }, //
				IdentityResourceAuthorization.class);
		for (IdentityResourceAuthorization authorization : authorizations) {
			authorization.setAbolishDate(new Date());
		}
	}

	public void abolishUser(User user) {
		List<RoleUserAuthorization> authorizations = RoleUserAuthorization.getRepository().find( //
				"select m from RoleUserAuthorization m where m.user.id=? and m.role.id=? and m.abolishDate>?", //
				new Object[] { user.getId(), this.getId(), new Date() }, //
				RoleUserAuthorization.class);
		for (RoleUserAuthorization authorization : authorizations) {
			authorization.setAbolishDate(new Date());
		}
	}
	
	public static Role findRoleByName(String roleName){
		Object[] params = new Object[] { roleName, new Date() };
		return getRepository().findByNamedQuery("findRoleByName", params, Role.class).get(0);
	}

	public static List<Role> findRoleByUserAccount(String userAccount) {
		Object[] params = new Object[] { userAccount, new Date() };
		return findByNamedQuery("findRoleByUserAccount", params, Role.class);
	}

	/**
	 * 角色是否存在
	 * 
	 * @return
	 */
	public boolean isRoleExist() {
		return !findByNamedQuery("isRoleExist", new Object[] { getName(), new Date() }, Role.class).isEmpty();
	}
	
	public List<IdentityResourceAuthorization> getAuthorizations() {
		return IdentityResourceAuthorization.findAuthorizationByRole(getId());
	}
	
	public List<RoleUserAuthorization> getUsers() {
		return RoleUserAuthorization.findUserAuthorizationByRole(getId());
	}

}
