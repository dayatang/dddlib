package org.openkoala.bpm.processdyna.web.controller.auth;

import java.util.ArrayList;
import java.util.List;

import org.openkoala.auth.application.vo.QueryConditionVO;
import org.openkoala.auth.application.vo.ResourceTypeVO;
import org.openkoala.auth.application.vo.ResourceVO;
import org.openkoala.auth.application.vo.RoleVO;
import org.openkoala.auth.application.vo.UserVO;

/**
 * 页面参数封装类
 * 
 * @author lingen
 * 
 */
public class ParamsPojo {

	private List<ResourceVO> rows = new ArrayList<ResourceVO>();

	private List<ResourceVO> resourceVOs = new ArrayList<ResourceVO>();

	private String page;

	private String pagesize;

	private ResourceVO resVO;

	private RoleVO roleVO;

	private ResourceVO parent;

	private ResourceVO resourceVO;

	private ResourceVO parentVO;

	private ResourceVO childVO;

	private ResourceTypeVO resourceTypeVO;

	private List<ResourceTypeVO> resourceTypeVOs = new ArrayList<ResourceTypeVO>();
	
	private List<RoleVO> roles = new ArrayList<RoleVO>();
	
	private List<ResourceVO> menus = new ArrayList<ResourceVO>();

	private QueryConditionVO search = new QueryConditionVO();
	
	private List<UserVO> users = new ArrayList<UserVO>();
	
	private Long userId;
	
	private String userAccount;
	
	private List<ResourceVO> addList;
	
	private List<ResourceVO> delList;
	
	private UserVO userVO;
	
	private Long roleId;


	public List<ResourceVO> getRows() {
		return rows;
	}

	public void setRows(List<ResourceVO> rows) {
		this.rows = rows;
	}

	public List<ResourceVO> getResourceVOs() {
		return resourceVOs;
	}

	public void setResourceVOs(List<ResourceVO> resourceVOs) {
		this.resourceVOs = resourceVOs;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getPagesize() {
		return pagesize;
	}

	public void setPagesize(String pagesize) {
		this.pagesize = pagesize;
	}

	public ResourceVO getResVO() {
		return resVO;
	}

	public void setResVO(ResourceVO resVO) {
		this.resVO = resVO;
	}

	public RoleVO getRoleVO() {
		return roleVO;
	}

	public void setRoleVO(RoleVO roleVO) {
		this.roleVO = roleVO;
	}

	public ResourceVO getParent() {
		return parent;
	}

	public void setParent(ResourceVO parent) {
		this.parent = parent;
	}

	public ResourceVO getResourceVO() {
		return resourceVO;
	}

	public void setResourceVO(ResourceVO resourceVO) {
		this.resourceVO = resourceVO;
	}

	public ResourceVO getParentVO() {
		return parentVO;
	}

	public void setParentVO(ResourceVO parentVO) {
		this.parentVO = parentVO;
	}

	public ResourceVO getChildVO() {
		return childVO;
	}

	public void setChildVO(ResourceVO childVO) {
		this.childVO = childVO;
	}

	public ResourceTypeVO getResourceTypeVO() {
		return resourceTypeVO;
	}

	public void setResourceTypeVO(ResourceTypeVO resourceTypeVO) {
		this.resourceTypeVO = resourceTypeVO;
	}

	public List<ResourceTypeVO> getResourceTypeVOs() {
		return resourceTypeVOs;
	}

	public void setResourceTypeVOs(List<ResourceTypeVO> resourceTypeVOs) {
		this.resourceTypeVOs = resourceTypeVOs;
	}

	public List<RoleVO> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleVO> roles) {
		this.roles = roles;
	}

	public List<ResourceVO> getMenus() {
		return menus;
	}

	public void setMenus(List<ResourceVO> menus) {
		this.menus = menus;
	}

	public QueryConditionVO getSearch() {
		return search;
	}

	public void setSearch(QueryConditionVO search) {
		this.search = search;
	}

	public List<UserVO> getUsers() {
		return users;
	}

	public void setUsers(List<UserVO> users) {
		this.users = users;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public List<ResourceVO> getAddList() {
		return addList;
	}

	public void setAddList(List<ResourceVO> addList) {
		this.addList = addList;
	}

	public List<ResourceVO> getDelList() {
		return delList;
	}

	public void setDelList(List<ResourceVO> delList) {
		this.delList = delList;
	}

	public UserVO getUserVO() {
		return userVO;
	}

	public void setUserVO(UserVO userVO) {
		this.userVO = userVO;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

}
