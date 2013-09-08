package com.service.freetalk.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.service.freetalk.service.UserService;

@Controller
public class ServletController {
	private UserService userService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = "/hello.do")
	public ModelAndView hello() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("hello");
		mav.addObject("hello", "hello world");
		return mav;
	}

	@RequestMapping(value = "/user/login.do")
	public ModelAndView login(@RequestBody String body) throws IOException {
		Map<String, String> param = new HashMap<String, String>();

		StringTokenizer token = new StringTokenizer(body, "&");
		while (token.hasMoreTokens()) {
			String[] split = token.nextToken().split("=");
			param.put(split[0], split[1]);
		}

		ModelAndView mav = new ModelAndView();
		Map map = new HashMap();
		map.put("user", userService.loginUser(param));
		mav.addObject("result", map);
		mav.setViewName("jsonView");
		return mav;
	}

	@RequestMapping(value = "/user/userList.do", method = RequestMethod.POST)
	public ModelAndView userList(@RequestBody String body) {
		ModelAndView mav = new ModelAndView();
		try {
			JSONObject json = new JSONObject(body);
			String userId = json.getString("userId");
			@SuppressWarnings("unchecked")
			List<Map<String, String>> list = userService.findUserList(userId);
			String jsonStr = null;
			try {
				ObjectMapper mapper = new ObjectMapper();
				jsonStr = mapper.writeValueAsString(list);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Map map = new HashMap();
			map.put("list", jsonStr);

			mav.addObject("result", list);
			mav.setViewName("jsonView");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mav;
	}

	@RequestMapping(value = "/device/register.do")
	public ModelAndView registerId(@RequestBody String body) {
		try {
			JSONObject json = new JSONObject(body);
			String regId = json.getString("regId");
			String userId = json.getString("userId");
			userService.registerId(regId, userId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ModelAndView mav = new ModelAndView();
		mav.setViewName("jsonView");
		return mav;
	}

	@RequestMapping(value = "/device/send.do")
	public ModelAndView sendMessage(@RequestBody String body) {
		userService.sendMessage(body);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("jsonView");
		return mav;
	}
}
