package org.openkoala.bpm.processdyna.web.controller.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import org.openkoala.auth.application.RoleApplication;
import org.openkoala.auth.application.UserApplication;
import org.openkoala.auth.application.vo.QueryConditionVO;
import org.openkoala.auth.application.vo.QueryItemVO;
import org.openkoala.auth.application.vo.RoleVO;
import org.openkoala.auth.application.vo.UserVO;
import org.openkoala.koala.auth.password.PasswordEncoder;
import org.openkoala.koala.auth.ss3adapter.CustomUserDetails;
import org.openkoala.koala.auth.ss3adapter.ehcache.CacheUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayatang.querychannel.support.Page;

@Controller
@RequestMapping("/auth/User")
public class UserController {

	@Inject
	private UserApplication userApplication;

	@Inject
	private RoleApplication roleApplication;
	
	@Inject
	private PasswordEncoder passwordEncoder;
	
	@ResponseBody
	@RequestMapping("/updatePassword")
	public Map<String, Object> updatePassword(@RequestParam String oldPassword,@RequestParam String userPassword) {
		Map<String, Object> dataMap  = new HashMap<String, Object>();
		CustomUserDetails current = (CustomUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		String username = current.getUsername();
		UserVO userVO = new UserVO();
		userVO.setUserAccount(username);
		userVO.setUserPassword(passwordEncoder.encode(userPassword));
		if (userApplication.updatePassword(userVO, passwordEncoder.encode(oldPassword))) {
			dataMap.put("result", "success");
			CacheUtil.refreshUserAttributes(username);
		} else {
			dataMap.put("result", "failure");
		}
		return dataMap;
	}

	@RequestMapping("/logOut")
	public String logOut() {
		return "login";
	}

	@RequestMapping("/list")
	public String list(Long roleId, ModelMap modelMap) {
		modelMap.addAttribute("roleId", roleId);
		return "auth/User-list";
	}

	@ResponseBody
	@RequestMapping("/query")
	public Map<String, Object> query(String page, String pagesize,String userNameForSearch,String userAccountForSearch) {
		Map<String, Object> dataMap = new HashMap<String,Object>();
		int start = Integer.parseInt(page);
		int limit = Integer.parseInt(pagesize);

		QueryConditionVO search = new QueryConditionVO();
		initSearchCondition(search,userNameForSearch,userAccountForSearch);
		Page<UserVO> all = userApplication.pageQueryUserCustom(start, limit,
				search);
		dataMap.put("Rows", all.getResult());
		dataMap.put("start", start * limit - limit);
		dataMap.put("limit", limit);
		dataMap.put("Total", all.getTotalCount());
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/pageJson")
	public Map<String, Object> pageJson(int page, int pagesize, Long roleId, 
			String userNameForSearch, String userAccountForSearch) {
		Map<String, Object> dataMap = new HashMap<String,Object>();
		
		RoleVO roleVoForFind = new RoleVO();
		roleVoForFind.setId(roleId);
		roleVoForFind.setName(userNameForSearch);
		roleVoForFind.setUseraccount(userAccountForSearch);
		
		Page<UserVO> all = null;
		
		if (roleId == null) {
			all = userApplication.pageQueryUser(page, pagesize);
		} else {
			all = roleApplication.pageQueryUserByRole(roleVoForFind, page, pagesize);
		}
		

		dataMap.put("Rows", all.getResult());
		dataMap.put("start", page * pagesize - pagesize);
		dataMap.put("limit", pagesize);
		dataMap.put("Total", all.getTotalCount());
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/queryNotAssignUserByRole")
	public Map<String, Object> queryNotAssignUserByRole(String page, String pagesize,Long roleId) {
		
		Map<String, Object> dataMap = new HashMap<String,Object>();
		int start = Integer.parseInt(page);
		int limit = Integer.parseInt(pagesize);

		RoleVO roleVoForFind = new RoleVO();
		roleVoForFind.setId(roleId);
		Page<UserVO> all = roleApplication.pageQueryNotAssignUserByRole(start,
				limit, roleVoForFind);

		dataMap.put("Rows", all.getResult());
		dataMap.put("start", start * limit - limit);
		dataMap.put("limit", limit);
		dataMap.put("Total", all.getTotalCount());
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/queryUsersForAssign")
	public Map<String, Object> queryUsersForAssign(int page, int pagesize,Long roleId,String userNameForSearch,String userAccountForSearch) {
		Map<String, Object> dataMap = new HashMap<String,Object>();

		QueryConditionVO search = new QueryConditionVO();
		initSearchCondition(search, userNameForSearch, userAccountForSearch);

		Page<UserVO> all = userApplication.pageQueryUserCustom(page, pagesize, search);

		if (roleId != null) {
			RoleVO role = new RoleVO();
			role.setId(roleId);
			List<UserVO> users = all.getResult();
			try {
				for (UserVO user : roleApplication.findUserByRole(role)) {
					users.remove(user);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		dataMap.put("Rows", all.getResult());
		dataMap.put("start", page * pagesize - pagesize);
		dataMap.put("limit", pagesize);
		dataMap.put("Total", all.getTotalCount());

		return dataMap;
	}

	
	private void initSearchCondition(QueryConditionVO search,String userNameForSearch,String userAccountForSearch) {
		search.setObjectName("User");
		List<QueryItemVO> searchConditions = new ArrayList<QueryItemVO>();

		if (userNameForSearch != null) {
			if (!userNameForSearch.isEmpty()) {
				QueryItemVO queryItem = new QueryItemVO();
				queryItem.setPropName("name");
				queryItem.setPropValue("'%" + userNameForSearch + "%'");
				queryItem.setOperaType(QueryConditionVO.LIKE);
				searchConditions.add(queryItem);
			}
		}

		if (userAccountForSearch != null) {
			if (!userAccountForSearch.isEmpty()) {
				QueryItemVO queryItem1 = new QueryItemVO();
				queryItem1.setPropName("userAccount");
				queryItem1.setPropValue("'%" + userAccountForSearch + "%'");
				queryItem1.setOperaType(QueryConditionVO.LIKE);
				searchConditions.add(queryItem1);
			}
		}

		search.setItems(searchConditions);
	}
	
	
	@ResponseBody
	@RequestMapping("/add")
	public Map<String, Object> add(ParamsPojo userPojo) {
		UserVO userVO = userPojo.getUserVO();
		Map<String, Object> dataMap = new HashMap<String,Object>();
		userVO.setSerialNumber("0");
		userVO.setUserPassword(passwordEncoder.encode(userVO.getUserPassword()));
		userApplication.saveUser(userVO);
		CacheUtil.refreshUserAttributes(userVO.getUserAccount());
		dataMap.put("result", "success");
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/del")
	public Map<String, Object> del(ParamsPojo userPojo) {
		List<UserVO> users = userPojo.getUsers();
		Map<String, Object> dataMap = new HashMap<String,Object>();
		for (UserVO user : users) {
			this.userApplication.removeUser(Long.valueOf(user.getId()));
			CacheUtil.removeUserFromCache(user.getUserAccount());
		}
		dataMap.put("result", "success");
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/update")
	public Map<String, Object> update(ParamsPojo userPojo) {
		UserVO userVO = userPojo.getUserVO();
		Map<String, Object> dataMap = new HashMap<String,Object>();
		this.userApplication.updateUser(userVO);
		dataMap.put("result", "success");
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/getUserRoles")
	public Map<String, Object> getUserRoles(String userAccount) {
		Map<String, Object> dataMap = new HashMap<String,Object>();
		dataMap.put("userRoles",
				this.roleApplication.findRoleByUserAccount(userAccount));
		dataMap.put("result", "success");
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/assignRoles")
	public Map<String, Object> assignRoles(ParamsPojo userPojo) {
		UserVO userVO = userPojo.getUserVO();
		List<RoleVO> roles = userPojo.getRoles();
		Map<String, Object> dataMap = new HashMap<String,Object>();
		this.userApplication.assignRole(userVO, roles);
		CacheUtil.refreshUserAttributes(((UserVO) userApplication
				.getUser(userVO.getId())).getUserAccount());
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 删除某个角色下的某个用户
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/removeUserForRole")
	public Map<String, Object> removeUserForRole(ParamsPojo params) {
		Long roleId = params.getRoleId();
		List<UserVO> users = params.getUsers();
		Map<String, Object> dataMap = new HashMap<String,Object>();
		RoleVO roleVO = new RoleVO();
		roleVO.setId(roleId);
		roleApplication.abolishUser(roleVO, users);
		for (UserVO userVO : users) {
			CacheUtil.refreshUserAttributes(((UserVO) userApplication
					.getUser(userVO.getId())).getUserAccount());
		}
		dataMap.put("result", "success");
		return dataMap;
	}

}
