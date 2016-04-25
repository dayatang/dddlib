package org.dddlib.organisation.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.Date;
import java.util.List;


@Entity
@DiscriminatorValue("OrgLineMgmt")
@NamedQueries({
		@NamedQuery(name = "OrgLineMgmt.getParentOfOrganization", query = "select o.commissioner from OrgLineMgmt o where o.responsible = :organization and o.fromDate <= :date and o.toDate > :date"),
		@NamedQuery(name = "OrgLineMgmt.findChildrenOfOrganization", query = "select o.responsible from OrgLineMgmt o where o.commissioner = :organization and o.fromDate <= :date and o.toDate > :date"),
		@NamedQuery(name = "OrgLineMgmt.findByResponsible", query = "select o from OrgLineMgmt o where o.responsible = :organization and o.fromDate <= :date and o.toDate > :date") })
public class OrgLineMgmt extends Accountability<Organization, Organization> {

	private static final long serialVersionUID = 7390804525640459582L;

	protected OrgLineMgmt() {
	}

	public OrgLineMgmt(Organization parent, Organization child, Date date) {
		super(parent, child, date);
	}

	public static Organization getParentOfOrganization(
			Organization organization, Date date) {
		return getRepository().createNamedQuery("OrgLineMgmt.getParentOfOrganization")
				.addParameter("organization", organization).addParameter("date", date).singleResult();
	}

	public static List<Organization> findChildrenOfOrganization(
			Organization organization, Date date) {
		return getRepository().createNamedQuery("OrgLineMgmt.findChildrenOfOrganization")
				.addParameter("organization", organization).addParameter("date", date).list();
	}

	public static List<OrgLineMgmt> findAll() {
		return getRepository().findAll(OrgLineMgmt.class);
	}

	public static OrgLineMgmt getByResponsible(Organization responsible, Date date) {
		return getRepository().createNamedQuery("OrgLineMgmt.findByResponsible")
				.addParameter("organization", responsible).addParameter("date", date).singleResult();
	}
}
