package org.openkoala.application.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.openkoala.exception.extend.ApplicationException;
import org.openkoala.auth.application.MenuApplication;
import org.openkoala.auth.application.ResourceApplication;
import org.openkoala.auth.application.vo.ResourceVO;
import org.openkoala.auth.application.vo.RoleVO;
import org.openkoala.koala.auth.core.domain.Resource;
import org.openkoala.koala.auth.core.domain.ResourceType;
import org.openkoala.koala.auth.core.domain.ResourceTypeAssignment;
import org.openkoala.util.DateFormatUtils;
import org.springframework.transaction.annotation.Transactional;

import com.dayatang.querychannel.support.Page;
import com.dayatang.utils.DateUtils;

@Named
@Remote
@Stateless(name = "MenuApplication")
@Transactional(value="transactionManager_security")
@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
public class MenuApplicationImpl extends BaseImpl implements MenuApplication {

	@Inject
	private ResourceApplication resourceApplication;

	public static Page<ResourceVO> basePageQuery(String query, Object[] params, int currentPage, int pageSize) {
		List<ResourceVO> result = new ArrayList<ResourceVO>();
		Page<Resource> pages = queryChannel().queryPagedResultByPageNo(query, params, currentPage, pageSize);
		for (Resource resource : pages.getResult()) {
			ResourceVO resourceVO = new ResourceVO();
			resourceVO.domain2Vo(resource);
			ResourceTypeAssignment assignment = ResourceTypeAssignment.findByResource(resource.getId());
			if (assignment != null) {
				resourceVO.setMenuType(String.valueOf(assignment.getResourceType().getId()));
			}
			result.add(resourceVO);
		}
		return new Page<ResourceVO>(pages.getCurrentPageNo(), pages.getTotalCount(), pages.getPageSize(), result);
	}

	public ResourceVO getMenu(Long menuId) {
		Resource resource = Resource.load(Resource.class, menuId);
		ResourceVO resourceVO = new ResourceVO();
		resourceVO.domain2Vo(resource);
		ResourceTypeAssignment assignment = ResourceTypeAssignment.findByResource(resource.getId());
		if (assignment != null) {
			resourceVO.setMenuType(String.valueOf(assignment.getResourceType().getId()));
		}
		return resourceVO;
	}

	public ResourceVO saveMenu(ResourceVO vo) {
		if (resourceApplication.isNameExist(vo)) {
			throw new ApplicationException("menu.name.exist", null);
		}

		if (resourceApplication.isIdentifierExist(vo)) {
			throw new ApplicationException("menu.identifier.exist", null);
		}

		vo.setAbolishDate(DateFormatUtils.format(new Date()));
		vo.setCreateDate(DateFormatUtils.format(new Date()));
		vo.setLevel("1");
		vo.setSerialNumber("0");
		vo.setSortOrder(0);
		Resource resource = new Resource();
		vo.vo2Domain(resource);
		resource.save();
		saveResourceTypeAssignment(vo, resource);
		vo.setId(resource.getId());
		return vo;
	}

	private void saveResourceTypeAssignment(ResourceVO vo, Resource resource) {
		ResourceTypeAssignment resourceTypeAssignment = new ResourceTypeAssignment();
		resourceTypeAssignment.setCreateDate(new Date());
		resourceTypeAssignment.setAbolishDate(DateUtils.MAX_DATE);
		resourceTypeAssignment.setResource(resource);
		resourceTypeAssignment.setResourceType(ResourceType.load(ResourceType.class, Long.valueOf(vo.getMenuType())));
		resourceTypeAssignment.save();
	}

	public void updateMenu(ResourceVO resourceVO) {
		Resource resource = Resource.load(Resource.class, resourceVO.getId());
		resource.setName(resourceVO.getName());
		resource.setIdentifier(resourceVO.getIdentifier());
		resource.setMenuIcon(resourceVO.getIcon());
		resource.setDesc(resourceVO.getDesc());
		ResourceTypeAssignment.findByResource(resourceVO.getId()).setResourceType(ResourceType.load(ResourceType.class, //
				Long.valueOf(resourceVO.getMenuType())));
	}

	public void removeMenu(Long menuId) {
		Resource.load(Resource.class, menuId).removeResource();
	}

