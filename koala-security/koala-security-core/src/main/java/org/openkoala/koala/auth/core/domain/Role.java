package org.openkoala.koala.auth.core.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.dayatang.utils.DateUtils;

/**
 * 角色实体类
 * 
 * @author lingen
 * 
 */
@Entity
public class Role extends Identity {

	private static final long serialVersionUID = -8345993710464457036L;

	@Column(name = "ROLE_DESC")
	private String roleDesc;

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}
	
	public Role() {
		
	}
	
	public Role(String name, String desc) {
		this.setAbolishDate(DateUtils.MAX_DATE);
		this.setCreateDate(new Date());
		this.setName(name);
		this.roleDesc = desc;
	}

	/**
	 * 为角色分配用户
	 * @param user
	 */
	public void assignUser(User user) {
		RoleUserAuthorization roleUserAssignment = new RoleUserAuthorization();
		roleUserAssignment.setCreateDate(new Date());
		roleUserAssignment.setAbolishDate(DateUtils.MAX_DATE);
		roleUserAssignment.setRole(this);
		roleUserAssignment.setUser(user);
		roleUserAssignment.save();
	}

	/**
	 * 为角色分配资源
	 * @param resource
	 */
	public void assignResource(Resource resource) {
		IdentityResourceAuthorization idres = new IdentityResourceAuthorization();
		idres.setIdentity(this);
		idres.setResource(resource);
		idres.setAbolishDate(DateUtils.MAX_DATE);
		idres.setCreateDate(new Date());
		idres.setScheduledAbolishDate(new Date());
		idres.save();
	}

	/**
	 * 废除角色所关联的资源
	 * @param resource
	 */
	public void abolishResource(Resource resource) {
		List<IdentityResourceAuthorization> authorizations = IdentityResourceAuthorization.getRepository().find( //
				"select m from IdentityResourceAuthorization m where m.identity.id=? and " //
				+ "m.resource.id=? and m.abolishDate>?", new Object[] { this.getId(), resource.getId(), new Date() }, //
				IdentityResourceAuthorization.class);
		for (IdentityResourceAuthorization authorization : authorizations) {
			authorization.setAbolishDate(new Date());
		}
	}

	/**
	 * 废除角色所关联的用户
	 * @param user
	 */
	public void abolishUser(User user) {
		List<RoleUserAuthorization> authorizations = RoleUserAuthorization.getRepository().find( //
				"select m from RoleUserAuthorization m where m.user.id=? and m.role.id=? and m.abolishDate>?", //
				new Object[] { user.getId(), this.getId(), new Date() }, //
				RoleUserAuthorization.class);
		for (RoleUserAuthorization authorization : authorizations) {
			authorization.setAbolishDate(new Date());
		}
	}
	
	/**
	 * 根据角色名称查找角色
	 * @param roleName
	 * @return
	 */
	public static Role findRoleByName(String roleName){
		return getRepository().findByNamedQuery("findRoleByName", new Object[] { roleName, new Date() }, Role.class).get(0);
	}

	/**
	 * 查找用户所拥有的角色
	 * @param userAccount
	 * @return
	 */
	public static List<Role> findRoleByUserAccount(String userAccount) {
		return findByNamedQuery("findRoleByUserAccount", new Object[] { userAccount, new Date() }, Role.class);
	}

	/**
	 * 角色是否存在
	 * 
	 * @return
	 */
	public boolean isRoleExist() {
		return !findByNamedQuery("isRoleExist", new Object[] { getName(), new Date() }, Role.class).isEmpty();
	}
	
	/**
	 * 获取角色的资源授权
	 */
	public List<IdentityResourceAuthorization> getIdentityResourceAuthorizations() {
		return IdentityResourceAuthorization.findAuthorizationByRole(getId());
	}
	
	/**
	 * 获取用户角色的授权
	 * @return
	 */
	public List<RoleUserAuthorization> getRoleUserAuthorizations() {
		return RoleUserAuthorization.findUserAuthorizationByRole(getId());
	}
	
	public static List<Role> findAllRoles() {
		return Role.getRepository().findByNamedQuery("findAllRoles", new Object[] { new Date() }, Role.class);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Role other = (Role) obj;
		if (getName() == null) {
			if (other.getName() != null) {
				return false;
			}
		} else if (!getName().equals(other.getName())) {
			return false;
		}
		return true;
	}

}
