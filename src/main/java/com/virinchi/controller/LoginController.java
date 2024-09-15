package com.virinchi.controller;



import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.virinchi.model.User;
import com.virinchi.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	@Autowired
	private UserRepository uRepo;
	
	@GetMapping("/")
	public String getLogin() {
		return "login";
	}

	
	
	@PostMapping("/login")
	public String postLogin(@ModelAttribute User u, Model model, HttpServletRequest req) {
		
		
		HttpSession session = req.getSession();
		String hashpwd=DigestUtils.shaHex(u.getPassword());
		if(uRepo.existsByEmailAndPassword(u.getEmail(), hashpwd)) {
			
			User user =uRepo.findByEmailAndPassword(u.getEmail(),hashpwd);
			session.setAttribute("user", user);
			session.setMaxInactiveInterval(60*60*24);
			return "redirect:/newsfeed";
		}
		else {
			model.addAttribute("error","Login failed try again");
			return "login";
		}
	}
	
	@GetMapping("/logout")
	public String postLogout(HttpServletRequest req) {
		HttpSession session = req.getSession();
		session.invalidate();
		return "login";
	}
	
}
