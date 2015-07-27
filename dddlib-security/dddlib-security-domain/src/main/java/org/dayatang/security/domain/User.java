package org.dayatang.security.domain;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.utils.Assert;

import javax.persistence.*;
import java.security.Principal;
import java.util.*;

/**
 * 系统用户。系统用户常常对应于一个员工
 * Created by yyang on 15/1/13.
 */
@Entity
@DiscriminatorValue("USER")
public class User extends Actor implements Principal {

    //口令
    private String password;

    //口令提示
    private String passwordHint;

    //是否已被锁定。被锁定的用户无法登录，直至解锁
    private boolean locked = false;

    @ManyToMany(mappedBy = "users")
    private Set<UserGroup> groups = new HashSet<UserGroup>();

    public User() {
    }

    public User(String name, String password) {
        super(name);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordHint() {
        return passwordHint;
    }

    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Set<UserGroup> getGroups() {
        return ImmutableSet.copyOf(groups);
    }

    @Override
    public String[] businessKeys() {
        return new String[] {"name"};
    }

    /**
     * 锁定员工。被锁定的员工无法登录，直至解锁
     */
    public void lock() {
        this.locked = true;
        save();
    }

    /**
     * 解锁员工。解锁后的员工可以登录系统
     */
    public void unlock() {
        this.locked = false;
        save();
    }

    /**
     * 根据用户名获取用户。此用户有可能处于失效状态。
     * @param name 用户名
     * @return 如果找到指定名字的用户则返回该用户，否则返回null
     */
    public static User getByName(String name) {
        return AbstractEntity.getByProperty(User.class, "name", name);
    }

    @Override
    public String toString() {
        return getName();
    }

    public static User create(String username) {
        return create(username, "");
    }

    public static User create(String username, String password) {
        User user = new User(username, password);
        user.save();
        return user;
    }
}
