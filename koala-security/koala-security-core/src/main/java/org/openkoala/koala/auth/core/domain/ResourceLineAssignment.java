package org.openkoala.koala.auth.core.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.dayatang.utils.DateUtils;

@Entity
@Table(name = "KS_RESOURCE_LINEASSIGNMENT")
public class ResourceLineAssignment extends Accountability {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "PARENT_ID", nullable = false)
	private Resource parent;

	@ManyToOne
	@JoinColumn(name = "CHILD_ID", nullable = false)
	private Resource child;

	public static List<ResourceLineAssignment> findRelationByResource(Long resourceId) {
		Object[] params = new Object[] { resourceId, resourceId };
		return ResourceLineAssignment.findByNamedQuery("findRelationByResource", params, ResourceLineAssignment.class);
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
	
	public static void removeAll(){
		String sql = "DELETE FROM ResourceLineAssignment";
		ResourceLineAssignment.getRepository().executeUpdate(sql, new Object[]{});
	}
	
	public static ResourceLineAssignment newResourceLineAssignment(Resource parent,Resource child){
	    ResourceLineAssignment assignment = null;
        List<ResourceLineAssignment> resources = Resource.getRepository().find("select r from ResourceLineAssignment r where r.parent = ? and r.child = ?", new Object[]{parent,child}, ResourceLineAssignment.class);
        if(resources!=null && resources.size()>0){
            assignment = resources.get(0);
        }
        
        if(assignment==null){
		    assignment  = new ResourceLineAssignment();
		    assignment.setParent(parent);
		    assignment.setChild(child);
	    assignment.setCreateDate(new Date());
	    assignment.setAbolishDate(DateUtils.MAX_DATE);
        }
	    return assignment;
	}

}
