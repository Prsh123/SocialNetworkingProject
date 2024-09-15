package com.virinchi.controller;

import java.time.LocalDateTime;
import java.util.Random;

import org.antlr.v4.runtime.atn.SemanticContext.OR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.virinchi.model.Otp;
import com.virinchi.repository.OtpRepository;
import com.virinchi.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MailController {
	
	@Autowired
	private UserRepository uRepo;
	@Autowired
	private JavaMailSender jms;
	@Autowired
	private OtpRepository oRepo;
	@GetMapping("/RegisterConfirm")
	public String postSignUpMail(Model model,HttpServletRequest req) {
		HttpSession session = req.getSession(); 
		String to=(String) session.getAttribute("userEmail");
		String subject= "SignUp";
		String message= (String) session.getAttribute("message");;
		
		SimpleMailMessage msg= new SimpleMailMessage();
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(message);
		
//	 jms.send(msg);
		
	 model.addAttribute("notify","User Successfully Signed up! Please Login");
		return "login";
	}
	
	@GetMapping("/mailOTP")
	public String postOTP(HttpServletRequest req,Model m) {
		HttpSession session = req.getSession();
		String to=(String) session.getAttribute("OTPemail");
		if(uRepo.existsByEmail(to)) {
			String subject= "Password Reset OTP";
			 Random random = new Random();
		        
		        // Generate a random number between 100000 and 999999
		        int sixDigitNumber = 100000 + random.nextInt(900000);
		        Otp o = new Otp();
		        o.setEmail(to);
		        o.setOtp(sixDigitNumber);
		        o.setCreation_time(LocalDateTime.now());
		        oRepo.deleteByEmail(to);
		        oRepo.save(o);
			String message= "OTP to reset password: "+ sixDigitNumber +"\n"+"The OTP will not be usable after 2 mintues.";
			SimpleMailMessage msg= new SimpleMailMessage();
			msg.setTo(to);
			msg.setSubject(subject);
			msg.setText(message);
			
			jms.send(msg);
			return "reset_page";
		}
		else {
			m.addAttribute("error","email not registered");
			return "login";
		}
	}
		
		@GetMapping("/mailPasswordChange")
		public String postPasswordChange(HttpServletRequest req,Model m) {
			HttpSession session = req.getSession(); 
			String to=(String) session.getAttribute("userEmail");
			String subject= "Password Change Information";
			String message= "Password has been changed";
			
			SimpleMailMessage msg= new SimpleMailMessage();
			msg.setTo(to);
			msg.setSubject(subject);
			msg.setText(message);
			
		 jms.send(msg);
			
		 m.addAttribute("notify","Password Changed");
			return "login";
		
		
	}
}
