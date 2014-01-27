package org.openkoala.koala.auth.core.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.dayatang.utils.DateUtils;

/**
 * 资源类型
 * 
 * @author Ken
 * @since 2013-03-12 09:25
 * 
 */
@Entity
@Table(name = "KS_RESOURCE_TYPE")
public class ResourceType extends Party {

	private static final long serialVersionUID = -5507294042279204096L;

	/**
	 * 查找所有资源类型
	 * 
	 * @return
	 */
	public static List<ResourceType> findAllResourceType() {
		return findByNamedQuery("findAllResourceType", new Object[] { "KOALA_MENU", "KOALA_DIRETORY", new Date() },
				ResourceType.class);
	}
	
	/**
	 * 查找菜单类型
	 * @return
	 */
	public static List<ResourceType> findMenuType() {
		return findByNamedQuery("findMenuType", new Object[] { "KOALA_MENU", "KOALA_DIRETORY", new Date() },
				ResourceType.class);
	}

	/**
	 * 创建资源类型
	 * @param name	资源类型名称
	 * @return
	 */
	public static ResourceType newResourceType(String name){
	    
	    ResourceType type  = null;
        List<ResourceType> resources = Resource.getRepository().find("select r from ResourceType r where r.name=?", // 
        		new Object[]{name}, ResourceType.class);
        
        if (resources != null && resources.size() > 0) {
            type = resources.get(0);
        }
        
        if (type == null) {
	      type = new ResourceType();
	      type.setName(name);
	      type.setCreateDate(new Date());
	      type.setAbolishDate(DateUtils.MAX_DATE);
        }
	    return type;
	}
	
	/**
	 * 根据资源类型ID获取资源类型与资源的关系
	 * @return
	 */
	public List<ResourceTypeAssignment> getResources() {
		return ResourceTypeAssignment.findResourceByType(getId());
	}
	
	/**
	 * 删除所有资源类型
	 */
	public static void removeAll(){
		ResourceType.getRepository().executeUpdate("DELETE FROM ResourceType", new Object[]{});
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
		ResourceType other = (ResourceType) obj;
		if (getName() == null) {
			if (other.getName() != null) {
				return false;
			}
		} else if (!getName().equals(other.getName())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
