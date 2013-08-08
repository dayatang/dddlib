package org.openkoala.application.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.apache.commons.lang3.StringUtils;
import org.openkoala.exception.extend.ApplicationException;
import org.openkoala.auth.application.ResourceApplication;
import org.openkoala.auth.application.vo.ResourceVO;
import org.openkoala.auth.application.vo.RoleVO;
import org.openkoala.koala.auth.core.domain.Resource;
import org.openkoala.koala.auth.core.domain.ResourceLineAssignment;
import org.openkoala.koala.auth.core.domain.ResourceType;
import org.openkoala.koala.auth.core.domain.ResourceTypeAssignment;
import org.openkoala.koala.auth.core.domain.Role;
import org.openkoala.util.DateFormatUtils;
import org.springframework.transaction.annotation.Transactional;

import com.dayatang.querychannel.support.Page;
import com.dayatang.utils.DateUtils;

@Named("urlApplication")
@Transactional(value="transactionManager_security")
@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
@Stateless(name = "ResourceApplication")
@Remote
public class ResourceApplicationImpl extends BaseImpl implements ResourceApplication {
    public static ResourceVO domainObject2Vo(Resource resource) {
        ResourceVO treeVO = new ResourceVO();
        treeVO.setId(resource.getId() == null ? null : resource.getId());
        treeVO.setName(resource.getName());
        treeVO.setText(resource.getName());
        treeVO.setDesc(resource.getDesc());
        treeVO.setIdentifier(resource.getIdentifier());
        treeVO.setAbolishDate(DateFormatUtils.format(resource.getAbolishDate()));
        treeVO.setCreateDate(DateFormatUtils.format(resource.getCreateDate()));
        treeVO.setSortOrder(resource.getSortOrder());
        treeVO.setLevel(resource.getLevel() == null ? "0" : resource.getLevel().toString());
        ResourceTypeAssignment assignment = ResourceTypeAssignment.findByResource(resource.getId());
        treeVO.setTypeId(assignment == null ? "" : String.valueOf(assignment.getResourceType().getId()));
        treeVO.setTypeName(assignment == null ? "" : assignment.getResourceType().getName());
        treeVO.setVersion(resource.getVersion());
        return treeVO;
    }

    public static Resource vo2DomainObject(ResourceVO vo) {
        Resource resource = new Resource();
        resource.setId(vo.getId());
        resource.setName(vo.getName());
        resource.setIdentifier(vo.getIdentifier());
        resource.setName(vo.getName());
        resource.setAbolishDate(DateUtils.MAX_DATE);
        resource.setCreateDate(new Date());
        resource.setSerialNumber("0");
        resource.setDesc(vo.getDesc());
        resource.setVersion(vo.getVersion());
        resource.setSortOrder(vo.getSortOrder());
        return resource;
    }
    
    
    /*
     *@see org.openkoala.auth.application.ResourceApplication#findResource(org.openkoala.auth.application.vo.ResourceVO, boolean)
     */
    public List<ResourceVO> findResource(ResourceVO params,boolean loadChildNode) {
        List<ResourceVO> result = new ArrayList<ResourceVO>();
        
        StringBuffer hql = new StringBuffer("from Resource r where 1=1");
        List<Object> conditionVals = new ArrayList<Object>();
        if(params != null){
            if(StringUtils.isNotBlank(params.getName())){
                hql.append(" and r.name like ?");
                conditionVals.add("%"+params.getName() + "%");
            }
            
            if(StringUtils.isNotBlank(params.getIdentifier())){
                hql.append(" and r.identifier like ?");
                conditionVals.add("%"+params.getIdentifier() + "%");
            }
            
            if(StringUtils.isNotBlank(params.getLevel())){
                hql.append(" and r.level = ?");
                conditionVals.add(params.getLevel());
            }
        }
        
        List<Resource> list = queryChannel().queryResult(hql.toString(), conditionVals.toArray());
        if(list != null){
            for (Resource o : list) {
                result.add(domainObject2Vo(o));
                if(loadChildNode){
                    //TODO
                }
            }
        }
        return result;
    }

