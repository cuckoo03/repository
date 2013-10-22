package service;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import pojo.User;
import pojo.UserCollection;

@Path("/myservice/")
@Produces(MediaType.APPLICATION_XML)
public class ServiceImpl implements ServiceDefn {
	private static Map<Integer, User> users = new HashMap<Integer, User>();
	static {
		users.put(1, new User(1, "a"));
		users.put(2, new User(2, "b"));
		users.put(3, new User(3, "c"));
	}

	@GET
	@Path("/users")
	public UserCollection getUsers() {
		return new UserCollection(users.values());
	}

	@GET
	@Path("/user/{id}")
	public User getUser(@PathParam("id") Integer id) {
		return users.get(id);
	}

	@GET
	@Path("/user2")
	public User getUser2(@QueryParam("id") Integer id) {
		return users.get(id);
	}

	@GET
	@Path("/users/bad")
	public Response getBadRequest() {
		return Response.status(Status.BAD_REQUEST).build();
	}
}
