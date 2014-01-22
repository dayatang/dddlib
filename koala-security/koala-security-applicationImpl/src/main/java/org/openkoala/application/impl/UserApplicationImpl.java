package org.openkoala.application.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.openkoala.exception.extend.ApplicationException;
import org.openkoala.auth.application.UserApplication;
import org.openkoala.auth.application.vo.QueryConditionVO;
import org.openkoala.auth.application.vo.RoleVO;
import org.openkoala.auth.application.vo.UserVO;
import org.openkoala.koala.auth.core.domain.Role;
import org.openkoala.koala.auth.core.domain.RoleUserAuthorization;
import org.openkoala.koala.auth.core.domain.User;
import org.springframework.transaction.annotation.Transactional;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.querychannel.support.Page;

@Remote
@Named("userApplication")
@Stateless(name = "UserApplication")
@Transactional(value="transactionManager_security")
@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
public class UserApplicationImpl extends BaseImpl implements UserApplication {

    public UserVO getUser(Long userId) {
    	User user = queryChannel().querySingleResult("select user from User user where user.abolishDate>?", new Object[] { new Date() });
    	if (user != null) {
	        UserVO userVO = new UserVO();
	        userVO.domain2Vo(user);
	        return userVO;
    	}
        return null;
    }

    public UserVO saveUser(UserVO userVO) {
		User user = new User();
		userVO.vo2Domain(user);
		
		if (user.isAccountExisted()) {
        	throw new ApplicationException("userAccount.exist", null);
        }
        
        if (user.isEmailExisted()) {
        	throw new ApplicationException("email.exist", null);
        }
        
        user.save();
        userVO.setId(user.getId());
        return userVO;
    }

    public void updateUser(UserVO userVO) {
        User user = User.load(User.class, userVO.getId());
        user.setName(userVO.getName());
        user.setUserDesc(userVO.getUserDesc());
        user.setUserAccount(userVO.getUserAccount());
        user.setValid(userVO.isValid());
    }

    public boolean updatePassword(UserVO userVO, String oldPass) {
        User user = User.findByUserAccount(userVO.getUserAccount());
        if (oldPass.equals(user.getUserPassword())) {
            user.setUserPassword(userVO.getUserPassword());
            return true;
        }
        return false;
    }

    public void removeUser(Long userId) {
        User user = User.load(User.class, userId);
        user.setAbolishDate(new Date());
        for (RoleUserAuthorization each : user.getRoles()) {
        	each.setAbolishDate(new Date());
        }
    }

    public List<UserVO> findAllUser() {
        List<UserVO> results = new ArrayList<UserVO>();
        List<User> users = User.findAll(User.class);
        for (User each : users) {
            UserVO userVO = new UserVO();
            userVO.domain2Vo(each);
            results.add(userVO);
        }
        return results;
    }

    public Page<UserVO> pageQueryUser(int currentPage, int pageSize) {
        List<UserVO> results = new ArrayList<UserVO>();
        Page<User> pages = queryChannel().queryPagedResultByPageNo("select m from User m where m.abolishDate>?", //
        		new Object[] { new Date() }, currentPage, pageSize);
        for (User each : pages.getResult()) {
            UserVO userVO = new UserVO();
            userVO.domain2Vo(each);
            results.add(userVO);
        }
        return new Page<UserVO>(pages.getCurrentPageNo(), pages.getTotalCount(), pages.getPageSize(), results);
    }

    public UserVO findByUserAccount(String userAccount) {
        User user = User.findByUserAccount(userAccount);
        if (user == null) {
        	return null;
        }
        UserVO userVO = new UserVO();
        userVO.domain2Vo(user);
        return userVO;
    }

    public void assignRole(UserVO userVO, RoleVO roleVO) {
        User us = new User();
        us.setId(userVO.getId());
        Role role = new Role();
        role.setId(roleVO.getId());
        us.assignRole(role);
    }

    public void assignRole(UserVO userVO, List<RoleVO> roleVOs) {
        for (RoleVO each : roleVOs) {
            assignRole(userVO, each);
        }
    }

    public void resetPassword() {
        User us = new User();
        us.resetPassword();
    }

    public void abolishRole(UserVO userVO, List<RoleVO> roles) {
        for (RoleVO each : roles) {
            User user = User.get(User.class, userVO.getId());
            Role role = Role.get(Role.class, each.getId());
            user.abolishRole(role);
        }
    }

    public Page<RoleVO> pageQueryNotAssignRoleByUser(int currentPage, int pageSize, UserVO userVO) {
        List<RoleVO> results = new ArrayList<RoleVO>();
        
        Page<Role> pages = queryChannel().queryPagedResultByPageNo( //
	        "select role from Role role where role.id not in" + //
	        "(select role from RoleUserAuthorization rau join " + //
	        "rau.role role join rau.user user where user.id=? " + //
	        "and rau.abolishDate>?) and role.abolishDate>?",  //
	        new Object[] { userVO.getId(), new Date(), new Date() }, 
	        currentPage, pageSize);
        
        for (Role each : pages.getResult()) {
            RoleVO roleVO = new RoleVO();
            roleVO.domain2Vo(each);
            results.add(roleVO);
        }
        
        return new Page<RoleVO>(pages.getCurrentPageNo(), pages.getTotalCount(), pages.getPageSize(), results);
    }

    public Page<UserVO> pageQueryUserCustom(int currentPage, int pageSize, QueryConditionVO query) {
        List<UserVO> results = new ArrayList<UserVO>();
        Page<User> pages = queryChannel().queryPagedResultByPageNo(genQueryCondition(query), //
        		new Object[] { new Date() }, currentPage, pageSize);
        for (User each : pages.getResult()) {
            UserVO userVO = new UserVO();
            userVO.domain2Vo(each);
            results.add(userVO);
        }
        return new Page<UserVO>(pages.getCurrentPageNo(), pages.getTotalCount(), pages.getPageSize(), results);
    }

	@Override
	public void modifyLastLoginTime(String useraccount) {
		User user = User.findByUserAccount(useraccount);
		if (user == null) {
			user = User.findByEmail(useraccount);
		}
		user.setLastLoginTime(new Date());
	}

	@Override
	public UserVO findByEmail(String email) {
		User user = User.findByEmail(email);
		
		if (user == null) {
			return null;
		}
		
		UserVO userVO = new UserVO();
        userVO.domain2Vo(user);
        return userVO;
	}
}