    /*
     *@see org.openkoala.auth.application.ResourceApplication#loadFirstStageResource(java.lang.Long)
     */
    public List<ResourceVO> loadFirstStageResource(Long resourceId) {
        
        return null;
    }

    public List<ResourceVO> findResourceTree() {
        List<ResourceVO> treeVOs = new ArrayList<ResourceVO>();
        List<Resource> topResources = Resource.findChildByParent(null);
        for (Resource res : topResources) {
            if (!Resource.isMenu(res)) {
                ResourceVO treeVO = domainObject2Vo(res);
                treeVOs.add(treeVO);
                innerFindResourceByParent(treeVO, null);
            }
        }
        return treeVOs;
    }

    public void assign(ResourceVO parentVO, ResourceVO childVO) {
        Resource parent = new Resource();
        parent.setId(Long.valueOf(parentVO.getId()));
        Resource child = Resource.load(Resource.class, childVO.getId());
        child.setLevel(parentVO.getLevel() == null ? "1" : String.valueOf(Integer.parseInt(parentVO.getLevel()) + 1));
        parent.assignChild(child);
    }

    public ResourceVO saveAndAssignParent(ResourceVO resourceVO, ResourceVO parent) {
        ResourceVO child = this.saveResource(resourceVO);
        this.assign(parent, child);
        return child;
    }

    private void innerFindResourceByParent(ResourceVO parent, RoleVO roleVO) {
        List<ResourceVO> childs = new ArrayList<ResourceVO>();
        List<Resource> resources = Resource.findChildByParent(Long.valueOf(parent.getId()));
        for (Resource res : resources) {
            if (!Resource.isMenu(res)) {
                ResourceVO treeVO = domainObject2Vo(res);
                if (roleVO != null) {
                    treeVO.setIschecked(Resource.hasPrivilegeByRole(res.getId(), roleVO.getId()));
                }
                childs.add(treeVO);
                parent.setChildren(childs);
                innerFindResourceByParent(treeVO, roleVO);
            }
        }
    }

    public ResourceVO getResource(Long resourceId) {
        return domainObject2Vo(Resource.get(Resource.class, resourceId));
    }

    public ResourceVO saveResource(ResourceVO resourceVO) {
        if (isNameExist(resourceVO)) {
            throw new ApplicationException("resource.name.exist", null);
        }
        if (isIdentifierExist(resourceVO)) {
            throw new ApplicationException("resource.identifier.exist", null);
        }
        Resource resource = vo2DomainObject(resourceVO);
        resource.setLevel("1");
        resource.save();
        saveResourceTypeAssignment(resourceVO, resource);
        resourceVO.setId(resource.getId());
        return resourceVO;
    }

    private void saveResourceTypeAssignment(ResourceVO resourceVO, Resource resource) {
        ResourceTypeAssignment resourceTypeAssignment = new ResourceTypeAssignment();
        resourceTypeAssignment.setCreateDate(new Date());
        resourceTypeAssignment.setAbolishDate(DateUtils.MAX_DATE);
        resourceTypeAssignment.setResource(resource);
        resourceTypeAssignment
                .setResourceType(ResourceType.load(ResourceType.class, Long.valueOf(resourceVO.getTypeId())));
        resourceTypeAssignment.save();
    }

    public void updateResource(ResourceVO vo) {
        Resource resource = Resource.load(Resource.class, vo.getId());
        resource.setName(vo.getName());
        resource.setIdentifier(vo.getIdentifier());
        resource.setDesc(vo.getDesc());
        ResourceTypeAssignment resourceTypeAssignment = ResourceTypeAssignment.findByResource(vo.getId());
        if (resourceTypeAssignment == null) {
        	resourceTypeAssignment = new ResourceTypeAssignment();
        	resourceTypeAssignment.setAbolishDate(DateUtils.MAX_DATE);
        	resourceTypeAssignment.setCreateDate(new Date());
        	resourceTypeAssignment.setResource(resource);
        	resourceTypeAssignment.setResourceType(ResourceType.load(ResourceType.class, Long.valueOf(vo.getTypeId())));
        	resourceTypeAssignment.save();
        	return;
        }
        resourceTypeAssignment.setResourceType(ResourceType.load(ResourceType.class, Long.valueOf(vo.getTypeId())));
    }

