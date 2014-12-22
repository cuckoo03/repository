package chap06.src.madvirus.spring.chap06.controller;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.ibatis.service.MyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {
	private MyService myservice;

	public MyService getMyservice() {
		return myservice;
	}

	public void setMyservice(MyService myservice) {
		this.myservice = myservice;
	}

	@RequestMapping(value = "/hello.do", method = RequestMethod.GET)
	public ModelAndView hello(String id) {
		System.out.println("id:" + id);
		ModelAndView mav = new ModelAndView("hello");
//		mav.setViewName("hello");
		mav.addObject("greeting", getGreeting());
		mav.addObject("id", id);

		return mav;
	}

	@RequestMapping(value = "/service.do", method = RequestMethod.GET)
	public ModelAndView service() {
		Map map = new HashMap();
		map.put("service", "myservice");
		return new ModelAndView("service", map);
	}

	private Object getGreeting() {
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if (hour >= 6 && hour <= 10) {
			return "6 to 10";
		} else
			return "hello";
	}
}
