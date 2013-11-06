package org.openkoala.organisation.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openkoala.organisation.OrganizationHasPrincipalYetException;
import org.openkoala.organisation.SnIsExistException;
import org.openkoala.organisation.TerminateHasEmployeePostException;
import org.openkoala.organisation.application.PostApplication;
import org.openkoala.organisation.application.dto.PostDTO;
import org.openkoala.organisation.domain.Organization;
import org.openkoala.organisation.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayatang.querychannel.support.Page;

/**
 * 岗位管理Controller
 * @author xmfang
 *
 */
@Controller
@RequestMapping("/post")
public class PostController extends BaseController {

	@Autowired
	private PostApplication postApplication;
	
	/**
	 * 分页查询职务
	 * @param page
	 * @param pagesize
	 * @param post
	 * @return
	 */
	@ResponseBody
    @RequestMapping("/pagingquery")
	public Map<String, Object> pagingQuery(int page, int pagesize, PostDTO post) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Page<PostDTO> posts = postApplication.pagingQueryPosts(post, page, pagesize);
		
		dataMap.put("Rows", posts.getResult());
		dataMap.put("start", page * pagesize - pagesize);
		dataMap.put("limit", pagesize);
		dataMap.put("Total", posts.getTotalCount());
		return dataMap;
	}

	/**
	 * 创建一个岗位
	 * @param post
	 * @return
	 */
	@ResponseBody
    @RequestMapping("/create")
	public Map<String, Object> createPost(Post post, Long organizationId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			post.setOrganization(getBaseApplication().getEntity(Organization.class, organizationId));
			getBaseApplication().saveParty(post);
			dataMap.put("result", "success");
		} catch (OrganizationHasPrincipalYetException exception) {
			dataMap.put("result", "该机构已经有负责岗位！");
		} catch (SnIsExistException exception) {
			dataMap.put("result", "岗位编码: " + post.getSn() + " 已被使用！");
		} catch (Exception e) {
			dataMap.put("result", "保存失败！");
			e.printStackTrace();
		}
		return dataMap;
	}

	/**
	 * 更新岗位信息
	 * @param post
	 * @return
	 */
	@ResponseBody
    @RequestMapping("/update")
	public Map<String, Object> updatePost(Post post, Long organizationId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			post.setOrganization(getBaseApplication().getEntity(Organization.class, organizationId));
			getBaseApplication().updateParty(post);
			dataMap.put("result", "success");
		} catch (SnIsExistException exception) {
			dataMap.put("result", "岗位编码: " + post.getSn() + " 已被使用！");
		} catch (Exception e) {
			dataMap.put("result", "修改失败！");
			e.printStackTrace();
		}
		return dataMap;
	}

	@ResponseBody
    @RequestMapping("/query-post-by-org")
	public Map<String, Object> queryPostsOfOrganization(Long organizationId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Organization organization = getBaseApplication().getEntity(Organization.class, organizationId);
		if (organization != null) {
			dataMap.put("result", organization.getPosts(new Date()));
		}
		return dataMap;
	}

	@ResponseBody
    @RequestMapping("/paging-query-post-by-org")
	public Map<String, Object> pagingQueryPostsOfOrganization(Long organizationId, PostDTO example, int page, int pagesize) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Organization organization = getBaseApplication().getEntity(Organization.class, organizationId);
		Page<PostDTO> posts = postApplication.pagingQueryPostsOfOrganizatoin(organization, example, page, pagesize);
		
		dataMap.put("Rows", posts.getResult());
		dataMap.put("start", page * pagesize - pagesize);
		dataMap.put("limit", pagesize);
		dataMap.put("Total", posts.getTotalCount());
		return dataMap;
	}
	
	/**
	 * 根据ID获得职务
	 * @param id
	 * @return
	 */
    @ResponseBody
    @RequestMapping("/get/{id}")
	public Map<String, Object> get(@PathVariable("id") Long id) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			dataMap.put("data", PostDTO.generateDtoBy(getBaseApplication().getEntity(Post.class, id)));
		} catch (Exception e) {
			dataMap.put("error", "查询指定岗位失败！");
			e.printStackTrace();
		}
		return dataMap;
	}
	
    /**
     * 撤销某个职务
     * @param postDto
     * @return
     */
	@ResponseBody
    @RequestMapping("/terminate")
	public Map<String, Object> terminatePost(PostDTO postDto) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			getBaseApplication().terminateParty(postDto.transFormToPost());
			dataMap.put("result", "success");
		} catch (TerminateHasEmployeePostException exception) {
			dataMap.put("result", "还有员工在此岗位上任职，不能撤销！");
		} catch (Exception e) {
			dataMap.put("result", "撤销员工岗位失败！");
			e.printStackTrace();
		}
		return dataMap;
	}
	
	/**
	 * 同时撤销多个职务
	 * @param postDTOs
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/terminate-posts", method = RequestMethod.POST, consumes = "application/json")
	public Map<String, Object> terminatePosts(@RequestBody PostDTO[] postDTOs) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			Set<Post> posts = new HashSet<Post>();
			for (PostDTO postDTO : postDTOs) {
				posts.add(postDTO.transFormToPost());
			}
			
			getBaseApplication().terminateParties(posts);
			dataMap.put("result", "success");
		} catch (TerminateHasEmployeePostException exception) {
			dataMap.put("result", "还有员工在此岗位上任职，不能撤销！");
		} catch (Exception e) {
			dataMap.put("result", "撤销员工岗位失败！");
			e.printStackTrace();
		}
		
		return dataMap;
	}
	
}
