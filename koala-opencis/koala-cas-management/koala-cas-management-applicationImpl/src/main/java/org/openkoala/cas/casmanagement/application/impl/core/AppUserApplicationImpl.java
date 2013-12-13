
package org.openkoala.cas.casmanagement.application.impl.core;

import java.util.List;
import java.util.ArrayList;
import java.text.MessageFormat;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import com.dayatang.querychannel.support.Page;
import com.dayatang.querychannel.service.QueryChannelService;
import org.openkoala.cas.casmanagement.application.dto.*;
import org.openkoala.cas.casmanagement.application.core.AppUserApplication;
import org.openkoala.cas.casmanagement.core.*;

@Named
@Transactional
public class AppUserApplicationImpl implements AppUserApplication {

	@Inject
	private QueryChannelService queryChannel;
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public AppUserDTO getAppUser(Long id) {
		AppUser appUser = AppUser.load(AppUser.class, id);
		AppUserDTO appUserDTO = new AppUserDTO();
		appUserDTO.domain2Dto(appUser);
		appUserDTO.setId(appUser.getId());
		return appUserDTO;
	}
	
	public AppUserDTO saveAppUser(AppUserDTO appUserDTO) {
		AppUser appUser = new AppUser();
		appUserDTO.dto2Domain(appUser);
		appUser.save();
		appUserDTO.setId(appUser.getId());
		return appUserDTO;
	}
	
	public void updateAppUser(AppUserDTO appUserDTO) {
		AppUser appUser = AppUser.get(AppUser.class, appUserDTO.getId());
		appUserDTO.dto2Domain(appUser);
	}
	
	public void removeAppUser(Long id) {
		this.removeAppUsers(new Long[] { id });
	}
	
	public void removeAppUsers(Long[] ids) {
		for (int i = 0; i < ids.length; i++) {
			AppUser appUser = AppUser.load(AppUser.class, ids[i]);
			appUser.remove();
		}
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<AppUserDTO> findAllAppUser() {
		List<AppUserDTO> list = new ArrayList<AppUserDTO>();
		List<AppUser> all = AppUser.findAll(AppUser.class);
		for (AppUser appUser : all) {
			AppUserDTO appUserDTO = new AppUserDTO();
			appUserDTO.domain2Dto(appUser);
			list.add(appUserDTO);
		}
		return list;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Page<AppUserDTO> pageQueryAppUser(AppUserDTO queryVo, int currentPage, int pageSize) {
		List<AppUserDTO> result = new ArrayList<AppUserDTO>();
		List<Object> conditionVals = new ArrayList<Object>();
	   	StringBuilder jpql = new StringBuilder("select _appUser from AppUser _appUser   where 1=1 ");
	
	   	if (queryVo.getUsername() != null && !"".equals(queryVo.getUsername())) {
	   		jpql.append(" and _appUser.username like ?");
	   		conditionVals.add(MessageFormat.format("%{0}%", queryVo.getUsername()));
	   	}		
	
	   	if (queryVo.getPassword() != null && !"".equals(queryVo.getPassword())) {
	   		jpql.append(" and _appUser.password like ?");
	   		conditionVals.add(MessageFormat.format("%{0}%", queryVo.getPassword()));
	   	}		
	
	   	if (queryVo.getEmail() != null && !"".equals(queryVo.getEmail())) {
	   		jpql.append(" and _appUser.email like ?");
	   		conditionVals.add(MessageFormat.format("%{0}%", queryVo.getEmail()));
	   	}		
	
        Page<AppUser> pages = queryChannel.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), currentPage, pageSize);
        for (AppUser appUser : pages.getResult()) {
            AppUserDTO appUserDTO = new AppUserDTO();
            appUserDTO.domain2Dto(appUser);
            result.add(appUserDTO);
        }
        return new Page<AppUserDTO>(pages.getCurrentPageNo(), pages.getTotalCount(), pages.getPageSize(), result);
	}
	
	public void enabled(long id) {
		
	}
	
	public void disable(long id) {
		
	}
}
