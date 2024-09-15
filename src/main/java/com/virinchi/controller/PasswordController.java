package com.virinchi.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.virinchi.model.Otp;
import com.virinchi.repository.OtpRepository;
import com.virinchi.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class PasswordController {
	private static final long EXPIRATION_MINUTES = 2;
	@Autowired
	private OtpRepository oRepo;
	
	@Autowired
	private UserRepository uRepo;
	
	@GetMapping("/forgotpassword")
	public String getForgot() {
		return "password_reset";
	}
	
	@PostMapping("/sendOTP")
	public String getOTP(@RequestParam("email") String email,HttpServletRequest req) {
		HttpSession session = req.getSession();
		session.setAttribute("OTPemail", email);
		return "redirect:/mailOTP";
	}

	
	@PostMapping("/verifyOTP")
	public String getOPTVerify(@RequestParam("OTP") int otpInput,HttpServletRequest req,Model m) {
		HttpSession session = req.getSession();
		String email = (String) session.getAttribute("OTPemail");
		Otp o = oRepo.findByEmail(email);
		LocalDateTime now = LocalDateTime.now();
		
        if (ChronoUnit.MINUTES.between(o.getCreation_time(), now) <= EXPIRATION_MINUTES) {
            return "changePassword";
        } else {
        	m.addAttribute("error","OTP expired");
            return "password_reset"; // Or throw an exception, depending on your needs
        }
	}
	
	@PostMapping("/change_Password")
	public String getChangePassword(@RequestParam("password") String password,HttpServletRequest req,Model m) {
		HttpSession session = req.getSession();
		String email = (String) session.getAttribute("OTPemail");
		String hashpassword = DigestUtils.shaHex(password);
		uRepo.updatePasswordByEmail(email, hashpassword);
		m.addAttribute("notify","Password Changed");
		return "redirect:/mailPasswordChange";
		
	}
	
}
