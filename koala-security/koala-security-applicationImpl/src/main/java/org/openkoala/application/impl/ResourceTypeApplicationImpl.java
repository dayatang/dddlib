package org.openkoala.application.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.openkoala.exception.extend.ApplicationException;
import org.openkoala.auth.application.ResourceTypeApplication;
import org.openkoala.auth.application.vo.ResourceTypeVO;
import org.openkoala.koala.auth.core.domain.ResourceType;
import org.openkoala.koala.auth.core.domain.ResourceTypeAssignment;
import org.springframework.transaction.annotation.Transactional;

import com.dayatang.querychannel.support.Page;
import com.dayatang.utils.DateUtils;

@Named
@Transactional(value="transactionManager_security")
@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
@Stateless(name = "ResourceTypeApplication")
@Remote
public class ResourceTypeApplicationImpl extends BaseImpl implements ResourceTypeApplication {

	public boolean isExist(ResourceTypeVO resourceTypeVO) {
		ResourceType resourceType = queryChannel().querySingleResult("from ResourceType o where o.name=? and o.abolishDate>?",
				new Object[] { resourceTypeVO.getName(), new Date() });
		if (resourceType != null) {
			throw new ApplicationException("resourceType.exist", null);
		}
		return false;
	}

	public void save(ResourceTypeVO resourceTypeVO) {
		if (!isExist(resourceTypeVO)) {
			ResourceType resourceType = new ResourceType();
			resourceType.setName(resourceTypeVO.getName());
			resourceType.setCreateDate(new Date());
			resourceType.setAbolishDate(DateUtils.MAX_DATE);
			resourceType.setSerialNumber("0");
			resourceType.setSortOrder(0);
			resourceType.save();
			resourceTypeVO.setId(String.valueOf(resourceType.getId()));
		}
	}

	public void delete(ResourceTypeVO resourceTypeVO) {
		delete(new ResourceTypeVO[] { resourceTypeVO });
	}

	public void delete(ResourceTypeVO[] resourceTypeVOs) {
		for (ResourceTypeVO resourceTypeVO : resourceTypeVOs) {
			ResourceType resourceType = ResourceType.load(ResourceType.class, Long.valueOf(resourceTypeVO.getId()));
			resourceType.setAbolishDate(new Date());
			removeResourceTypeAssignment(resourceType);
		}
	}

	/**
	 * 删除与资源的关系
	 * @param resourceType
	 */
	private void removeResourceTypeAssignment(ResourceType resourceType) {
		for (ResourceTypeAssignment assignment : resourceType.getResources()) {
			assignment.setAbolishDate(new Date());
		}
	}

	public void update(ResourceTypeVO resourceTypeVO) {
		ResourceType resourceType = ResourceType.load(ResourceType.class, Long.valueOf(resourceTypeVO.getId()));
		if (resourceType.getName().equals(resourceTypeVO.getName())) {
			return;
		}
		if (!isExist(resourceTypeVO)) {
			resourceType.setName(resourceTypeVO.getName());
		}
	}

	public Page<ResourceTypeVO> pageQuery(int currentPage, int pageSize) {
		Page<ResourceType> page = queryChannel().queryPagedResultByPageNo(
				"from ResourceType o where o.name<>? and o.name<>? and o.abolishDate>?",
				new Object[] { "KOALA_MENU", "KOALA_DIRETORY", new Date() }, currentPage, pageSize);
		List<ResourceTypeVO> list = new ArrayList<ResourceTypeVO>();
		for (ResourceType resourceType : page.getResult()) {
			ResourceTypeVO resourceTypeVO = new ResourceTypeVO();
			resourceTypeVO.setId(String.valueOf(resourceType.getId()));
			resourceTypeVO.setName(resourceType.getName());
			list.add(resourceTypeVO);
		}
		return new Page<ResourceTypeVO>(page.getCurrentPageNo(), page.getTotalPageCount(), page.getPageSize(), list);
	}


	public List<ResourceTypeVO> findResourceType() {
		List<ResourceType> resourceTypes = ResourceType.findAllResourceType();
		List<ResourceTypeVO> result = new ArrayList<ResourceTypeVO>();
		for (ResourceType resourceType : resourceTypes) {
			ResourceTypeVO resourceTypeVO = new ResourceTypeVO();
			resourceTypeVO.setId(String.valueOf(resourceType.getId()));
			resourceTypeVO.setName(resourceType.getName());
			resourceTypeVO.setText(resourceType.getName());
			result.add(resourceTypeVO);
		}
		return result;
	}

	public List<ResourceTypeVO> findMenuType() {
		List<ResourceTypeVO> result = new ArrayList<ResourceTypeVO>();
		for (ResourceType resourceType : ResourceType.findMenuType()) {
			ResourceTypeVO resourceTypeVO = new ResourceTypeVO();
			resourceTypeVO.setId(String.valueOf(resourceType.getId()));
			resourceTypeVO.setName(resourceType.getName());
			result.add(resourceTypeVO);
		}
		return result;
	}

}
