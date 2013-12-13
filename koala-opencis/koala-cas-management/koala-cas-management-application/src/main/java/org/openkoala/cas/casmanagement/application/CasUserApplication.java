package org.openkoala.cas.casmanagement.application;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openkoala.cas.casmanagement.application.dto.UserDTO;

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
	Response createUser(UserDTO user);
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	Response updateUser(@PathParam("id") long id, UserDTO user);
	
	@PUT
	@Path("/password/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	Response modifyPassword(@PathParam("id") long id, UserDTO user);
	
	@PUT
	@Path("/enabled/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	Response enabled(@PathParam("id") Long id); 
	
	@PUT
	@Path("/disabled/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	Response disabled(@PathParam("id") Long id);
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	UserDTO getUser(@PathParam("id") Long id);
	
}
