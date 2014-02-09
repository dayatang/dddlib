package org.openkoala.organisation.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openkoala.organisation.NameExistException;
import org.openkoala.organisation.OrganizationHasPrincipalYetException;
import org.openkoala.organisation.PostExistException;
import org.openkoala.organisation.TheJobHasPostAccountabilityException;

import com.dayatang.domain.QuerySettings;

/**
 * 职务
 * @author xmfang
 *
 */
@Entity
@DiscriminatorValue("Job")
public class Job extends Party {

	private static final long serialVersionUID = -5433410950032866468L;
	
	public Job() {
		super();
	}
	
	public Job(String name) {
		super(name);
	}
	
	public Job(String name, String sn) {
		super(name, sn);
	}

	/*
	 * 描述
	 */
	private String description;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public void terminate(Date date) {
		if (hasPosts(date)) {
			throw new TheJobHasPostAccountabilityException();
		}
		super.terminate(date);
	}
	
	private boolean hasPosts(Date date) {
		List<Post> posts = Post.findByJob(this, date);
		return !posts.isEmpty();
	}
	
	@Override
	public void save() {
		if (nameExist()) {
			throw new NameExistException();
		}
		super.save();
	}
	
	private boolean nameExist() {
		Date now = new Date();
		QuerySettings<Job> querySettings = QuerySettings.create(Job.class);
		querySettings.eq("name", getName())
			.le("createDate", now)
			.gt("terminateDate", now);
		
		if (getId() != null) {
			querySettings.notEq("id", getId());
		}
		
		Job job = getRepository().getSingleResult(querySettings);
		return job != null;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Job)) {
			return false;
		}
		Job that = (Job) other;
		return new EqualsBuilder().append(this.getSn(), that.getSn())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getSn()).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(getName()).build();
	}

}
