package org.dayatang.security.api.query;

/**
 * Created by yyang on 2016/11/29.
 */
public class LoginCommand {
    private String username;
    private String password;

    public LoginCommand(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
