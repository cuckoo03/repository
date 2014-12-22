package com;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/rest")
public class RestSample {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/hello")
	public String getHello() {
		return "hello rest";
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/hello/id={id}")
	public String getHello(@PathParam("id") final int id) {
		return "id:" + id;
	}

	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/hello/xml")
	public Response getHelloXml() {
		Hello hello = new Hello();
		hello.setId(1);
		return Response.status(200).entity(hello).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/hello/json")
	public List<Hello> getHelloJson() {
		List<Hello> list = new ArrayList<Hello>();
		Hello hello = new Hello();
		hello.setId(1);
		list.add(hello);
		return list;
	}
}
