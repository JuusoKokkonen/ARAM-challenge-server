package com.example.ARAM.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class WebController {

	@RequestMapping("*")
	public String main() {
		String apiGuide = "Welcome to the index page. Avaliable api endpoints: "
				+ "api/challenge/{challenge_id}, "
				+ "api/user/{username}, "
				+ "api/newchallenge/{username}, "
				+ "api/refreshchallenge/{challenge_id}, "
				+ "api/champions, "
				+ "api/challengelists, "
				+ "api/championlists";
		return apiGuide;
	}
	
	
}
