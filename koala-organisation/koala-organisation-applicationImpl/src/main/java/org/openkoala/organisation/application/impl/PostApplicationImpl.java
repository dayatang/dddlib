package org.openkoala.organisation.application.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.openkoala.organisation.application.PostApplication;
import org.openkoala.organisation.application.dto.PostDTO;
import org.openkoala.organisation.domain.Organization;
import org.openkoala.organisation.domain.Post;
import org.springframework.transaction.annotation.Transactional;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

@Named
@Transactional
public class PostApplicationImpl implements PostApplication {

	
	private QueryChannelService queryChannel;
	
	
	private QueryChannelService getQueryChannelService(){
		if(queryChannel!=null){
			return queryChannel;
		}
		return InstanceFactory.getInstance(QueryChannelService.class,"queryChannel_org");
	}
	@Override
	public Page<PostDTO> pagingQueryPosts(PostDTO example, int currentPage, int pagesize) {
		List<Object> conditionVals = new ArrayList<Object>();

		StringBuilder jpql = new StringBuilder("select _post from Post _post where _post.createDate <= ? and _post.terminateDate > ?");
		Date now = new Date();
		conditionVals.add(now);
		conditionVals.add(now);

		return queryResult(example, jpql, "_post", conditionVals, currentPage, pagesize);
	}

	@Override
	public Page<PostDTO> pagingQueryPostsOfOrganizatoin(Organization organization, PostDTO example, int currentPage, int pagesize) {
		List<Object> conditionVals = new ArrayList<Object>();

		StringBuilder jpql = new StringBuilder("select _post from Post _post"
				+ " where _post.organization = ? and _post.createDate <= ? and _post.terminateDate > ?");
		Date now = new Date();
		conditionVals.add(organization);
		conditionVals.add(now);
		conditionVals.add(now);

		return queryResult(example, jpql, "_post", conditionVals, currentPage, pagesize);
	}

	private Page<PostDTO> queryResult(PostDTO example, StringBuilder jpql, String conditionPrefix, List<Object> conditionVals,
			int currentPage, int pagesize) {
		assembleJpqlAndConditionValues(example, jpql, conditionPrefix, conditionVals);
		Page<Post> postPage = getQueryChannelService().queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), currentPage, pagesize);
		
		return new Page<PostDTO>(Page.getStartOfPage(currentPage, pagesize), 
				postPage.getTotalCount(), pagesize, transformToDtos(postPage.getResult()));
	}
	
	private void assembleJpqlAndConditionValues(PostDTO example, StringBuilder jpql, String conditionPrefix, List<Object> conditionVals) {
		String andCondition = " and " + conditionPrefix;
		if (!StringUtils.isBlank(example.getName())) {
			jpql.append(andCondition);
			jpql.append(".name like ?");
			conditionVals.add(MessageFormat.format("%{0}%", example.getName()));
		}
		if (!StringUtils.isBlank(example.getSn())) {
			jpql.append(andCondition);
			jpql.append(".sn like ?");
			conditionVals.add(MessageFormat.format("%{0}%", example.getSn()));
		}
		if (!StringUtils.isBlank(example.getDescription())) {
			jpql.append(andCondition);
			jpql.append(".description like ?");
			conditionVals.add(MessageFormat.format("%{0}%", example.getDescription()));
		}
	}
	
	private List<PostDTO> transformToDtos(List<Post> posts) {
		List<PostDTO> results = new ArrayList<PostDTO>();
		for (Post post : posts) {
			results.add(PostDTO.generateDtoBy(post));
		}
		return results;
	}
	
}
