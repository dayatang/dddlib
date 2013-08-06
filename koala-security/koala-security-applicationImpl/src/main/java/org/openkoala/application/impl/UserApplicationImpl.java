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
import org.openkoala.util.DateFormatUtils;
import org.springframework.transaction.annotation.Transactional;

import com.dayatang.querychannel.support.Page;
import com.dayatang.utils.DateUtils;

@Named("userApplication")
@Transactional(value="transactionManager_security")
@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
@Stateless(name = "UserApplication")
@Remote
public class UserApplicationImpl extends BaseImpl implements UserApplication {

    public UserVO getUser(Long userId) {
        User user = User.get(User.class, userId);
        UserVO usvo = new UserVO();
        usvo.setId(user.getId());
        usvo.setLastLoginTime(user.getLastLoginTime() == null ? "" : user.getLastLoginTime().toString());
        usvo.setUserAccount(user.getUserAccount());
        usvo.setUserPassword(user.getUserPassword());
        usvo.setUserDesc(user.getUserDesc());
        usvo.setAbolishDate(user.getAbolishDate() == null ? "" : user.getAbolishDate().toString());
        usvo.setCreateDate(user.getCreateDate() == null ? "" : user.getCreateDate().toString());
        usvo.setCreateOwner(user.getCreateOwner());
        usvo.setName(user.getName());
        usvo.setSerialNumber(user.getSerialNumber());
        usvo.setSortOrder(user.getSortOrder());
        usvo.setValid(user.isValid());
        return usvo;
    }

