package com.greencloud.website.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.greencloud.website.model.User;
import com.greencloud.website.service.IUserService;

@RestController
public class LoginController {
	
	private @Resource IUserService userService;
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String login(String username, String password, String checkcode, Model model, HttpSession session) {
		User user = userService.findByName(username);
		model.addAttribute(user);
		return "hello";
	}
}