	public List<ResourceVO> findAllMenu() {
		List<ResourceVO> list = new ArrayList<ResourceVO>();
		List<Resource> all = Resource.findAll(Resource.class);
		for (Resource menu : all) {
			ResourceVO resourceVO = new ResourceVO();
			resourceVO.domain2Vo(menu);
			ResourceTypeAssignment assignment = ResourceTypeAssignment.findByResource(menu.getId());
			if (assignment != null) {
				resourceVO.setMenuType(String.valueOf(assignment.getResourceType().getId()));
			}
			list.add(resourceVO);
		}
		return list;
	}

	public Page<ResourceVO> pageQueryMenu(int currentPage, int pageSize) {
		return basePageQuery("select m from Resource m order by m.id", new Object[0], currentPage, pageSize);
	}

	public void assign(ResourceVO parentVO, ResourceVO childVO) {
		Resource parent = new Resource();
		parent.setId(parentVO.getId());
		Resource child = Resource.load(Resource.class, childVO.getId());
		child.setLevel(String.valueOf(Integer.parseInt(parentVO.getLevel()) + 1));
		child.setId(childVO.getId());
		parent.assignChild(child);
	}
	
	/**
	 * 获取一个用户的一级目录
	 * @param userAccount
	 * @return
	 */
	public List<ResourceVO> findTopMenuByUser(String userAccount) {
		List<ResourceVO> treeVOs = new ArrayList<ResourceVO>();
		List<Resource> topMenus = Resource.findChildByParentAndUser(null, userAccount);
		for (Resource res : topMenus) {
			if (Resource.isMenu(res)) {
				ResourceVO treeVO = new ResourceVO();
				treeVO.domain2Vo(res);
				ResourceTypeAssignment assignment = ResourceTypeAssignment.findByResource(res.getId());
				if (assignment != null) {
					treeVO.setMenuType(String.valueOf(assignment.getResourceType().getId()));
				}
				treeVOs.add(treeVO);
			}
		}
		return treeVOs;
	}

	public List<ResourceVO> findAllMenuByUser(String userAccount) {
		List<ResourceVO> treeVOs = new ArrayList<ResourceVO>();
		List<Resource> topMenus = Resource.findChildByParentAndUser(null, userAccount);
		for (Resource res : topMenus) {
			if (Resource.isMenu(res)) {
				ResourceVO treeVO = new ResourceVO();
				treeVO.domain2Vo(res);
				ResourceTypeAssignment assignment = ResourceTypeAssignment.findByResource(res.getId());
				if (assignment != null) {
					treeVO.setMenuType(String.valueOf(assignment.getResourceType().getId()));
				}
				treeVOs.add(treeVO);
				innerFindMenuByParentAndUser(treeVO, userAccount);
			}
		}
		return treeVOs;
	}

	public List<ResourceVO> findChildSelectItemByRole(ResourceVO parent, RoleVO roleVO) {
		List<ResourceVO> result = new ArrayList<ResourceVO>();
		List<Resource> childs = Resource.findChildByParent(parent.getId());
		for (Resource child : childs) {
			if (Resource.isMenu(child)) {
				ResourceVO treeds = new ResourceVO();
				treeds.domain2Vo(child);
				ResourceTypeAssignment assignment = ResourceTypeAssignment.findByResource(child.getId());
				if (assignment != null) {
					treeds.setMenuType(String.valueOf(assignment.getResourceType().getId()));
				}
				treeds.setIschecked(Resource.hasPrivilegeByRole(child.getId(), roleVO.getId()));
				result.add(treeds);
			}
		}
		return result;
	}

	public List<ResourceVO> findAllTreeSelectItemByRole(RoleVO roleVO) {
		List<ResourceVO> treeVOs = new ArrayList<ResourceVO>();
		List<Resource> topMenus = Resource.findChildByParent(null);
		for (Resource res : topMenus) {
			ResourceVO treeVO = new ResourceVO();
			treeVO.domain2Vo(res);
			ResourceTypeAssignment assignment = ResourceTypeAssignment.findByResource(res.getId());
			if (assignment != null) {
				treeVO.setMenuType(String.valueOf(assignment.getResourceType().getId()));
			}
			treeVO.setIschecked(Resource.hasPrivilegeByRole(res.getId(), roleVO.getId()));
			treeVOs.add(treeVO);
			innerFindMenuByParent(treeVO, roleVO);
		}
		return treeVOs;
	}

