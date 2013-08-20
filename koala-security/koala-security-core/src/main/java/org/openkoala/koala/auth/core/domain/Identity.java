package org.openkoala.koala.auth.core.domain;

import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author lingen
 * 
 */
@Entity
@Table(name = "KS_IDENTITY")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "IDENTITY_TYPE", discriminatorType = DiscriminatorType.STRING)
@Cacheable
public abstract class Identity extends Party {

	private static final long serialVersionUID = -3878339448106527391L;

	@Column(name = "ISVALID")
	private boolean isValid;

	@Column(name = "CREATE_OWNER")
	private String createOwner;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "identity")
	private List<IdentityResourceAuthorization> authorizations;

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

	public void disableIdentity() {
		this.isValid = false;
		this.save();
	}

	public void enableIdentity() {
		this.isValid = true;
		this.save();
	}

	public List<Identity> findByCreateOwner(String createOwner) {
		return null;
	}

	public List<IdentityResourceAuthorization> getIdentityResourceAuthorizations() {
		return authorizations;
	}

	public void setAuthorizations(List<IdentityResourceAuthorization> authorizations) {
		this.authorizations = authorizations;
	}

	public void deleteByCreateOwner() {
		return;
	}
}