    public void removeResource(long id) {
    	Resource resource = Resource.load(Resource.class, id);
    	resource.removeResource();
    }

    /**
     * 该资源是否已经被授权
     * @param resource
     * @return 
     */
    private boolean hasAuthorizations(Resource resource) {
        if (resource.getAuthorizations() != null && resource.getAuthorizations().size() > 0) {
            return true;
        }
        return false;
    }

    public List<ResourceVO> findAllResource() {
        List<ResourceVO> list = new ArrayList<ResourceVO>();
        {
            List<Resource> all = Resource.findAll(Resource.class);
            for (Resource res : all) {
                list.add(domainObject2Vo(res));
            }
        }
        return list;
    }

    public List<RoleVO> findAllRoleByResource(ResourceVO urlVO) {
        List<RoleVO> list = new ArrayList<RoleVO>();
        {
            List<Role> all = Resource.findRoleByResource(urlVO.getIdentifier());
            for (Role role : all) {
                RoleVO roleVO = new RoleVO();
                roleVO.setAbolishDate(role.getAbolishDate().toString());
                roleVO.setCreateDate(role.getCreateDate().toString());
                roleVO.setCreateOwner(role.getCreateOwner());
                roleVO.setId(role.getId());
                roleVO.setName(role.getName());
                roleVO.setRoleDesc(role.getRoleDesc());
                roleVO.setSerialNumber(role.getSerialNumber());
                roleVO.setSortOrder(role.getSortOrder());
                roleVO.setValid(role.isValid());
                list.add(roleVO);
            }
        }
        return list;
    }

    public static Page<ResourceVO> BasePageQuery(String query, Object[] params, int currentPage, int pageSize) {
        List<ResourceVO> result = new ArrayList<ResourceVO>();
        Page<Resource> pages = queryChannel().queryPagedResultByPageNo(query, params, currentPage, pageSize);
        for (Resource ne : pages.getResult()) {
            result.add(domainObject2Vo(ne));
        }
        Page<ResourceVO> returnPage = new Page<ResourceVO>(pages.getCurrentPageNo(), pages.getTotalCount(),
                pages.getPageSize(), result);
        return returnPage;
    }

    public Page<ResourceVO> pageQueryNotAssignByRole(int currentPage, int pageSize, RoleVO roleVO) {
        return BasePageQuery(
                "select k from org.openkoala.koala.auth.core.domain.UrlResource k where k.id "
                        + "not in(select r.id from org.openkoala.koala.auth.core.domain.Role m,"
                        + "org.openkoala.koala.auth.core.domain.UrlResource r,"
                        + " org.openkoala.koala.auth.core.domain.IdentityResourceAuthorization t where m.id=t.identity.id and r.id=t.resource.id "
                        + " and m.id=?1)", new Object[] {roleVO.getId() }, currentPage, pageSize);
    }

    public boolean isNameExist(ResourceVO resourceVO) {
        return getResource(resourceVO).isNameExist();
    }

    public boolean isIdentifierExist(ResourceVO resourceVO) {
        return getResource(resourceVO).isIdentifierExist();
    }

    private Resource getResource(ResourceVO resourceVO) {
        Resource resource = new Resource();
        resource.setName(resourceVO.getName());
        resource.setIdentifier(resourceVO.getIdentifier());
        return resource;
    }

