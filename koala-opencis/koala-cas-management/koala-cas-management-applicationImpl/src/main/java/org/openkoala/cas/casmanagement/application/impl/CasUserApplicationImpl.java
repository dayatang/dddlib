package org.openkoala.cas.casmanagement.application.impl;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import org.openkoala.auth.application.UserApplication;
import org.openkoala.auth.application.vo.UserVO;
import org.openkoala.cas.casmanagement.application.CasUserApplication;
import org.openkoala.cas.casmanagement.infra.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Named
@Transactional
public class CasUserApplicationImpl implements CasUserApplication {
	
	@Inject
	private UserApplication userApplication;

	public UserVO createUser(UserVO user) {
		user.setValid(true);
		user.setUserPassword(new PasswordEncoder("", "MD5").encode(user.getUserPassword()));
		UserVO result = userApplication.saveUser(user);
		return result;
	}

	public UserVO updateUser(long id, UserVO user) {
		UserVO userVO = userApplication.getUser(id);
		userVO.setName(user.getName());
		userVO.setUserDesc(user.getUserDesc());
		userApplication.updateUser(userVO);
		return userVO;
	}

	public UserVO enabled(Long id) {
		isEnabled(id, true);
		return userApplication.getUser(id);
	}

	private void isEnabled(Long id, boolean isEnabled) {
		UserVO userVO = userApplication.getUser(id);
		userVO.setValid(isEnabled);
		userApplication.updateUser(userVO);
	}

	public UserVO disabled(Long id) {
		isEnabled(id, false);
		return userApplication.getUser(id);
	}

	public UserVO getUser(Long id) {
		return userApplication.getUser(id);
	}

	public UserVO modifyPassword(Long id, UserVO user) {
		UserVO userVO = userApplication.getUser(id);
		userVO.setUserPassword(new PasswordEncoder("", "MD5").encode(user.getUserPassword()));
		userApplication.updatePassword(userVO, new PasswordEncoder("", "MD5").encode(user.getOldPassword()));
		return userApplication.getUser(id);
	}

	public Response removeUser(Long id) {
		userApplication.removeUser(id);
		return Response.ok().build();
	}

	@Override
	public boolean isUserValid(String username, String password) {
		// TODO TEST CODE
		if(username.equals(password)){
			return true;
		}
		return false;
	}

}
