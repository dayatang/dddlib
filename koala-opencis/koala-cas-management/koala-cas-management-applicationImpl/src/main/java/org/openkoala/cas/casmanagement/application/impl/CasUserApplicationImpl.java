package org.openkoala.cas.casmanagement.application.impl;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openkoala.auth.application.UserApplication;
import org.openkoala.auth.application.vo.UserVO;
import org.openkoala.cas.casmanagement.application.CasUserApplication;
import org.openkoala.cas.casmanagement.application.dto.UserDTO;
import org.openkoala.cas.casmanagement.infra.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Named
@Transactional
public class CasUserApplicationImpl implements CasUserApplication {
	
	@Inject
	private UserApplication userApplication;

	public Response createUser(UserDTO user) {
		UserVO userVO = new UserVO();
		userVO.setUserAccount(user.getUserAccount());
		userVO.setName(user.getUsername());
		userVO.setUserDesc(user.getUserDesc());
		userVO.setEmail(user.getEmail());
		userVO.setValid(true);
		userVO.setUserPassword(new PasswordEncoder("", "MD5").encode(user.getPassword()));
		userApplication.saveUser(userVO);
		return Response.ok("success").type(MediaType.TEXT_PLAIN).build();
	}

	public Response updateUser(long id, UserDTO user) {
		UserVO userVO = userApplication.getUser(id);
		userVO.setName(user.getUsername());
		userVO.setUserDesc(user.getUserDesc());
		userApplication.updateUser(userVO);
		return Response.ok("success").type(MediaType.TEXT_PLAIN).build();
	}

	public Response enabled(Long id) {
		isEnabled(id, true);
		return Response.ok("success").type(MediaType.TEXT_PLAIN).build();
	}

	private void isEnabled(Long id, boolean isEnabled) {
		UserVO userVO = userApplication.getUser(id);
		userVO.setValid(isEnabled);
		userApplication.updateUser(userVO);
	}

	public Response disabled(Long id) {
		isEnabled(id, false);
		return Response.ok("success").type(MediaType.TEXT_PLAIN).build();
	}

	public UserDTO getUser(Long id) {
		UserVO userVO = userApplication.getUser(id);
		UserDTO result = new UserDTO();
		result.setUsername(userVO.getUserAccount());
		result.setUserDesc(userVO.getUserDesc());
		return result;
	}

	public Response modifyPassword(long id, UserDTO user) {
		UserVO userVO = userApplication.getUser(id);
		userVO.setUserPassword(new PasswordEncoder("", "MD5").encode(user.getPassword()));
		userApplication.updatePassword(userVO, new PasswordEncoder("", "MD5").encode(user.getOldPassword()));
		return Response.ok("success").type(MediaType.TEXT_PLAIN).build();
	}

}
