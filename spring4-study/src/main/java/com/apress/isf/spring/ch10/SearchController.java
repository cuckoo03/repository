package com.apress.isf.spring.ch10;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/search")
public class SearchController {
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public String searchAll(Model model) {
		model.addAttribute("docs", "docs");
		return "search/all";
	}
}
