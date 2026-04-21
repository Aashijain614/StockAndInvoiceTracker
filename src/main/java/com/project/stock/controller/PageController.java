package com.project.stock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.stock.entity.User;
import com.project.stock.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class PageController {
	private final UserService userService;

	@GetMapping("/")
	public String getHome() {
	    return "home";
	}
	
	@GetMapping("/register")
    public String signupPage(Model model) {
        model.addAttribute("user", new User()); // binding object
        return "signUp";
    }
	
	@PostMapping("/signup")
    public String registerUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/login";
    }
	
	@GetMapping("/login")
    public String login(Model model) {
        
        return "login";
    }
	
	
	@PostMapping("/login")
	public String loginUser(@RequestParam String email,
            @RequestParam String password, Model model,HttpSession session) {

	    User existingUser = userService.loginUser(email,password);

	    if (existingUser == null) {
	        model.addAttribute("error", "Invalid email or password!");
	        return "login";
	    }
	    session.setAttribute("loggedInUser", existingUser);

	    if (existingUser.getRole().equals("ADMIN")) {
	        return "redirect:/admin/dashboard";
	    } else {
	        return "redirect:/cashier/dashboard";
	    }
	    
	}
	
}
