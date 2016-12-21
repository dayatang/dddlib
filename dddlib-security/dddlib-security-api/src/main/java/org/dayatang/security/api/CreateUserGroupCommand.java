package org.dayatang.security.api;

/**
 * Created by yyang on 2016/12/2.
 */
public class CreateUserGroupCommand implements Command<UserGroupInfo> {

    private String name;

    public CreateUserGroupCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
