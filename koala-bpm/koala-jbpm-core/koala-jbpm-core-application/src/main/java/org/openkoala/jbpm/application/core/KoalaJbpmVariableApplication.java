
package org.openkoala.jbpm.application.core;

import java.util.List;
import com.dayatang.querychannel.support.Page;
import org.openkoala.jbpm.application.vo.*;

public interface KoalaJbpmVariableApplication {

	public KoalaJbpmVariableVO getKoalaJbpmVariable(Long id);
	
	public KoalaJbpmVariableVO saveKoalaJbpmVariable(KoalaJbpmVariableVO koalaJbpmVariable);
	
	public void updateKoalaJbpmVariable(KoalaJbpmVariableVO koalaJbpmVariable);
	
	public void removeKoalaJbpmVariable(Long id);
	
	public void removeKoalaJbpmVariables(Long[] ids);
	
	public List<KoalaJbpmVariableVO> findAllKoalaJbpmVariable();
	
	public Page<KoalaJbpmVariableVO> pageQueryKoalaJbpmVariable(KoalaJbpmVariableVO koalaJbpmVariable, int currentPage, int pageSize);
	

}

