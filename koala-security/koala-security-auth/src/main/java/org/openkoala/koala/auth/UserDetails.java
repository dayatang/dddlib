package org.openkoala.koala.auth;

import java.io.Serializable;
import java.util.List;

public interface UserDetails extends Serializable {
	
    public List<String> getAuthorities();
    
    public String getPassword();
    
    public String getUseraccount();
    
    public boolean isAccountNonExpired();
    
    public boolean isAccountNonLocked();
    
    public boolean isCredentialsNonExpired();
    
    public boolean isEnabled();
    
    public boolean isSuper();
    
}