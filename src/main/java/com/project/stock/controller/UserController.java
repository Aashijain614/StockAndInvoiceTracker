package com.project.stock.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.stock.entity.User;
import com.project.stock.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@GetMapping("/search-users")
	public String manageUsers(@RequestParam(required = false) String keyword,
	                          Model model) {

	    List<User> users;

	    if (keyword != null && !keyword.isEmpty()) {
	        users = userService.searchUsers(keyword);
	    } else {
	        users = userService.getAllUsers();
	    }

	    model.addAttribute("users", users);
	    return "admin/manage-users";
	}
}
