package com.virinchi.controller;

import java.util.Optional;

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
public class RegisterController {
	@Autowired
	private UserRepository uRepo;
	
	@GetMapping("/register")
	public String getSinup() {
		return "register";
	}
	@PostMapping("/register")
	public String postSignup(@ModelAttribute User u, Model model,HttpServletRequest req) {
		HttpSession session = req.getSession();
		Optional<User> existingUser = uRepo.findByEmail(u.getEmail());
	        if (existingUser.isPresent()) {
	        	model.addAttribute("error","Email already signed Up. Sign Up with different email.");
				return "register";
	        }
	        
			
		String hashpwd=DigestUtils.shaHex(u.getPassword());

		u.setPassword(hashpwd);
		User savedUser = uRepo.save(u);
		if(savedUser != null) {
			String message = "Dear "+u.getFname() +" "+u.getLname() +"\n"+"Thank You For Signing Up for out Service.";
			session.setAttribute("message", message);
			session.setAttribute("userEmail", u.getEmail());
			model.addAttribute("notify","User Successfully Signed up! Please Login");
			return "redirect:/RegisterConfirm";
		}
		else {
			model.addAttribute("error","User  Signed Failed! Try Again");
			return "register";
		}

	}	
}
