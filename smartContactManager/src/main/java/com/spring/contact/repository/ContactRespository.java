package com.spring.contact.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.contact.entities.Contact;
import com.spring.contact.entities.User;
@Repository
public interface ContactRespository extends JpaRepository<Contact, Integer>  {

	@Query("from Contact as d where d.user.id=:userid")
	public Page<Contact> findContactsbyusere(@Param("userid")int userid,Pageable perpage);
	
	
	public List<Contact> findByNameContainingAndUser(String name,User user);
	
}
   