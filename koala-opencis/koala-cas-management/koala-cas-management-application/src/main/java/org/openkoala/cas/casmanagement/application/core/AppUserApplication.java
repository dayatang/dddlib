
package org.openkoala.cas.casmanagement.application.core;

import java.util.List;
import com.dayatang.querychannel.support.Page;
import org.openkoala.cas.casmanagement.application.dto.*;

public interface AppUserApplication {

	AppUserDTO getAppUser(Long id);
	
	AppUserDTO saveAppUser(AppUserDTO appUser);
	
	void updateAppUser(AppUserDTO appUser);
	
	void removeAppUser(Long id);
	
	void removeAppUsers(Long[] ids);
	
	List<AppUserDTO> findAllAppUser();
	
	Page<AppUserDTO> pageQueryAppUser(AppUserDTO appUser, int currentPage, int pageSize);
	
	void enabled(long id);
	
	void disable(long id); 

}

