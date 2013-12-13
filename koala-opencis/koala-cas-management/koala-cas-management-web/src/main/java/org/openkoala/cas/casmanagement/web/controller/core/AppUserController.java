
package org.openkoala.cas.casmanagement.web.controller.core;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import com.dayatang.querychannel.support.Page;
import org.openkoala.cas.casmanagement.application.core.AppUserApplication;
import org.openkoala.cas.casmanagement.application.dto.*;

@Controller
@RequestMapping("/AppUser")
public class AppUserController {
		
	@Inject
	private AppUserApplication appUserApplication;
	
	@ResponseBody
	@RequestMapping("/add")
	public Map<String, Object> add(AppUserDTO appUserDTO) {
		Map<String, Object> result = new HashMap<String, Object>();
		appUserApplication.saveAppUser(appUserDTO);
		result.put("result", "success");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/update")
	public Map<String, Object> update(AppUserDTO appUserDTO) {
		Map<String, Object> result = new HashMap<String, Object>();
		appUserApplication.updateAppUser(appUserDTO);
		result.put("result", "success");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/pageJson")
	public Map<String, Object> pageJson(AppUserDTO appUserDTO, @RequestParam int page, @RequestParam int pagesize) {
		Map<String, Object> result = new HashMap<String, Object>();
		Page<AppUserDTO> all = appUserApplication.pageQueryAppUser(appUserDTO, page, pagesize);
		result.put("Rows", all.getResult());
		result.put("start", page * pagesize - pagesize);
		result.put("limit", pagesize);
		result.put("Total", all.getTotalCount());
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/delete")
	public Map<String, Object> delete(@RequestParam String ids) {
		Map<String, Object> result = new HashMap<String, Object>();
		String[] value = ids.split(",");
        Long[] idArrs = new Long[value.length];
        for (int i = 0; i < value.length; i ++) {
        	idArrs[i] = Long.parseLong(value[i]);
        }
        appUserApplication.removeAppUsers(idArrs);
		result.put("result", "success");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/get/{id}")
	public Map<String, Object> get(@PathVariable long id) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", appUserApplication.getAppUser(id));
		return result;
	}
	
		
}