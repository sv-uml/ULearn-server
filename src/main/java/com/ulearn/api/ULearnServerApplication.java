package com.ulearn.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SpringBootApplication
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:3003", "http://ulearn-uml.herokuapp.com" })
public class ULearnServerApplication {
	
	@Autowired
	private AppDao appDao;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> login(@RequestBody Map<String, Object> req) {
		Map<String, Object> ret = new HashMap<String, Object>();
		String email = String.valueOf(req.get("email"));
		String password = String.valueOf(req.get("password"));

		Map<String, Object> user = appDao.getUserByEmail(email);
		
		if (user.isEmpty()) {
			// User not found, error.
			ret.put("message", "Invalid email/password.");
			return ret;
		}
		
		String dbPassword = String.valueOf(user.get("password"));
		
		if (BCrypt.checkpw(password, dbPassword)) {
			// Valid user
			ret.put("id", user.get("id"));
			ret.put("name", String.valueOf(user.get("name")));
			ret.put("email", email);
		}
		return ret;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> register(@RequestBody Map<String, Object> req) {
		Map<String, Object> ret = new HashMap<String, Object>();
		String name = String.valueOf(req.get("name"));
		String email = String.valueOf(req.get("email"));
		String password = String.valueOf(req.get("password"));
		if (appDao.userExists(email)) {
			ret.put("message", "User exists");
			return ret;
		}
		String newPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		int id = appDao.register(name, email, newPassword);
		ret.put("id", id);
		return ret;
	}

	public static void main(String[] args) {
		SpringApplication.run(ULearnServerApplication.class, args);
	}
}
