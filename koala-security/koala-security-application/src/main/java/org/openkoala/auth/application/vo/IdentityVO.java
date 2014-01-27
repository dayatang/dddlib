package org.openkoala.auth.application.vo;

import java.io.Serializable;

public class IdentityVO extends PartyVO implements Serializable{
	/**
     * 
     */
    private static final long serialVersionUID = -1733560775559010935L;
    private boolean isValid;
	private String createOwner;
	public boolean isValid() {
		return isValid;
	}

	
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	public String getCreateOwner() {
		return createOwner;
	}
	public void setCreateOwner(String createOwner) {
		this.createOwner = createOwner;
	}

}
