package org.openkoala.koala.auth;

import java.io.Serializable;

public interface GrantedAuthority extends Serializable {

	public String getAuthority();
}
