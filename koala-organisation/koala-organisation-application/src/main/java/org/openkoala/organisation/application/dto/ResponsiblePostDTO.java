package org.openkoala.organisation.application.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openkoala.organisation.domain.Post;

public class ResponsiblePostDTO {

	private Long postId;
	
	private String postName;
	
	private String postSn;
	
	private String postDescription;
	
	private boolean principal;

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getPostSn() {
		return postSn;
	}

	public void setPostSn(String postSn) {
		this.postSn = postSn;
	}

	public String getPostDescription() {
		return postDescription;
	}

	public void setPostDescription(String postDescription) {
		this.postDescription = postDescription;
	}

	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}
	
	public static ResponsiblePostDTO generateByPost(Post post, boolean principal) {
		ResponsiblePostDTO result = new ResponsiblePostDTO();
		
		result.setPrincipal(principal);
		result.setPostId(post.getId());
		result.setPostName(post.getName());
		result.setPostSn(post.getSn());
		result.setPostDescription(post.getDescription());
		
		return result;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof ResponsiblePostDTO)) {
			return false;
		}
		ResponsiblePostDTO that = (ResponsiblePostDTO) other;
		return new EqualsBuilder().append(postId, that.postId)
				.append(principal, that.principal)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(postId).append(principal).toHashCode();
	}
	
}
