
package org.openkoala.jbpm.application.core;

import java.util.List;
import com.dayatang.querychannel.support.Page;
import org.openkoala.jbpm.application.vo.*;

public interface JoinAssignApplication {

	public JoinAssignVO getJoinAssign(Long id);
	
	public JoinAssignVO saveJoinAssign(JoinAssignVO joinAssign);
	
	public void updateJoinAssign(JoinAssignVO joinAssign);
	
	public void removeJoinAssign(Long id);
	
	public void removeJoinAssigns(Long[] ids);
	
	public List<JoinAssignVO> findAllJoinAssign();
	
	public Page<JoinAssignVO> pageQueryJoinAssign(JoinAssignVO joinAssign, int currentPage, int pageSize);
	
	public JoinAssignVO getJoinAssignByName(String name);

}

