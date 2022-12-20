package com.spring.contact.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.contact.entities.User;
import com.spring.contact.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//fetching the user from databse
		
		User user=userRepository.getUserByUserName(username);
		if(user==null) {
			throw new UsernameNotFoundException("Not Found");
		}
		CustomUserDetails customUserDetails=new CustomUserDetails(user);
		
		return customUserDetails;
	}
	

}
