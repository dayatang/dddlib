package org.openkoala.jbpm.core.jbpm.action.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.openkoala.auth.application.ResourceApplication;
import org.openkoala.auth.application.ResourceTypeApplication;
import org.openkoala.auth.application.vo.ResourceVO;
import org.openkoala.auth.application.vo.RoleVO;
import org.openkoala.koala.auth.ss3adapter.ehcache.CacheUtil;
import com.dayatang.querychannel.support.Page;
import com.opensymphony.xwork2.ActionSupport;

public class ResourceAction extends ActionSupport {

	private static final long serialVersionUID = -4480422887567118987L;
	@Inject
	private ResourceApplication resourceApplication;

	@Inject
	private ResourceTypeApplication resourceTypeApplication;

	private Map<String, Object> dataMap = new HashMap<String, Object>();
	private List<ResourceVO> resources = new ArrayList<ResourceVO>();
	private String page;
	private String pagesize;
	private ResourceVO resourceVO;
	private ResourceVO parentVO;
	private ResourceVO childVO;
	private RoleVO roleVO;

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public List<ResourceVO> getUrlResources() {
		return resources;
	}

	public void setUrlResources(List<ResourceVO> urlResources) {
		this.resources = urlResources;
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

	public ResourceVO getResourceVO() {
		return resourceVO;
	}

	public void setResourceVO(ResourceVO resourceVO) {
		this.resourceVO = resourceVO;
	}

	public String list() {
		return "METHOD";
	}

	public RoleVO getRoleVO() {
		return roleVO;
	}

	public void setRoleVO(RoleVO roleVO) {
		this.roleVO = roleVO;
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

	public String pageQueryNotAssignUrlByUser() {
		int start = Integer.parseInt(page);
		int limit = Integer.parseInt(pagesize);
		Page<ResourceVO> all = resourceApplication.pageQueryNotAssignByRole(start, limit, roleVO);
		dataMap.put("Rows", all.getResult());
		dataMap.put("start", start * limit - limit);
		dataMap.put("limit", limit);
		dataMap.put("Total", all.getTotalCount());
		return "JSON";
	}

	public String add() {
		dataMap.clear();
		resourceApplication.saveResource(resourceVO);
		CacheUtil.refreshUrlAttributes(resourceVO.getIdentifier());
		dataMap.put("result", "success");
		return "JSON";
	}

	public String addAndAssignParent() {
		resourceApplication.saveAndAssignParent(childVO, parentVO);
		CacheUtil.refreshUrlAttributes(childVO.getIdentifier());
		dataMap.put("result", "success");
		return "JSON";
	}

	public String del() {
		resourceApplication.removeResource(resourceVO.getId());
		CacheUtil.refreshUrlAttributes(resourceVO.getIdentifier());
		dataMap.put("result", "success");
		return "JSON";
	}

	/**
	 * 查询某节点下一级子菜单并判断是否有权限
	 * 
	 * @return
	 */
	public String update() {
		dataMap.clear();
		resourceApplication.updateResource(resourceVO);
		CacheUtil.refreshUrlAttributes(resourceVO.getIdentifier());
		dataMap.put("result", "success");
		return "JSON";
	}

	/**
	 * 资源树
	 * 
	 * @return
	 */
	public String findAllReourceTree() {
		dataMap.clear();
		dataMap.put("Rows", resourceApplication.findResourceTree());
		return "JSON";
	}

	public String findAllResourceType() {
		dataMap.put("data", resourceTypeApplication.findResourceType());
		return "JSON";
	}

}