package org.openkoala.jbpm.core.jbpm.action.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.apache.struts2.ServletActionContext;
import org.openkoala.auth.application.RoleApplication;
import org.openkoala.auth.application.UserApplication;
import org.openkoala.auth.application.vo.QueryConditionVO;
import org.openkoala.auth.application.vo.QueryItemVO;
import org.openkoala.auth.application.vo.RoleVO;
import org.openkoala.auth.application.vo.UserVO;
import org.openkoala.koala.auth.ss3adapter.CustomUserDetails;
import org.openkoala.koala.auth.ss3adapter.SecurityMD5;
import org.openkoala.koala.auth.ss3adapter.ehcache.CacheUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import com.dayatang.querychannel.support.Page;
import com.opensymphony.xwork2.ActionSupport;

public class UserAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private UserApplication userApplication;
	@Inject
	private RoleApplication roleApplication;
	private Map<String, Object> dataMap = new HashMap<String, Object>();
	private List<UserVO> users = new ArrayList<UserVO>();
	private String page;
	private String pagesize;
	private UserVO userVO;
	private String userAccount;
	private List<RoleVO> roles = new ArrayList<RoleVO>();
	private Long roleId;
	private QueryConditionVO search;
	private String userNameForSearch;
	private String userAccountForSearch;
	private String oldPassword;

	public UserAction() {
		dataMap = new HashMap<String, Object>();
		search = new QueryConditionVO();
	}

	public UserApplication getUserApplication() {
		return userApplication;
	}

	public void setUserApplication(UserApplication userApplication) {
		this.userApplication = userApplication;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public List<UserVO> getUsers() {
		return users;
	}

	public void setUsers(List<UserVO> users) {
		this.users = users;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getPagesize() {
		return pagesize;
	}

	public void setPagesize(String pagesize) {
		this.pagesize = pagesize;
	}

	public UserVO getUserVO() {
		return userVO;
	}

	public void setUserVO(UserVO userVO) {
		this.userVO = userVO;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public List<RoleVO> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleVO> roles) {
		this.roles = roles;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getUserNameForSearch() {
		return userNameForSearch;
	}

	public void setUserNameForSearch(String userNameForSearch) {
		this.userNameForSearch = userNameForSearch;
	}

	public String getUserAccountForSearch() {
		return userAccountForSearch;
	}

	public void setUserAccountForSearch(String userAccountForSearch) {
		this.userAccountForSearch = userAccountForSearch;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String login() {
		dataMap.clear();
		if (userVO.getUserAccount().equals("admin") && //
				userVO.getUserPassword().equals("1")) {
			dataMap.put("result", "success");
			ServletActionContext.getRequest().getSession() //
					.setAttribute("username", userVO.getUserAccount());
		} else {
			dataMap.put("result", "fail");
		}
		return "JSON";
	}

	public String logOut() {
		return "LOGOUT";
	}

	public String list() {
		return "METHOD";
	}

	public String query() {
		int start = Integer.parseInt(this.page);
		int limit = Integer.parseInt(this.pagesize);
		initSearchCondition();
		Page<UserVO> all = userApplication.pageQueryUserCustom(start, limit, search);
		if(all!=null){
			dataMap.put("Rows", all.getResult());
			dataMap.put("start", start * limit - limit);
			dataMap.put("limit", limit);
			dataMap.put("Total", all.getTotalCount());
		}
		return "JSON";
	}

	public String pageJson() {
		int start = Integer.parseInt(this.page);
		int limit = Integer.parseInt(this.pagesize);

		Page<UserVO> all = null;
		if (roleId == null) {
			all = userApplication.pageQueryUser(start, limit);
		} else {
			RoleVO roleVoForFind = new RoleVO();
			roleVoForFind.setId(roleId);
			try {
				all = new Page<UserVO>(start, limit, limit, roleApplication.findUserByRole(roleVoForFind));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		dataMap.put("Rows", all.getResult());
		dataMap.put("start", start * limit - limit);
		dataMap.put("limit", limit);
		dataMap.put("Total", all.getTotalCount());
		return "JSON";
	}

	public String queryNotAssignUserByRole() {
		int start = Integer.parseInt(this.page);
		int limit = Integer.parseInt(this.pagesize);

		RoleVO roleVoForFind = new RoleVO();
		roleVoForFind.setId(roleId);
		Page<UserVO> all = roleApplication.pageQueryNotAssignUserByRole(start, limit, roleVoForFind);

		dataMap.put("Rows", all.getResult());
		dataMap.put("start", start * limit - limit);
		dataMap.put("limit", limit);
		dataMap.put("Total", all.getTotalCount());
		return "JSON";
	}

	public String queryUsersForAssign() {
		int start = Integer.parseInt(this.page);
		int limit = Integer.parseInt(this.pagesize);

		initSearchCondition();

		Page<UserVO> all = userApplication.pageQueryUserCustom(start, limit, search);

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
		dataMap.put("start", start * limit - limit);
		dataMap.put("limit", limit);
		dataMap.put("Total", all.getTotalCount());

		return "JSON";
	}

	private void initSearchCondition() {
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

	public String add() {
		userVO.setSerialNumber("0");
		userVO.setUserPassword(SecurityMD5.encode(userVO.getUserPassword(), //
				userVO.getUserAccount()));
		userApplication.saveUser(userVO);
		CacheUtil.refreshUserAttributes(userVO.getUserAccount());
		dataMap.put("result", "success");
		userVO = null;
		return "JSON";
	}

	public String del() {
		for (UserVO user : users) {
			this.userApplication.removeUser(Long.valueOf(user.getId()));
			CacheUtil.removeUserFromCache(user.getUserAccount());
		}
		dataMap.put("result", "success");
		userVO = null;
		return "JSON";
	}

	public String update() {
		this.userApplication.updateUser(userVO);
		dataMap.put("result", "success");
		return "JSON";
	}

	public String getUserRoles() {
		dataMap.put("userRoles", this.roleApplication.findRoleByUserAccount(userAccount));
		dataMap.put("result", "success");
		return "JSON";
	}

	public String assignRoles() {
		this.userApplication.assignRole(userVO, roles);
		CacheUtil.refreshUserAttributes(((UserVO) userApplication.getUser(userVO.getId())).getUserAccount());
		dataMap.put("result", "success");
		return "JSON";
	}

	/**
	 * 删除某个角色下的某个用户
	 * 
	 * @return
	 */
	public String removeUserForRole() {
		RoleVO roleVO = new RoleVO();
		roleVO.setId(roleId);
		roleApplication.abolishUser(roleVO, users);
		for (UserVO userVO : users) {
			CacheUtil.refreshUserAttributes(((UserVO) userApplication.getUser(userVO.getId())).getUserAccount());
		}
		dataMap.put("result", "success");
		return "JSON";
	}

	/**
	 * 修改用户密码
	 * 
	 * @return
	 */
	public String updatePassword() {
		CustomUserDetails current = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String username = current.getUsername();
		if (current.isSuper()) {
			dataMap.put("result", "超级管理员密码不支持页面修改");
			return "JSON";
		}
		userVO.setUserAccount(username);
		userVO.setUserPassword(SecurityMD5.encode(userVO.getUserPassword(), username));
		if (userApplication.updatePassword(userVO, SecurityMD5.encode(oldPassword, username))) {
			dataMap.put("result", "success");
			CacheUtil.refreshUserAttributes(username);
		} else {
			dataMap.put("result", "failure");
		}
		return "JSON";
	}

}
