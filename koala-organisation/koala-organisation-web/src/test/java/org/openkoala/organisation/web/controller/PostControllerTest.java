package org.openkoala.organisation.web.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openkoala.organisation.OrganizationHasPrincipalYetException;
import org.openkoala.organisation.SnIsExistException;
import org.openkoala.organisation.TerminateHasEmployeePostException;
import org.openkoala.organisation.application.BaseApplication;
import org.openkoala.organisation.application.PostApplication;
import org.openkoala.organisation.application.dto.PostDTO;
import org.openkoala.organisation.domain.Company;
import org.openkoala.organisation.domain.Job;
import org.openkoala.organisation.domain.Organization;
import org.openkoala.organisation.domain.OrganizationAbstractEntity;
import org.openkoala.organisation.domain.Post;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.dayatang.querychannel.support.Page;

/**
 * PostController单元测试
 * @author xmfang
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({PostDTO.class, OrganizationAbstractEntity.class})
public class PostControllerTest {
	
	@Mock
	private PostApplication postApplication;

	@Mock
	private BaseApplication baseApplication;

	@InjectMocks
	private PostController postController = new PostController();
	
	private Long organizationId = 1L;
	private Organization organization = new Company("总公司", "COM-XXX");
	private Post post = new Post("CEO", "XXX");
	
	@Test
	public void testPagingQuery() {
		List<PostDTO> dtos = generatePostDtos();
		Page<PostDTO> postPage = new Page<PostDTO>(1, 2, 10, dtos);
		
		when(postApplication.pagingQueryPosts(new PostDTO(), 1, 10)).thenReturn(postPage);
		assertEquals(dtos, postController.pagingQuery(1, 10, new PostDTO()).get("Rows"));
	}

	private List<PostDTO> generatePostDtos() {
		PostDTO dto1 = new PostDTO();
		dto1.setId(1L);
		dto1.setName("CEO");
		dto1.setSn("XXX1");
		
		PostDTO dto2 = new PostDTO();
		dto2.setId(2L);
		dto2.setName("总公司副总经理");
		dto2.setSn("XXX2");
		
		List<PostDTO> dtos = new ArrayList<PostDTO>();
		dtos.add(dto1);
		dtos.add(dto2);
		
		return dtos;
	}
	
	@Test
	public void testCreatePost() {
		when(baseApplication.getEntity(Organization.class, organizationId)).thenReturn(organization);
		postController.createPost(post, organizationId);
		verify(baseApplication, times(1)).saveParty(post);
	}

	@Test
	public void testCatchOrganizationHasPrincipalYetExceptionWhenCreatePost() {
		when(baseApplication.getEntity(Organization.class, organizationId)).thenReturn(organization);
		doThrow(new OrganizationHasPrincipalYetException()).when(baseApplication).saveParty(post);
		assertEquals("该机构已经有负责岗位！", postController.createPost(post, organizationId).get("result"));
	}

	@Test
	public void testCatchSnIsExistExceptionWhenCreatePost() {
		when(baseApplication.getEntity(Organization.class, organizationId)).thenReturn(organization);
		doThrow(new SnIsExistException()).when(baseApplication).saveParty(post);
		assertEquals("岗位编码: " + post.getSn() + " 已被使用！", postController.createPost(post, organizationId).get("result"));
	}

	@Test
	public void testCatchExceptionWhenCreatePost() {
		when(baseApplication.getEntity(Organization.class, organizationId)).thenReturn(organization);
		doThrow(new RuntimeException()).when(baseApplication).saveParty(post);
		assertEquals("保存失败！", postController.createPost(post, organizationId).get("result"));
	}
	
	public void testUpdatePost() {
		when(baseApplication.getEntity(Organization.class, organizationId)).thenReturn(organization);
		postController.updatePost(post, organizationId);
		verify(baseApplication, times(1)).updateParty(post);
	}

	@Test
	public void testCatchSnIsExistExceptionWhenUpdatePost() {
		when(baseApplication.getEntity(Organization.class, organizationId)).thenReturn(organization);
		doThrow(new SnIsExistException()).when(baseApplication).updateParty(post);
		assertEquals("岗位编码: " + post.getSn() + " 已被使用！", postController.updatePost(post, organizationId).get("result"));
	}

	@Test
	public void testCatchExceptionWhenUpdatePost() {
		when(baseApplication.getEntity(Organization.class, organizationId)).thenReturn(organization);
		doThrow(new RuntimeException()).when(baseApplication).updateParty(post);
		assertEquals("修改失败！", postController.updatePost(post, organizationId).get("result"));
	}
	
	@Test
	public void testQueryPostsOfOrganization() {
		Organization organization = mock(Organization.class);
		Set<Post> posts = new HashSet<Post>();
		posts.add(post);
		
		when(baseApplication.getEntity(Organization.class, organizationId)).thenReturn(organization);
		when(organization.getPosts(any(Date.class))).thenReturn(posts);
		assertEquals(posts, postController.queryPostsOfOrganization(organizationId).get("result"));
	}
	
	@Test
	public void testPagingQueryPostsOfOrganization() {
		List<PostDTO> dtos = generatePostDtos();
		PostDTO example = new PostDTO();
		Page<PostDTO> postPage = new Page<PostDTO>(1, 2, 10, dtos);
		
		when(baseApplication.getEntity(Organization.class, organizationId)).thenReturn(organization);
		when(postApplication.pagingQueryPostsOfOrganizatoin(organization, example, 1, 10)).thenReturn(postPage);
		assertEquals(dtos, postController.pagingQueryPostsOfOrganization(organizationId, example, 1, 10).get("Rows"));
	}
	
	@Test
	public void testGetPostsByEmployee() {
		Long postId = 1L;
		PostDTO dto = createPostDTO();
		
		PowerMockito.mockStatic(PostDTO.class);
		when(baseApplication.getEntity(Post.class, postId)).thenReturn(post);
		when(PostDTO.generateDtoBy(post)).thenReturn(dto);
		
		assertEquals(dto, postController.get(postId).get("data"));
	}

	private PostDTO createPostDTO() {
		PostDTO dto = new PostDTO();
		dto.setName("CEO");
		dto.setSn("XXX");
		return dto;
	}
	
	@Test
	public void testTerminatePost() {
		PostDTO dto = preparePostDtoAndStaticMock();
		postController.terminatePost(dto);
		verify(baseApplication, only()).terminateParty(dto.transFormToPost());
	}
	
	private PostDTO preparePostDtoAndStaticMock() {
		Long organizationId = 1L;
		Long jobId = 2L;
		
		staticMockForTransFormDtoToPost(organizationId, jobId);
		
		PostDTO dto = createPostDTO();
		dto.setOrganizationId(organizationId);
		dto.setJobId(jobId);
		return dto;
	}
	
	private void staticMockForTransFormDtoToPost(Long organizationId, Long jobId) {
		PowerMockito.mockStatic(OrganizationAbstractEntity.class);
		when(OrganizationAbstractEntity.get(Organization.class, organizationId)).thenReturn(new Company());
		when(OrganizationAbstractEntity.get(Job.class, jobId)).thenReturn(new Job());
	}
	
	@Test
	public void testCatchTerminateHasEmployeePostExceptionWhenTerminatePost() {
		PostDTO dto = preparePostDtoAndStaticMock();
		Post post = dto.transFormToPost();
		doThrow(new TerminateHasEmployeePostException()).when(baseApplication).terminateParty(post);
		assertEquals("还有员工在此岗位上任职，不能撤销！", postController.terminatePost(dto).get("result"));
	}
	
	@Test
	public void testCatchExceptionWhenTerminatePost() {
		PostDTO dto = preparePostDtoAndStaticMock();
		Post post = dto.transFormToPost();
		doThrow(new RuntimeException()).when(baseApplication).terminateParty(post);
		assertEquals("撤销员工岗位失败！", postController.terminatePost(dto).get("result"));
	}
	
	@Test
	public void testTerminatePosts() {
		Long organizationId = 1L;
		Long jobId = 2L;
		staticMockForTransFormDtoToPost(organizationId, jobId);

		PostDTO[] postDtos = new PostDTO[2];
		Set<Post> posts = new HashSet<Post>();

		int i = 0;
		for (PostDTO dto : generatePostDtos()) {
			dto.setOrganizationId(organizationId);
			dto.setJobId(jobId);
			postDtos[i] = dto;
			posts.add(dto.transFormToPost());
			i ++;
		}
		
		postController.terminatePosts(postDtos);
		verify(baseApplication, only()).terminateParties(posts);
	}
	
	@Test
	public void testCatchTerminateHasEmployeePostExceptionWhenTerminatePosts() {
		Long organizationId = 1L;
		Long jobId = 2L;
		staticMockForTransFormDtoToPost(organizationId, jobId);

		PostDTO[] postDtos = new PostDTO[2];
		Set<Post> posts = new HashSet<Post>();

		int i = 0;
		for (PostDTO dto : generatePostDtos()) {
			dto.setOrganizationId(organizationId);
			dto.setJobId(jobId);
			postDtos[i] = dto;
			posts.add(dto.transFormToPost());
			i ++;
		}
		
		doThrow(new TerminateHasEmployeePostException()).when(baseApplication).terminateParties(posts);
		assertEquals("还有员工在此岗位上任职，不能撤销！", postController.terminatePosts(postDtos).get("result"));
	}
	
	@Test
	public void testCatchExceptionWhenTerminatePosts() {
		Long organizationId = 1L;
		Long jobId = 2L;
		staticMockForTransFormDtoToPost(organizationId, jobId);

		PostDTO[] postDtos = new PostDTO[2];
		Set<Post> posts = new HashSet<Post>();

		int i = 0;
		for (PostDTO dto : generatePostDtos()) {
			dto.setOrganizationId(organizationId);
			dto.setJobId(jobId);
			postDtos[i] = dto;
			posts.add(dto.transFormToPost());
			i ++;
		}
		
		doThrow(new RuntimeException()).when(baseApplication).terminateParties(posts);
		assertEquals("撤销员工岗位失败！", postController.terminatePosts(postDtos).get("result"));
	}
	
}
