package org.dayatang.security.api;

import java.util.Date;

/**
 * Created by yyang on 2016/11/29.
 */
public class UserInfo {
    private String id;
    private int version;

    private String username;
    private String remark;

    private Date created;
    private Date lastModified;
    private Date expired;
    private boolean disabled;
    private boolean locked;

}
