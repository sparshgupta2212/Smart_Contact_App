package com.spring.contact.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class forgetController {

	@RequestMapping("/forgot")
	public String openEmailForm() {
		return  "forgot_email_form";	
	}
}
