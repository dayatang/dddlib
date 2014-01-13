package org.openkoala.cas.casmanagement.application.impl;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import org.openkoala.auth.application.UserApplication;
import org.openkoala.auth.application.vo.UserVO;
import org.openkoala.cas.casmanagement.application.CasUserApplication;
import org.openkoala.koala.auth.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Named
@Transactional
public class CasUserApplicationImpl implements CasUserApplication {
	
	@Inject
	private UserApplication userApplication;
	
	@Resource
	private PasswordEncoder passwordEncoder;

	public void setUserApplication(UserApplication userApplication) {
		this.userApplication = userApplication;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public UserVO createUser(UserVO user) {
		user.setValid(true);
		user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
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
		userVO.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
		userApplication.updatePassword(userVO, passwordEncoder.encode(user.getOldPassword()));
		return userApplication.getUser(id);
	}

	public Response removeUser(Long id) {
		userApplication.removeUser(id);
		return Response.ok().build();
	}

	@Override
	public boolean isUserValid(String username, String password) {
		UserVO userVO = userApplication.findByUserAccount(username);
		
		if (userVO == null) {
			return false;
		}
		
		if(passwordEncoder.encode(password).equals(userVO.getUserPassword())){
			return true;
		}
		
		return false;
	}

	@Override
	public String getEmail(String useraccount) {
		UserVO userVO = userApplication.findByUserAccount(useraccount);
		return userVO.getEmail();
	}

}
