
package org.openkoala.jbpm.application.core;

import java.util.List;
import com.dayatang.querychannel.support.Page;
import org.openkoala.jbpm.application.vo.*;

public interface KoalaAssignInfoApplication {

	public KoalaAssignInfoVO getKoalaAssignInfo(Long id);
	
	public KoalaAssignInfoVO saveKoalaAssignInfo(KoalaAssignInfoVO koalaAssignInfo);
	
	public void updateKoalaAssignInfo(KoalaAssignInfoVO koalaAssignInfo);
	
	public void removeKoalaAssignInfo(Long id);
	
	public void removeKoalaAssignInfos(Long[] ids);
	
	public List<KoalaAssignInfoVO> findAllKoalaAssignInfo();
	
	public Page<KoalaAssignInfoVO> pageQueryKoalaAssignInfo(KoalaAssignInfoVO koalaAssignInfo, int currentPage, int pageSize);
	


			
	public Page<KoalaAssignDetailVO> findJbpmNamesByKoalaAssignInfo(Long id, int currentPage, int pageSize);		
	
}

