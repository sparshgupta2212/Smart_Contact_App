package com.spring.contact.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.spring.contact.entities.Contact;
import com.spring.contact.entities.User;
import com.spring.contact.helper.Message;
import com.spring.contact.repository.ContactRespository;
import com.spring.contact.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired 
	private ContactRespository contactRespository;
	
	@ModelAttribute
	public void addcommondata(Model model,Principal principal) {
		String username=principal.getName();
		User user=userRepository.getUserByUserName(username);
		model.addAttribute("user",user);
	}
	
	
	@RequestMapping("/index")
	public String dasboard(Model model,Principal principal) {
		
		model.addAttribute("title","User Dashboard");
		return "normal/user_dashboard";
	}
	@GetMapping("add-contact")
	public String openAddcontactForm(Model  model) {
		model.addAttribute("title","Add-Contact");
		model.addAttribute("contact",new Contact() );
		return "normal/addcontactform";
	}
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,
			@RequestParam("picture") MultipartFile file,
			Principal principal,HttpSession session) {
		try {
		String name=principal.getName();
		User user=this.userRepository.getUserByUserName(name);
		
		
		//proocessiong and uploading file
		if(file.isEmpty()) {
			contact.setImage("contact.png");
		}else {
			contact.setImage(file.getOriginalFilename());
			File savefile=new ClassPathResource("static/image").getFile();
			Path path=Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Done");
		}
		
		user.getContacts().add(contact);
		contact.setUser(user);
		this.userRepository.save(user);
		
		session.setAttribute("message",new Message("Successfully added","success" ));
		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.setAttribute("message",new Message("something went wrong","danger"));

		}
		
		return "normal/addcontactform";
	}
	//show contact handler
	//per page=5
	//current page=0[page]
	@GetMapping("/show-contacts/{page}")
	public String showContact(@PathVariable("page") Integer page, Model m,Principal principal) {
		m.addAttribute("title","Show-Contacts");
		String username=principal.getName();
		User user=this.userRepository.getUserByUserName(username);
		Pageable pageable=PageRequest.of(page, 5);
		Page<Contact> contacts=this.contactRespository.findContactsbyusere(user.getId(),pageable);
		
		m.addAttribute("contacts",contacts);
		m.addAttribute("currentpage",page);
		m.addAttribute("totalpages",contacts.getTotalPages());
		
		return "normal/showcontact";
	}
	@GetMapping("/{cid}/contact")
	public String showContactDetails(@PathVariable("cid") Integer cid,Model m,Principal principal) {
		m.addAttribute("title","Show-Contacts detail of cid");
		
		Optional<Contact> contactoptional=this.contactRespository.findById(cid);
		Contact contact=contactoptional.get();
		//che
		String username=principal.getName();
		User user=this.userRepository.getUserByUserName(username);
		
		if(user.getId()==contact.getUser().getId()) {
			m.addAttribute("contact",contact);
			m.addAttribute("title",contact.getName());
		}
		
		
		
		return "normal/contactdetail";
	}
	@GetMapping("/delete/{cid}")
	public String deletecontact(@PathVariable("cid") Integer cid,Model m,HttpSession session,Principal principal) {
		System.out.println("asc");
		
//		Optional<Contact> contactoptional=this.contactRespository.findById(cid);
		Contact contact=this.contactRespository.findById(cid).get();
		String username=principal.getName();
		User user=this.userRepository.getUserByUserName(username);
		if(user.getId()==contact.getUser().getId()) {
		
		System.out.println(contact.getCid());
		user.getContacts().remove(contact);
		this.userRepository.save(user);
		
//		contact.setUser(null);
//		this.contactRespository.delete(contact);
		
		session.setAttribute("message",new Message("contact deleted successfully","success"));
		
		}	
		return "redirect:/user/show-contacts/0";
	}
	@PostMapping("/update-contact/{cid}")
	public String updateformm(@PathVariable("cid") Integer cid,Model m) {
		m.addAttribute("title","update form");
		Contact contact=this.contactRespository.findById(cid).get();
		m.addAttribute("contact",contact);

		return "normal/updateform";
	}
	//update handler
	@PostMapping("/process-update")
	public String updatehandler(@ModelAttribute Contact contact,@RequestParam("picture") MultipartFile file,Model m,HttpSession session,Principal principal) {
		
		try {
			Contact oldcontact=this.contactRespository.findById(contact.getCid()).get();
			if(!file.isEmpty()) {
				
				//delete photo
				File deletefile=new ClassPathResource("static/image").getFile();
				File file1=new File(deletefile,oldcontact.getImage());
				file1.delete();
				
				
				 //update photo
				File savefile=new ClassPathResource("static/image").getFile();
				Path path=Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());
				
			}else {
				contact.setImage(oldcontact.getImage());
			}
			User user=this.userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);
			this.contactRespository.save(contact);
			session.setAttribute("message",new Message("COntact Update","success"));
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "redirect:/user/"+contact.getCid()+"/contact";
	}
	//your profile handler
	@GetMapping("/profile")
	public String yourprofile(Model m) {
		m.addAttribute("title","profile page");
		return "normal/profile";
	}
	
	//open setting handler
	@GetMapping("/settings")
	public String openSetting() {
		return "normal/setting";
	}
	
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldpassword") String oldPassword,@RequestParam("newpassword") String newPassword,Principal principal,HttpSession session) {
		
		String name=principal.getName();
		User currentUser=this.userRepository.getUserByUserName(name);
		
		if(this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
			currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.userRepository.save(currentUser);
			session.setAttribute("message",new Message("your password is successfully changes", "success"));
		}else {
			session.setAttribute("message",new Message("your password not correct", "danger"));
			return "redirect:/user/settings";

		}
		return "redirect:/user/index";
	}
	
}


//Pageable sortbytile=PageRequest.of(0,2,Sort.by("title").descending);




