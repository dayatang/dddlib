package org.openkoala.cas.casmanagement.application;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openkoala.auth.application.vo.UserVO;

/**
 * CAS用户应用接口层WebService API
 * @author zhuyuanbiao
 * @date 2013年12月9日 上午10:30:02
 *
 */
@Path("/user")
public interface CasUserApplication {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	UserVO createUser(UserVO user);
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	UserVO updateUser(@PathParam("id") long id, UserVO user);
	
	@PUT
	@Path("/password/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	UserVO modifyPassword(@PathParam("id") Long id, UserVO user);
	
	@PUT
	@Path("/enabled/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	UserVO enabled(@PathParam("id") Long id); 
	
	@PUT
	@Path("/disabled/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	UserVO disabled(@PathParam("id") Long id);
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	UserVO getUser(@PathParam("id") Long id);
	
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	Response removeUser(@PathParam("id") Long id);
	
	@GET
	@Path("/isUserValid")
	@Produces(MediaType.TEXT_PLAIN)
	boolean isUserValid(@FormParam("username")String username,@FormParam("password")String password);
	
}
