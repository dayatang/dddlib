package org.openkoala.koala.auth;

import java.util.List;


public interface AccessDecisionManager {
    void decide(UserDetails userDetails, List<GrantedAuthority> configAttributes);
}
