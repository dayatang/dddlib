package org.openkoala.koala.auth.core.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 责任关系：用户角色授权
 * 责任方：用户 USER
 * 委托方：角色 ROLE
 * @author lingen
 *
 */
@Entity
@Table(name = "KS_ROLE_USER_AUTH")
public class RoleUserAuthorization extends Accountability {
	
	@ManyToOne
	@JoinColumn(name = "ROLE_ID", nullable = false)
	private Role role;
	
	@ManyToOne
	@JoinColumn(name = "USER_ID", nullable = false)
	private User user;
	

	private static final long serialVersionUID = 8314855131825344365L;

	public Role getRole() {
		return role;
	}


	public void setRole(Role role) {
		this.role = role;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 根据用户查找该用户拥有的角色
	 * @param user
	 * @return
	 */
	public static List<RoleUserAuthorization> findAuthorizationByUser(User user) {
		return findByNamedQuery("findAuthorizationByUser", new Object[] { user, new Date() }, RoleUserAuthorization.class);
	}
	
	/**
	 * 根据角色ID查找该角色拥有的用户
	 * @param roleId
	 * @return
	 */
	public static List<RoleUserAuthorization> findUserAuthorizationByRole(long roleId) {
		return findByNamedQuery("findUserAuthorizationByRole", new Object[] { roleId, new Date() }, RoleUserAuthorization.class);
	}
}
