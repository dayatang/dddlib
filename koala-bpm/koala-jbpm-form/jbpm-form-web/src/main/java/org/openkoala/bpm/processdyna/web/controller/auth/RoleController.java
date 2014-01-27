package org.openkoala.bpm.processdyna.web.controller.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.openkoala.auth.application.ResourceApplication;
import org.openkoala.auth.application.RoleApplication;
import org.openkoala.auth.application.UserApplication;
import org.openkoala.auth.application.vo.QueryConditionVO;
import org.openkoala.auth.application.vo.QueryItemVO;
import org.openkoala.auth.application.vo.ResourceVO;
import org.openkoala.auth.application.vo.RoleVO;
import org.openkoala.auth.application.vo.UserVO;
import org.openkoala.koala.auth.ss3adapter.ehcache.CacheUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayatang.querychannel.support.Page;

@Controller
@RequestMapping("/auth/Role")
public class RoleController {
	
	@Inject
	private RoleApplication roleApplication;

	@Inject
	private ResourceApplication urlApplication;

	@Inject
	private UserApplication userApplication;
	
	@RequestMapping("/list")
	public String list(Long userId, String userAccount, ModelMap modelMap) {
		modelMap.addAttribute("userId", userId);
		modelMap.addAttribute("userAccount", userAccount);
		return "auth/Role-list";
	}
	
