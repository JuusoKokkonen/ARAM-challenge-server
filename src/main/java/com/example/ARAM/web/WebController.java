package com.example.ARAM.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class WebController {

	@RequestMapping("*")
	public String main() {
		return "Welcome to the index page. Please try the following endpoint too: /test, /dbtest, /dbtest/newtest/{var} ";
	}
	
	@RequestMapping("/test")
	public String test() {
		return "Hyvin toimii :D:D:D:D:DDDDD";
	}
	
	
}