	public List<ResourceVO> findMenuTree() {
		List<ResourceVO> treeVOs = new ArrayList<ResourceVO>();
		List<Resource> topMenus = Resource.findChildByParent(null);
		for (Resource res : topMenus) {
			if (Resource.isMenu(res)) {
				ResourceVO treeVO = new ResourceVO();
				treeVO.domain2Vo(res);
				ResourceTypeAssignment assignment = ResourceTypeAssignment.findByResource(res.getId());
				if (assignment != null) {
					treeVO.setMenuType(String.valueOf(assignment.getResourceType().getId()));
				}
				treeVOs.add(treeVO);
				innerFindMenuByParent(treeVO, null);
			}
		}
		return treeVOs;
	}

	public List<ResourceVO> findAllChildByParentAndUser(ResourceVO menuVO, String userAccount) {
		List<ResourceVO> treeVOs = new ArrayList<ResourceVO>();
		List<Resource> topMenus = Resource
				.findChildByParentAndUser(menuVO == null ? null : menuVO.getId(), userAccount);
		for (Resource menu : topMenus) {
			if (Resource.isMenu(menu)) {
				ResourceVO treeVO = new ResourceVO();
				treeVO.domain2Vo(menu);
				ResourceTypeAssignment assignment = ResourceTypeAssignment.findByResource(menu.getId());
				if (assignment != null) {
					treeVO.setMenuType(String.valueOf(assignment.getResourceType().getId()));
				}
				treeVOs.add(treeVO);
				innerFindMenuByParentAndUser(treeVO, userAccount);
			}
		}
		return treeVOs;
	}

	public List<ResourceVO> findChildByParentAndUser(ResourceVO menuVO, String userAccount) {
		List<ResourceVO> treeVOs = new ArrayList<ResourceVO>();
		List<Resource> topMenus = Resource
				.findChildByParentAndUser(menuVO == null ? null : menuVO.getId(), userAccount);
		for (Resource menu : topMenus) {
			if (Resource.isMenu(menu)) {
				ResourceVO treeVO = new ResourceVO();
				treeVO.domain2Vo(menu);
				ResourceTypeAssignment assignment = ResourceTypeAssignment.findByResource(menu.getId());
				if (assignment != null) {
					treeVO.setMenuType(String.valueOf(assignment.getResourceType().getId()));
				}
				treeVOs.add(treeVO);
			}
		}
		return treeVOs;
	}

	private void innerFindMenuByParent(ResourceVO parent, RoleVO roleVO) {
		List<ResourceVO> childs = new ArrayList<ResourceVO>();
		List<Resource> menus = Resource.findChildByParent(parent.getId());
		for (Resource res : menus) {
			ResourceVO treeVO = new ResourceVO();
			treeVO.domain2Vo(res);
			ResourceTypeAssignment assignment = ResourceTypeAssignment.findByResource(res.getId());
			if (assignment != null) {
				treeVO.setMenuType(String.valueOf(assignment.getResourceType().getId()));
			}
			if (roleVO != null) {
				treeVO.setIschecked(Resource.hasPrivilegeByRole(res.getId(), roleVO.getId()));
			}
			childs.add(treeVO);
			parent.setChildren(childs);
			innerFindMenuByParent(treeVO, roleVO);
		}
	}

	private void innerFindMenuByParentAndUser(ResourceVO parent, String userAccount) {
		List<ResourceVO> childs = new ArrayList<ResourceVO>();
		List<Resource> menus = null;
		menus = Resource.findChildByParentAndUser(parent.getId(), userAccount);
		for (Resource menu : menus) {
			if (Resource.isMenu(menu)) {
				ResourceVO treeVO = new ResourceVO();
				treeVO.domain2Vo(menu);
				ResourceTypeAssignment assignment = ResourceTypeAssignment.findByResource(menu.getId());
				if (assignment != null) {
					treeVO.setMenuType(String.valueOf(assignment.getResourceType().getId()));
				}
				childs.add(treeVO);
				parent.setChildren(childs);
				innerFindMenuByParentAndUser(treeVO, userAccount);
			}
		}
	}

	public ResourceVO saveAndAssignParent(ResourceVO menuVO, ResourceVO parent) {
		ResourceVO child = this.saveMenu(menuVO);
		this.assign(parent, child);
		return child;
	}

	public void updateMeunOrder(List<ResourceVO> resourceVOs) {
		for (ResourceVO resourceVO : resourceVOs) {
			Resource menu = Resource.load(Resource.class, resourceVO.getId());
			menu.setSortOrder(resourceVO.getSortOrder());
		}
	}
}
