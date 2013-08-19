package org.openkoala.koala.auth.core.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.dayatang.utils.DateUtils;

/**
 * 资源的垂直关系
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Aug 16, 2013 9:14:59 AM
 */
@Entity
@Table(name = "KS_RESOURCE_LINEASSIGNMENT")
public class ResourceLineAssignment extends Accountability {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "PARENT_ID", nullable = false)
	private Resource parent;

	@ManyToOne
	@JoinColumn(name = "CHILD_ID", nullable = false)
	private Resource child;

	/**
	 * 查找资源关系
	 * @param resourceId
	 * @return
	 */
	public static List<ResourceLineAssignment> findRelationByResource(Long resourceId) {
		return ResourceLineAssignment.findByNamedQuery("findRelationByResource",  //
				new Object[] { resourceId, resourceId }, ResourceLineAssignment.class);
	}

	public Resource getParent() {
		return parent;
	}

	public void setParent(Resource parent) {
		this.parent = parent;
	}

	public Resource getChild() {
		return child;
	}

	public void setChild(Resource child) {
		this.child = child;
	}
	
	/**
	 * 删除所有的资源垂直关系
	 */
	public static void removeAll(){
		ResourceLineAssignment.getRepository().executeUpdate("DELETE FROM ResourceLineAssignment", new Object[]{});
	}
	
	/**
	 * 创建资源的垂直关系
	 * @param parent	父资源
	 * @param child		子资源
	 * @return
	 */
	public static ResourceLineAssignment newResourceLineAssignment(Resource parent,Resource child){
	    ResourceLineAssignment assignment = null;
        List<ResourceLineAssignment> resources = Resource.getRepository().find("select r from ResourceLineAssignment r where r.parent = ? and r.child = ?", new Object[]{parent,child}, ResourceLineAssignment.class);
        if (resources != null && resources.size() > 0) {
            assignment = resources.get(0);
        }
        
        if (assignment == null) {
		    assignment  = new ResourceLineAssignment();
		    assignment.setParent(parent);
		    assignment.setChild(child);
		    assignment.setCreateDate(new Date());
		    assignment.setAbolishDate(DateUtils.MAX_DATE);
        }
	    return assignment;
	}

}
