package com.ulearn.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SpringBootApplication
public class ULearnServerApplication {
	
	@RequestMapping("/")
	@ResponseBody
	public String getIndex() {
		return "ULearn API";
	}

	public static void main(String[] args) {
		SpringApplication.run(ULearnServerApplication.class, args);
	}
}
