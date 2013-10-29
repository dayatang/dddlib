package org.openkoala.organisation.application;

import org.openkoala.organisation.application.dto.PostDTO;
import org.openkoala.organisation.domain.Organization;

import com.dayatang.querychannel.support.Page;

public interface PostApplication {

	Page<PostDTO> pagingQueryPosts(PostDTO example, int currentPage, int pagesize);
	
	Page<PostDTO> pagingQueryPostsOfOrganizatoin(Organization organization, PostDTO example, int currentPage, int pagesize);
}
