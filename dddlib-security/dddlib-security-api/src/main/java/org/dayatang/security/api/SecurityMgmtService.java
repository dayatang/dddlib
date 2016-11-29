package org.dayatang.security.api;

/**
 * Created by yyang on 2016/10/31.
 */
public interface SecurityMgmtService {
    UserInfo createUser(String username, String password);
    UserInfo createUser(String username, String password, String remark);
    void changeUsername(String originalName, String newName);
}
