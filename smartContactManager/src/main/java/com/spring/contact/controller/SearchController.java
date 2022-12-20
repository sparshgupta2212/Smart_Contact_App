package com.spring.contact.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.spring.contact.entities.Contact;
import com.spring.contact.entities.User;
import com.spring.contact.repository.ContactRespository;
import com.spring.contact.repository.UserRepository;

@RestController
public class SearchController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRespository contactRespository;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> searchh(@PathVariable("query") String query,Principal principal){
		
		User user=this.userRepository.getUserByUserName(principal.getName());
		List<Contact> contact=this.contactRespository.findByNameContainingAndUser(query, user);
		
		return ResponseEntity.ok(contact);
	}
}
