package org.dayatang.security.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.security.Principal;
import java.util.List;

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

    public void changePassword(String origPassword, String newPassword) {
        if (!matchPassword(origPassword)) {
            throw new PasswordUnmatchException();
        }
        setPassword(newPassword);
        save();
    }

    /**
     * 判断口令是否匹配
     * @param password
     * @return
     */
    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }

    public boolean unmatchPassword(String password) {
        return !matchPassword(password);
    }

    /**
     * 判断系统中是否已存在指定名字的用户
     * @param username 要检查的用户名
     * @return 如果已存在返回true，否则返回false
     */
    public static boolean existsUsername(String username) {
        return getByName(username) != null;
    }

    /**
     * 根据ID获取用户。此用户有可能处于失效状态。
     * @param id 用户ID
     * @return 如果找到指定ID的用户则返回该用户，否则返回null
     */
    public static User get(String id) {
        return get(User.class, id);
    }

    /**
     * 根据用户名获取用户。此用户有可能处于失效状态。
     * @param name 用户名
     * @return 如果找到指定名字的用户则返回该用户，否则返回null
     */
    public static User getByName(String name) {
        return getByName(User.class, name);
    }

    /**
     * 列出系统全部用户
     * @return 系统的全部用户
     */
    public static List<User> list() {
        return findAll(User.class);
    }

    /**
     * 根据提供的用户名和口令验证用户的有效性
     * @param username 用户名
     * @param password 口令
     * @return 如果用户存在，口令匹配，而且没有失效或锁定，则验证成功，返回true；否则验证失败，返回false
     */
    public static boolean authenticate(String username, String password) {
        User user = getByName(username);
        if (user == null || user.isDisabled() || user.isLocked()) {
            return false;
        }
        return user.matchPassword(password);
    }

    public static User create(String username) {
        return create(username, "");
    }

    public static User create(String username, String password) {
        if (existsUsername(username)) {
            throw new DuplicateUsernameException();
        }
        User user = new User(username, password);
        user.save();
        return user;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 23).append(getName()).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof User)) {
            return false;
        }
        User that = (User) other;
        return new EqualsBuilder().append(this.getName(), that.getName()).isEquals();
    }

    @Override
    public String toString() {
        return getName();
    }
}
