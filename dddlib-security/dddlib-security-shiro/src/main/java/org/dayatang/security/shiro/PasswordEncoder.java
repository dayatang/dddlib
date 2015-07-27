package org.dayatang.security.shiro;

/**
 * Created by wxzhou on 15-1-26.
 */
public interface PasswordEncoder {
    String encodePassword(String password);
    String encodePassword(String password, Object salt);
}