	@ResponseBody
	@RequestMapping("/abolishResource")
	public Map<String, Object> abolishResource(ParamsPojo params,RoleVO roleVO,List<ResourceVO> menus) {
		Map<String, Object> dataMap = new HashMap<String,Object>();
		roleApplication.abolishMenu(roleVO, menus);
		for (ResourceVO mv : menus) {
			CacheUtil.refreshUrlAttributes(mv.getIdentifier());
		}
		dataMap.put("result", "success");
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/queryUrlResourceByRole")
	public Map<String, Object> queryUrlResourceByRole(ParamsPojo params) {
		RoleVO roleVO = params.getRoleVO();
		Map<String, Object> dataMap = new HashMap<String,Object>();
		List<ResourceVO> all = roleApplication.findResourceByRole(roleVO);
		dataMap.put("data", all);

		return dataMap;

	}

	@ResponseBody
	@RequestMapping("/queryUserByRole")
	public Map<String, Object> queryUserByRole(ParamsPojo params) {
		RoleVO roleVO = params.getRoleVO();
		Map<String, Object> dataMap = new HashMap<String,Object>();
		List<UserVO> all = roleApplication.findUserByRole(roleVO);
		dataMap.put("data", all);
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/queryMenuByRole")
	public Map<String, Object> queryMenuByRole(ParamsPojo params) {
		RoleVO roleVO = params.getRoleVO();
		Map<String, Object> dataMap = new HashMap<String,Object>();
		List<ResourceVO> all = roleApplication.findMenuByRole(roleVO);
		dataMap.put("data", all);
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/pageJson")
	public Map<String, Object> pageJson(String page,String pagesize,String userAccount) {
		Map<String, Object> dataMap = new HashMap<String,Object>();
		int start = Integer.parseInt(page);
		int limit = Integer.parseInt(pagesize);
		Page<RoleVO> all = null;
		if (userAccount == null || userAccount.isEmpty()) {
			all = roleApplication.pageQueryRole(start, limit);
		} else {
			all = new Page<RoleVO>(start, limit, limit, roleApplication.findRoleByUserAccount(userAccount));
		}
		dataMap.put("Rows", all.getResult());
		dataMap.put("start", start * limit - limit);
		dataMap.put("limit", limit);
		dataMap.put("Total", all.getTotalCount());

		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/query")
	public Map<String, Object> query(String page,String pagesize,String roleNameForSearch) {
		Map<String, Object> dataMap = new HashMap<String,Object>();
		int start = Integer.parseInt(page);
		int limit = Integer.parseInt(pagesize);
		QueryConditionVO search = new QueryConditionVO();
		initSearchCondition(search,roleNameForSearch);

		Page<RoleVO> all = roleApplication.pageQueryByRoleCustom(start, limit, search);
		dataMap.put("Rows", all.getResult());
		dataMap.put("start", start * limit - limit);
		dataMap.put("limit", limit);
		dataMap.put("Total", all.getTotalCount());
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/queryRolesForAssign")
	public Map<String, Object> queryRolesForAssign(String page,String pagesize,Long userId,String userAccount,String roleNameForSearch) {
		Map<String, Object> dataMap = new HashMap<String,Object>();
		int start = Integer.parseInt(page);
		int limit = Integer.parseInt(pagesize);
		QueryConditionVO search = new QueryConditionVO();
		initSearchCondition(search,roleNameForSearch);

		Page<RoleVO> all = roleApplication.pageQueryByRoleCustom(start, limit, search);

		if (userId != null) {
			List<RoleVO> roles = all.getResult();
			for (RoleVO role : roleApplication.findRoleByUserAccount(userAccount)) {
				roles.remove(role);
			}
		}

		dataMap.put("Rows", all.getResult());
		dataMap.put("start", start * limit - limit);
		dataMap.put("limit", limit);
		dataMap.put("Total", all.getTotalCount());

		return dataMap;
	}
	
	private void initSearchCondition(QueryConditionVO search,String roleNameForSearch) {
		search.setObjectName("Role");
		List<QueryItemVO> searchConditions = new ArrayList<QueryItemVO>();
		if (roleNameForSearch != null) {
			if (!roleNameForSearch.isEmpty()) {
				QueryItemVO queryItem = new QueryItemVO();
				queryItem.setPropName("name");

				queryItem.setPropValue("'%" + roleNameForSearch + "%'");

				queryItem.setOperaType(QueryConditionVO.LIKE);
				searchConditions.add(queryItem);
			}
		}
		search.setItems(searchConditions);
	}

	@ResponseBody
	@RequestMapping("/queryNotAssignRoleByUser")
	public Map<String, Object> queryNotAssignRoleByUser(String page,String pagesize,long userId) {
		Map<String, Object> dataMap = new HashMap<String,Object>();
		int start = Integer.parseInt(page);
		int limit = Integer.parseInt(pagesize);

		UserVO userVoForFind = new UserVO();
		userVoForFind.setId(userId);
		Page<RoleVO> all = userApplication.pageQueryNotAssignRoleByUser(start, limit, userVoForFind);

		dataMap.put("Rows", all.getResult());
		dataMap.put("start", start * limit - limit);
		dataMap.put("limit", limit);
		dataMap.put("Total", all.getTotalCount());

		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/add")
	public Map<String, Object> add(ParamsPojo params) {
		RoleVO roleVO = params.getRoleVO();
		Map<String, Object> dataMap = new HashMap<String,Object>();
		roleVO.setSerialNumber("test");
		roleApplication.saveRole(roleVO);
		dataMap.put("result", "success");
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/del")
	public Map<String, Object> del(ParamsPojo params) {
		List<RoleVO> roles = params.getRoles();
		Map<String, Object> dataMap = new HashMap<String,Object>();
		for (RoleVO role : roles) {
			roleApplication.removeRole(Long.valueOf(role.getId()));
		}
		dataMap.put("result", "success");
		return dataMap;

	}

	@ResponseBody
	@RequestMapping("/update")
	public Map<String, Object> update(ParamsPojo params) {
		RoleVO roleVO = params.getRoleVO();
		Map<String, Object> dataMap = new HashMap<String,Object>();
		roleApplication.updateRole(roleVO);
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 为角色分配菜单资源
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/assignMenuResources")
	public Map<String, Object> assignMenuResources(ParamsPojo params) {
		RoleVO roleVO = params.getRoleVO();
		List<ResourceVO> menus = params.getMenus();
		Map<String, Object> dataMap = new HashMap<String,Object>();
		// 原先拥有的菜单资源
		List<ResourceVO> ownMenus = roleApplication.findAllResourceByRole(roleVO);
		// 勾选中的菜单资源
		List<ResourceVO> tmpList = new ArrayList<ResourceVO>(menus);
		List<ResourceVO> addList = new ArrayList<ResourceVO>();
		List<ResourceVO> delList = new ArrayList<ResourceVO>();
		// 得到相到的菜单资源
		menus.retainAll(ownMenus);
		// 去掉相同的菜单资源
		ownMenus.removeAll(menus);
		// 得到被删除的菜单资源
		delList.addAll(ownMenus);
		// 得到被新添加的菜单资源
		tmpList.removeAll(menus);

		addList.addAll(tmpList);
		roleApplication.abolishMenu(roleVO, delList);
		roleApplication.assignMenuResource(roleVO, addList);
		for (ResourceVO mVO : addList) {
			CacheUtil.refreshUrlAttributes(((ResourceVO) urlApplication.getResource(mVO.getId())).getIdentifier());
		}
		for (ResourceVO mVO : delList) {
			CacheUtil.refreshUrlAttributes(((ResourceVO) urlApplication.getResource(mVO.getId())).getIdentifier());
		}
		dataMap.put("result", "success");

		return dataMap;
	}

	/**
	 * 为角色分配资源
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/assignResources")
	public Map<String, Object> assignResources(ParamsPojo params) {
		RoleVO roleVO = params.getRoleVO();
		List<ResourceVO> addList = params.getAddList();
		List<ResourceVO> delList = params.getDelList();
		Map<String, Object> dataMap = new HashMap<String,Object>();
		if (addList != null && addList.size() > 0) {
			roleApplication.assignMenuResource(roleVO, addList);
		}
		if (delList != null && delList.size() > 0) {
			roleApplication.abolishMenu(roleVO, delList);
		}
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 为角色分配URL资源
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/assignUrlResources")
	public Map<String, Object> assignUrlResources(ParamsPojo params) {
		RoleVO roleVO = params.getRoleVO();
		List<ResourceVO> menus = params.getMenus();
		Map<String, Object> dataMap = new HashMap<String,Object>();
		roleApplication.assignMenuResource(roleVO, menus);
		for (ResourceVO mVO : menus) {
			CacheUtil.refreshUrlAttributes(mVO.getIdentifier());
		}
		dataMap.put("result", "success");

		return dataMap;
	}

	/**
	 * 查询某个角色下的所有用户
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findUsersByRole")
	public Map<String, Object> findUsersByRole(ParamsPojo params) {
		RoleVO roleVO = params.getRoleVO();
		Map<String, Object> dataMap = new HashMap<String,Object>();
		dataMap.put("roleUsers", roleApplication.findUserByRole(roleVO));
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 给角色分配用户
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/assignUsers")
	public Map<String, Object> assignUsers(ParamsPojo params) {
		RoleVO roleVO = params.getRoleVO();
		List<UserVO> users = params.getUsers();
		Map<String, Object> dataMap = new HashMap<String,Object>();
		for (UserVO user : users) {
			roleApplication.assignUser(roleVO, user);
			CacheUtil.refreshUserAttributes(((UserVO) userApplication.getUser(user.getId())).getUserAccount());
		}
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 返回角色所拥有的资源页面
	 * 
	 * @return
	 */
	@RequestMapping("/resourceList")
	public String resourceList() {
		return "Role-resourceList";
	}

	/**
	 * 删除某个用户下的某个角色
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/removeRoleForUser")
	public Map<String, Object> removeRoleForUser(ParamsPojo params) {
		long userId = params.getUserId();
		List<RoleVO> roles = params.getRoles();
		Map<String, Object> dataMap = new HashMap<String,Object>();
		UserVO user = new UserVO();
		user.setId(userId);
		userApplication.abolishRole(user, roles);
		CacheUtil.refreshUserAttributes(((UserVO) userApplication.getUser(user.getId())).getUserAccount());
		dataMap.put("result", "success");
		return dataMap;
	}

}