    public UserVO saveUser(UserVO userVO) {
		User user = new User();
		user.setLastLoginTime(userVO.getLastLoginTime() == null ? null : DateFormatUtils.parse(userVO.getLastLoginTime()));
		user.setAbolishDate(DateUtils.MAX_DATE);
        user.setCreateDate(new Date());
        user.setUserAccount(userVO.getUserAccount());
        user.setUserPassword(userVO.getUserPassword());
        user.setUserDesc(userVO.getUserDesc());
        user.setCreateOwner(userVO.getCreateOwner());
        user.setName(userVO.getName());
        user.setSerialNumber(userVO.getSerialNumber());
        user.setSortOrder(userVO.getSortOrder());
        user.setValid(userVO.isValid());
        // 检查用户账号是否已经存在
        if (user.isAccountExist()) {
        	throw new ApplicationException("userAccount.exist", null);
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
        for (RoleUserAuthorization authorization : user.getRoles()) {
        	authorization.setAbolishDate(new Date());
        }
    }

    public List<UserVO> findAllUser() {
        List<UserVO> list = new ArrayList<UserVO>();
        List<User> all = User.findAll(User.class);
        for (User user : all) {
            UserVO usvo = new UserVO();
            usvo.setId(user.getId());
            usvo.setVersion(user.getVersion());
            usvo.setLastLoginTime(user.getLastLoginTime() == null ? null : user.getLastLoginTime().toString());
            usvo.setUserAccount(user.getUserAccount());
            usvo.setUserPassword(user.getUserPassword());
            usvo.setUserDesc(user.getUserDesc());
            usvo.setAbolishDate(user.getAbolishDate() == null ? null : user.getAbolishDate().toString());
            usvo.setCreateDate(user.getCreateDate() == null ? null : user.getCreateDate().toString());
            usvo.setCreateOwner(user.getCreateOwner());
            usvo.setName(user.getName());
            usvo.setSerialNumber(user.getSerialNumber());
            usvo.setSortOrder(user.getSortOrder());
            usvo.setValid(user.isValid());
            list.add(usvo);
        }
        return list;
    }

    public Page<UserVO> pageQueryUser(int currentPage, int pageSize) {
        List<UserVO> result = new ArrayList<UserVO>();
        Page<User> pages = queryChannel().queryPagedResultByPageNo("select m from User m where m.abolishDate>?", //
        		new Object[] { new Date() }, currentPage, pageSize);
        for (User user : pages.getResult()) {
            UserVO usvo = new UserVO();
            usvo.setId(user.getId());
            usvo.setVersion(user.getVersion());
            usvo.setLastLoginTime(user.getLastLoginTime() == null ? null : user.getLastLoginTime().toString());
            usvo.setUserAccount(user.getUserAccount());
            usvo.setUserPassword(user.getUserPassword());
            usvo.setLastModifyTime(user.getLastModifyTime() == null ? null : user.getLastModifyTime().toString());
            usvo.setUserDesc(user.getUserDesc());
            usvo.setAbolishDate(user.getAbolishDate().toString());
            usvo.setCreateDate(user.getCreateDate().toString());
            usvo.setCreateOwner(user.getCreateOwner());
            usvo.setName(user.getName());
            usvo.setSerialNumber(user.getSerialNumber());
            usvo.setSortOrder(user.getSortOrder());
            usvo.setValid(user.isValid());
            result.add(usvo);
        }
        return new Page<UserVO>(pages.getCurrentPageNo(), pages.getTotalCount(), pages.getPageSize(), result);
    }

    public UserVO findByUserAccount(String userAccount) {
        User user = User.findByUserAccount(userAccount);
        if (user == null) {
        	return null;
        }
        UserVO usvo = new UserVO();
        usvo.setId(user.getId());
        usvo.setVersion(user.getVersion());
        usvo.setLastLoginTime(user.getLastLoginTime() == null ? null : user.getLastLoginTime().toString());
        usvo.setUserAccount(user.getUserAccount());
        usvo.setUserPassword(user.getUserPassword());
        usvo.setUserDesc(user.getUserDesc());
        usvo.setAbolishDate(user.getAbolishDate() == null ? null : user.getAbolishDate().toString());
        usvo.setCreateDate(user.getCreateDate() == null ? null : user.getCreateDate().toString());
        usvo.setCreateOwner(user.getCreateOwner());
        usvo.setName(user.getName());
        usvo.setSerialNumber(user.getSerialNumber());
        usvo.setSortOrder(user.getSortOrder());
        usvo.setValid(user.isValid());
        return usvo;
    }

    public void assignRole(UserVO userVO, RoleVO roleVO) {
        User us = new User();
        us.setId(Long.valueOf(userVO.getId()));
        Role role = new Role();
        role.setId(Long.valueOf(roleVO.getId()));
        us.AssignRole(role);
    }

    public void assignRole(UserVO userVO, List<RoleVO> roleVOs) {
        for (RoleVO rv : roleVOs) {
            assignRole(userVO, rv);
        }
    }

    public void passwordReset() {
        User us = new User();
        us.passwordReset();
    }

    public void abolishRole(UserVO userVO, List<RoleVO> roles) {
        for (RoleVO roleVO : roles) {
            User user = User.get(User.class, userVO.getId());
            Role role = Role.get(Role.class, roleVO.getId());
            user.abolishRole(role);
        }
    }

    public Page<RoleVO> pageQueryNotAssignRoleByUser(int currentPage, int pageSize, UserVO userVO) {
        List<RoleVO> result = new ArrayList<RoleVO>();
        Page<Role> pages = queryChannel().queryPagedResultByPageNo(
//        		"select role from Role role where role.id not in" +
//        		"(select m.id from Role m,User r,RoleUserAuthorization t where m.id=t.role.id and r.id=t.user.id" +
//        		" and r.id=?1)", new Object[] { userVO.getId() }, 
//        		currentPage, pageSize);
	        "select role from Role role where role.id not in" +
	        "(select role from RoleUserAuthorization rau join rau.role role join rau.user user where user.id=? " +
	        "and rau.abolishDate>?) and role.abolishDate>?", 
	        new Object[] { userVO.getId(), new Date(), new Date() }, 
        currentPage, pageSize);
        for (Role role : pages.getResult()) {
            RoleVO roleVO = new RoleVO();
            roleVO.setAbolishDate(DateFormatUtils.format(role.getAbolishDate()));
            roleVO.setCreateDate(DateFormatUtils.format(role.getCreateDate()));
            roleVO.setCreateOwner(role.getCreateOwner());
            roleVO.setId(role.getId());
            roleVO.setName(role.getName());
            roleVO.setSerialNumber(role.getSerialNumber());
            roleVO.setSortOrder(role.getSortOrder());
            roleVO.setRoleDesc(role.getRoleDesc());
            roleVO.setValid(role.isValid());
            roleVO.setVersion(role.getVersion());
            result.add(roleVO);
        }
        return new Page<RoleVO>(pages.getCurrentPageNo(), pages.getTotalCount(), pages.getPageSize(), result);
    }

    public Page<UserVO> pageQueryUserCustom(int currentPage, int pageSize, QueryConditionVO query) {
        List<UserVO> result = new ArrayList<UserVO>();
        Page<User> pages = queryChannel().queryPagedResultByPageNo(genQueryCondition(query), //
        		new Object[] { new Date() }, currentPage, pageSize);
        for (User user : pages.getResult()) {
            UserVO usvo = new UserVO();
            usvo.setId(user.getId());
            usvo.setVersion(user.getVersion());
            usvo.setLastLoginTime(user.getLastLoginTime() == null ? null : user.getLastLoginTime().toString());
            usvo.setUserAccount(user.getUserAccount());
            usvo.setUserPassword(user.getUserPassword());
            usvo.setLastModifyTime(user.getLastModifyTime() == null ? null : user.getLastModifyTime().toString());
            usvo.setUserDesc(user.getUserDesc());
            usvo.setAbolishDate(user.getAbolishDate().toString());
            usvo.setCreateDate(user.getCreateDate().toString());
            usvo.setCreateOwner(user.getCreateOwner());
            usvo.setName(user.getName());
            usvo.setSerialNumber(user.getSerialNumber());
            usvo.setSortOrder(user.getSortOrder());
            usvo.setValid(user.isValid());
            result.add(usvo);
        }
        return new Page<UserVO>(pages.getCurrentPageNo(), pages.getTotalCount(), pages.getPageSize(), result);
    }
}
