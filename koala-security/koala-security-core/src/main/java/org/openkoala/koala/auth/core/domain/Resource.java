package org.openkoala.koala.auth.core.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.dayatang.utils.DateUtils;

/**
 * 资源的父类
 * 
 * @author lingen
 * 
 */
@Entity
@Table(name = "KS_RESOURCE")
public class Resource extends Party {

	private static final long serialVersionUID = 7184025731035256920L;

	/** 是否有效 **/
	@Column(name = "ISVALID")
	private boolean isValid;

	/** 资源标识 **/
	@Column(name = "IDENTIFIER", length = 100, nullable = false)
	private String identifier;

	/** 资源级别 **/
	@Column(name = "[LEVEL]")
	private String level;

	/** 资源图标 **/
	@Column(name = "MENU_ICON", length = 100)
	private String menuIcon;

	/** 资源描述 **/
	@Column(name = "DESCRIPTION", length = 100)
	private String desc;

	/** 角色资源授权关系 **/
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "resource")
	private Set<IdentityResourceAuthorization> authorizations = new HashSet<IdentityResourceAuthorization>();

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Set<IdentityResourceAuthorization> getAuthorizations() {
		return authorizations;
	}

	public void setAuthorizations(Set<IdentityResourceAuthorization> authorizations) {
		this.authorizations = authorizations;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public String getMenuIcon() {
		return menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}

	/**
	 * 使资源失效
	 */
	public void disableResource() {
		this.isValid = false;
		this.save();
	}

	/**
	 * 使资源有效
	 */
	public void enableResource() {
		this.isValid = true;
		this.save();
	}

	/**
	 * 为资源分配角色
	 * @param role
	 */
	public void assignRole(Role role) {
		IdentityResourceAuthorization idres = new IdentityResourceAuthorization();
		idres.setIdentity(role);
		idres.setResource(this);
		idres.setAbolishDate(DateUtils.MAX_DATE);
		idres.setCreateDate(new Date());
		idres.setScheduledAbolishDate(new Date());
		idres.save();
	}

	/**
	 * 根据角色ID查找资源
	 * @param roleId
	 * @return
	 */
	public static List<Resource> findResourceByRole(Long roleId) {
		return Resource.findByNamedQuery("findResourceByRole", new Object[] { roleId, new Date() }, Resource.class);
	}

	/**
	 * 根据父资源ID查找子资源
	 * @param parentId
	 * @return
	 */
	public static List<Resource> findChildByParent(Long parentId) {
		if (parentId == null) {
			return Resource.findByNamedQuery("findTopLevelResource", new Object[] { "1", new Date() }, Resource.class);
		} 
		return Resource.findByNamedQuery("findChildByParent", new Object[] { parentId, new Date() }, Resource.class);
	}

	/**
	 * 根据父资源ID和用户账号查找子资源
	 * @param parentId
	 * @param userAccount
	 * @return
	 */
	public static List<Resource> findChildByParentAndUser(Long parentId, String userAccount) {
		if (parentId == null) {
			if ("".equals(userAccount)) {
				return Resource.findByNamedQuery("findTopLevelResource", //
						new Object[] { "1", new Date() }, Resource.class);
			}
			return Resource.findByNamedQuery("findTopLevelResourceByUser", //
					new Object[] { userAccount, new Date(), new Date() }, Resource.class);
		} else {
			if ("".equals(userAccount)) {
				return Resource.findByNamedQuery("findChildByParent",  //
						new Object[] { parentId, new Date() }, Resource.class);
			} 
			return Resource.findByNamedQuery("findChildByParentAndUser", //
					new Object[] { parentId, userAccount, new Date(), new Date() }, Resource.class);
		}
	}

	/**
	 * 删除资源
	 */
	public void removeResource() {
		removeResourceLineAssignment();
		removeIdentityResourceAuthorization();
		removeResourceTypeAssignment();
		this.setAbolishDate(new Date());
	}

	/**
	 * 删除与资源类型的关系 
	 */
	private void removeResourceTypeAssignment() {
		ResourceTypeAssignment.findByResource(this.getId()).setAbolishDate(new Date());
	}

	/**
	 * 删除授权关系
	 */
	private void removeIdentityResourceAuthorization() {
		for (IdentityResourceAuthorization authorization : IdentityResourceAuthorization //
				.findAllRelaitionByResource(this.getId())) {
			authorization.setAbolishDate(new Date());
		}
	}

	/**
	 * 删除垂直关系
	 */
	private void removeResourceLineAssignment() {
		for (ResourceLineAssignment assignment : ResourceLineAssignment.findRelationByResource(this.getId())) {
			assignment.setAbolishDate(new Date());
			if (assignment.getChild() != null && assignment.getChild().getAbolishDate().after(new Date())) {
				assignment.getChild().setAbolishDate(new Date());
			}
		}
	}

	/**
	 * 为资源分配父资源
	 * @param parent
	 */
	public void assignParent(Resource parent) {
		ResourceLineAssignment resLineAssignment = new ResourceLineAssignment();
		resLineAssignment.setCreateDate(new Date());
		resLineAssignment.setAbolishDate(DateUtils.MAX_DATE);
		resLineAssignment.setScheduledAbolishDate(new Date());
		resLineAssignment.setParent(parent);
		resLineAssignment.setChild(this);
		resLineAssignment.save();
	}

	/**
	 * 为资源分配子资源
	 * @param child
	 */
	public void assignChild(Resource child) {
		ResourceLineAssignment resLineAssignment = new ResourceLineAssignment();
		resLineAssignment.setCreateDate(new Date());
		resLineAssignment.setScheduledAbolishDate(new Date());
		resLineAssignment.setAbolishDate(DateUtils.MAX_DATE);
		resLineAssignment.setParent(this);
		resLineAssignment.setChild(child);
		resLineAssignment.save();
	}

	/**
	 * 根据资源标识符查找角色
	 * @param identifier
	 * @return
	 */
	public static List<Role> findRoleByResource(String identifier) {
		return Resource.findByNamedQuery("findRoleByResource", new Object[] { identifier }, Role.class);
	}

	/**
	 * 判断一个角色是否有权限访问某一个资源
	 * @param resId
	 * @param roleId
	 * @return
	 */
	public static boolean hasPrivilegeByRole(Long resId, Long roleId) {
		List<IdentityResourceAuthorization> ls = Resource.findByNamedQuery("queryPrivilegeByRole", //
				new Object[] { resId, roleId, new Date() }, //
				IdentityResourceAuthorization.class);
		return !ls.isEmpty();
	}

	/**
	 * 判断用户是否有权限
	 * @param identifier
	 * @param userAccount
	 * @return
	 */
	public static boolean hasPrivilegeByUser(String identifier, String userAccount) {
		List<IdentityResourceAuthorization> ls = Resource.findByNamedQuery("queryPrivilegeByUser", //
				new Object[] { identifier, userAccount }, //
				IdentityResourceAuthorization.class);
		return !ls.isEmpty();
	}

	/**
	 * 判断一个资源是否有子资源
	 * @param parentId
	 * @return
	 */
	public static boolean hasChildByParent(Long parentId) {
		return !Resource.findByNamedQuery("hasChildByParent", new Object[] { parentId }, Resource.class).isEmpty();
	}

	/**
	 * 是否是菜单资源
	 * 
	 * @param resource
	 * @return
	 */
	public static boolean isMenu(Resource resource) {
		List<Resource> resources = Resource.findByNamedQuery("findResourceById", new Object[] { "KOALA_MENU", //
				"KOALA_DIRETORY", resource.getId() }, Resource.class);
		if (resources != null && !resources.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * 创建资源
	 * @param name			资源名称
	 * @param identifier	资源标识
	 * @param level			级别
	 * @param menuIcon		菜单图标
	 * @return
	 */
	public static Resource newResource(String name, String identifier, String level, String menuIcon) {
	    Resource resource  = null;
	    List<Resource> resources = Resource.getRepository().find("select r from Resource r where r.name = ? " + //
	    		"and r.identifier = ?", new Object[]{name,identifier}, Resource.class);
	    if (resources!=null && resources.size()>0){
	        resource = resources.get(0);
	    }
	    
	    if (resource == null) {
	        resource = new Resource();
		    resource.setName(name);
		    resource.setDesc(name);
		    resource.setCreateDate(new Date());
		    resource.setIdentifier(identifier);
		    resource.setLevel(level);
		    resource.setMenuIcon(menuIcon);
		    resource.setValid(true);
		    resource.setAbolishDate(DateUtils.MAX_DATE);
	    }
	    return resource;
	}

	/**
	 * 资源名称是否已经存在
	 * 
	 * @return
	 */
	public boolean isNameExist() {
		return !Resource.findByNamedQuery("isResouceNameExist", new Object[] { getName(), new Date() }, Resource.class)
				.isEmpty();
	}
	
	/**
	 * 删除所有资源
	 */
	public static void removeAll(){
		String sql = "DELETE FROM Resource";
		Resource.getRepository().executeUpdate(sql, new Object[]{});
	}

	/**
	 * 资源标识是否已经存在
	 * 
	 * @return
	 */
	public boolean isIdentifierExist() {
		return !Resource.findByNamedQuery("isResourceIdentifierExist", new Object[] { getIdentifier(), new Date() },
				Resource.class).isEmpty();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
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
		Resource other = (Resource) obj;
		if (identifier == null) {
			if (other.identifier != null) {
				return false;
			}
		} else if (!identifier.equals(other.identifier)) {
			return false;
		}
		return true;
	}

}