    public void initResource(String type) {
        String menuIcon = "images/icons/other/node.png";
        
//        ResourceLineAssignment.removeAll();
//        Resource.removeAll();
//        ResourceType.removeAll();
//        
//        
        Resource resourceManager = null;
        Resource resource = null;
        Resource menuResource = null;
        Resource typeResource = null;
        Resource userRoleResource = null;
        
        Resource userManager = null;
        Resource roleManager = null;
        ResourceType koalaMenu = null;
        ResourceType koalaDirectory = null;
        
        if("StrutsMVC".equals(type)){
            
             resourceManager = Resource.newResource("资源", "resource", "1", menuIcon);
             resource = Resource.newResource("资源管理", "auth-Resource-list.action", "2", menuIcon);
             menuResource = Resource.newResource("菜单管理", "auth-Menu-list.action", "2", menuIcon);
             typeResource = Resource.newResource("资源类型管理", "auth-ResourceType-list.action", "2", menuIcon);
             userRoleResource = Resource.newResource("用户角色管理", "userole", "1", menuIcon);
            
             userManager = Resource.newResource("用户管理", "auth-User-list.action", "2", menuIcon);
             roleManager = Resource.newResource("角色管理", "auth-Role-list.action", "2", menuIcon);
             koalaMenu = ResourceType.newResourceType("KOALA_MENU");
             koalaDirectory = ResourceType.newResourceType("KOALA_DIRETORY");
        }
        else{
        	  resourceManager = Resource.newResource("资源", "resource", "1", menuIcon);
              resource = Resource.newResource("资源管理", "auth/Resource/list.koala", "2", menuIcon);
              menuResource = Resource.newResource("菜单管理", "auth/Menu/list.koala", "2", menuIcon);
              typeResource = Resource.newResource("资源类型管理", "auth/ResourceType/list.koala", "2", menuIcon);
              userRoleResource = Resource.newResource("用户角色管理", "userole", "1", menuIcon);
             
              userManager = Resource.newResource("用户管理", "auth/User/list.koala", "2", menuIcon);
              roleManager = Resource.newResource("角色管理", "auth/Role/list.koala", "2", menuIcon);
              koalaMenu = ResourceType.newResourceType("KOALA_MENU");
              koalaDirectory = ResourceType.newResourceType("KOALA_DIRETORY");
        }

        resourceManager.save();
        resource.save();
        menuResource.save();
        typeResource.save();
        userRoleResource.save();
        userManager.save();
        roleManager.save();
        koalaMenu.save();
        koalaDirectory.save();
        ResourceLineAssignment resourceLineResource = ResourceLineAssignment.newResourceLineAssignment(resourceManager,
                resource);
        ResourceLineAssignment resourceLineMenu = ResourceLineAssignment.newResourceLineAssignment(resourceManager,
                menuResource);
        ResourceLineAssignment resourceLinetype = ResourceLineAssignment.newResourceLineAssignment(resourceManager,
                typeResource);
        ResourceLineAssignment urlroleLineUser = ResourceLineAssignment.newResourceLineAssignment(userRoleResource,
                userManager);
        ResourceLineAssignment urlroleLineRole = ResourceLineAssignment.newResourceLineAssignment(userRoleResource,
                roleManager);
        resourceLineResource.save();
        resourceLineMenu.save();
        resourceLinetype.save();
        urlroleLineUser.save();
        urlroleLineRole.save();
        ResourceTypeAssignment resourceManagerType = ResourceTypeAssignment.newResourceTypeAssignment(resourceManager,
                koalaDirectory);
        ResourceTypeAssignment userRoleResourceType = ResourceTypeAssignment.newResourceTypeAssignment(
                userRoleResource, koalaDirectory);
        ResourceTypeAssignment resourceType = ResourceTypeAssignment.newResourceTypeAssignment(resource, koalaMenu);
        ResourceTypeAssignment menuResourceType = ResourceTypeAssignment.newResourceTypeAssignment(menuResource,
                koalaMenu);
        ResourceTypeAssignment typeResourceType = ResourceTypeAssignment.newResourceTypeAssignment(typeResource,
                koalaMenu);
        ResourceTypeAssignment userManagerType = ResourceTypeAssignment.newResourceTypeAssignment(userManager,
                koalaMenu);
        ResourceTypeAssignment roleManagerType = ResourceTypeAssignment.newResourceTypeAssignment(roleManager,
                koalaMenu);
        resourceManagerType.save();
        userRoleResourceType.save();
        resourceType.save();
        menuResourceType.save();
        typeResourceType.save();
        userManagerType.save();
        roleManagerType.save();
    }

    
}
