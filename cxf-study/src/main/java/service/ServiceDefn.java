package service;

import pojo.User;
import pojo.UserCollection;

public interface ServiceDefn {
	public abstract UserCollection getUsers();
	public abstract User getUser(Integer id);
	
	public abstract User getUser2(Integer id);
	public abstract javax.ws.rs.core.Response getBadRequest();
}
