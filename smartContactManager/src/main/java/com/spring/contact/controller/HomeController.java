package com.spring.contact.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.contact.entities.User;
import com.spring.contact.helper.Message;
import com.spring.contact.repository.UserRepository;

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordencoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/home")
	public String home(Model model) {
		model.addAttribute("title","Home-smart Contact Manager");
		return "home";
	}
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title","About-smart Contact Manager");
		return "about";
	}
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title","Register - smart Contact Manager");
		model.addAttribute("user",new User());
		return "signup";
	}
	//this handler for registration user
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult bindingResult,
			@RequestParam(value="agreement",defaultValue = "false")
	boolean agreement,Model model,HttpSession session) {
		
		try{
			
			if(!agreement) {
				throw new Exception("you have not agreed the terms and conditions");
			}
			if(bindingResult.hasErrors()) {
				System.out.println("ednq");
				model.addAttribute("user",user);
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageurl("default.png");
			user.setPassword(passwordencoder.encode(user.getPassword()));
			
//		
//		System.out.println(agreement);
//		System.out.println(user);
			User result=this.userRepository.save(user);
			model.addAttribute("user",new User());
			session.setAttribute("message",new Message("Successfully Registered !!","alert-success"));
			return "signup";
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message",new Message("Something Went Wrong" +e.getMessage(), "alert-danger"));
			return "signup";
		}
	}
	//handler fore login
	@GetMapping("/signin")
	public String customLogin(Model model) {
		model.addAttribute("title","Login - smart Contact Manager");
		return "login";
	}
	
	
	
}
