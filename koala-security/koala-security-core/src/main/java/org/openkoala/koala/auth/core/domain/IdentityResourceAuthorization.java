package org.openkoala.koala.auth.core.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author lingen
 * 授权关系
 * 责任方：角色 ROLE
 * 委托方：资源 Resources
 * 
 */
@Entity
@Table(name = "KS_IDENTITY_RESOURCE_AUTH")
public class IdentityResourceAuthorization extends Accountability {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5527620335383954438L;
	
	@ManyToOne
	@JoinColumn(name = "IDENTITY_ID", nullable = false)
	private Identity identity;

	@ManyToOne
	@JoinColumn(name = "RESOURCE_ID", nullable = false)
	private Resource resource;
	
	public Identity getIdentity() {
		return identity;
	}
	
	public static List<IdentityResourceAuthorization> findAllRelaitionByResource(Long resId)
	{
		Object[] params = new Object[]{resId};
		return IdentityResourceAuthorization.findByNamedQuery(
				"findAllRelaitionByResource", 
				params, IdentityResourceAuthorization.class);
	}


	public void setIdentity(Identity identity) {
		this.identity = identity;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	/**
	 * 查找所有资源和角色
	 * @return
	 */
	public static List<IdentityResourceAuthorization> findAllReourcesAndRoles(){
	    return IdentityResourceAuthorization.getRepository().findAll(IdentityResourceAuthorization.class);
	}
	
	/**
	 * 根据角色ID获取角色的授权信息
	 * @param roleId
	 * @return
	 */
	public static List<IdentityResourceAuthorization> findAuthorizationByRole(long roleId) {
		return findByNamedQuery("findAuthorizationByRole", new Object[] { roleId, new Date() }, //
				IdentityResourceAuthorization.class);
	}

}
