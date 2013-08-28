package com.service.freetalk.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/web-application-context.xml")
public class UserServiceTest {
	@Autowired
	UserService service;

	@Test
	public void loginUser() {
//		Map<String, String> param = new HashMap<String, String>();
//		param.put("userId", "user1");
//		param.put("password", "user1");
//		Assert.assertEquals(service.loginUser(param), "1");
	}
	public void findUserListTest() {
		List list = service.findUserList("");
		for (Object s : list) {
			System.out.println(s);
		}
	}
	public void SendMessageTest() {
		JSONObject json = new JSONObject();
		try {
			json.put("regId", "1");
			json.put("sendMessage", "2");
			service.sendMessage(json.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
